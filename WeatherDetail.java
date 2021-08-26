package com.weather.bremachitra.checkweather.activities;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.weather.bremachitra.checkweather.R;
import com.weather.bremachitra.checkweather.models.CityWeather;
import com.weather.bremachitra.checkweather.utils.Weathericon;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherDetail extends AppCompatActivity {
    @BindView(R.id.textViewCardCityName)
    TextView textViewCityName;
    @BindView(R.id.textViewCardCurrentTemp)
    TextView textViewCurrentTemp;
    @BindView(R.id.textViewCardMaxTemp)
    TextView textViewMaxTemp;
    @BindView(R.id.textViewCardMinTemp)
    TextView  textViewMinTemp;
    @BindView(R.id.textViewHumidity)
    TextView textViewhumidity;
    @BindView(R.id.textViewPressure)
    TextView textViewPressure;
    @BindView(R.id.textViewCardWeatherDescription)
    TextView textViewDescription;
    @BindView(R.id.imageViewCardWeatherIcon)
    ImageView weatherIcon;
    @BindView(R.id.textViewSunrise)
    TextView textViewSunrise;
    @BindView(R.id.textViewSunset)
    TextView textViewSunset;
    @BindView(R.id.textViewWind)
    TextView textViewWind;
    CityWeather cityWeather;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.weather_detail_layout);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if(! bundle.isEmpty())
        {
        cityWeather = (CityWeather) bundle.getSerializable("city");
        }
        Log.i("city",cityWeather.getName());
        textViewCityName.setText(cityWeather.getName() + ", " + cityWeather.getSys().getCountry());
        textViewDescription.setText(cityWeather.getWeather().get(0).getDescription());
        String weatherDes = cityWeather.getWeather().get(0).getMain();
        Picasso.get().load(Weathericon.getIcon(weatherDes)).into(weatherIcon);
        int current = (int) (((double) cityWeather.main.getTemp()) - 273.15) * 9 / 5 + 32;
        int minTemp = (int) ((((double) cityWeather.main.getTemp_min()) - 273.15) * 9 / 5 + 32);
        int maxTemp = (int) (((double) cityWeather.main.getTemp_max()) - 273.15) * 9 / 5 + 32;
        textViewCurrentTemp.setText(current + "°");
        textViewMinTemp.setText(minTemp + "°");
        textViewMaxTemp.setText(maxTemp + "°");
        textViewhumidity.setText(""+cityWeather.getMain().getHumidity()+" %");
        textViewPressure.setText(""+cityWeather.getMain().getPressure()+" hPa");
        TimeZone timezone = java.util.TimeZone.getDefault();
        long unixSecondsSunrise = cityWeather.getSys().getSunrise();
        long unixSecondsSunset = cityWeather.getSys().getSunset();
        Date currentdate = new Date(new Date().getTime());
        if ((Boolean) timezone.inDaylightTime(currentdate))
        {
            unixSecondsSunrise -= 3600;
            unixSecondsSunset  -=3600;
        }
        Date sunrisedate = new java.util.Date(unixSecondsSunrise*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm a");
        sdf.setTimeZone(timezone);
        String formattedDateSunrise = sdf.format(sunrisedate);
        textViewSunrise.setText(formattedDateSunrise);
        Date dateset = new java.util.Date(unixSecondsSunset*1000L);
        SimpleDateFormat sdfset = new java.text.SimpleDateFormat("hh:mm a");
        sdfset.setTimeZone(timezone);
        String formattedDateSunset = sdfset.format(dateset);
        textViewSunset.setText(formattedDateSunset);
        textViewWind.setText(""+cityWeather.getWind().getSpeed()+ " m/s");
    }
}
