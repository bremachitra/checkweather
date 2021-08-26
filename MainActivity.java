package com.weather.bremachitra.checkweather.activities;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import com.weather.bremachitra.checkweather.API.Api;
import com.weather.bremachitra.checkweather.API.Apiservices.WeatherServices;
import com.weather.bremachitra.checkweather.R;
import com.weather.bremachitra.checkweather.adapter.Cityweatheradapter;
import com.weather.bremachitra.checkweather.interfaces.onSwipeListener;
import com.weather.bremachitra.checkweather.models.CityWeather;
import com.weather.bremachitra.checkweather.utils.ItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.CirclePromptBackground;
import static java.lang.Thread.sleep;
import static retrofit2.converter.gson.GsonConverterFactory.create;

public class MainActivity extends AppCompatActivity {
    private List<CityWeather> cities;
    @BindView(R.id.recyclerWeather)
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    @BindView(R.id.swipeRefreshWeather)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fabAddLocation)
    FloatingActionButton fabAddCity ;
    private WeatherServices weatherServices;
    private MaterialTapTargetPrompt mFabPrompt;
    @BindView(R.id.imageDelete)
    FloatingActionButton deleteImage;
    public static final String MY_PREFS_NAME = "bremachitra.MyPrefsFile";
    int i =0 ;
    boolean prefile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        SharedPreferences settings = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Set<String> getset = new LinkedHashSet<>();
        Set<String> restoredcity = settings.getStringSet("cities",getset);
        Log.i("restored city", restoredcity.toString());
                if(restoredcity.isEmpty())
                {
                    cities = getCities();
                    if(cities.size() == 0){
                        showFabPrompt();
                    }
                }
                else
                {
                 prefile= true;
                    cities = getCities();
                 Log.i("shared preference","true");
                }
        weatherServices = Api.getApi().create(WeatherServices.class);
        layoutManager = new LinearLayoutManager(this);
        adapter = new Cityweatheradapter(cities, R.layout.weather_layout, this, new Cityweatheradapter.OnItemClickListener() {

            @Override
            public void onItemClick(CityWeather cityweather, int position, View view) {
            Intent intent =new Intent(MainActivity.this,WeatherDetail.class);
                intent.putExtra("city",cityweather);
                startActivity(intent);
                }
            });

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy >0) {
                    // Scroll Down
                    if (fabAddCity.isShown()) {
                        fabAddCity.show();
                        deleteImage.show();

                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!fabAddCity.isShown()) {
                        fabAddCity.show();
                        deleteImage.show();
                    }
                }
            }
        });

        if(prefile)
        {
            try{
                sleep(1000);
                for(String s   : restoredcity)
                { Log.i("S............",""+s);
                sleep(5000);
                    addCity(s);
                }

            }
            catch(Exception e)
            {

            }

        }

        fabAddCity.setOnClickListener(view -> {
            showAlertAddCity("Add city","Type the city you want to add");
        });

        swipeRefreshLayout.setOnRefreshListener( () ->{
            Log.i("calling RefreshListener","updating now");
        refreshData();
                }
        );

        deleteImage.setOnClickListener(view ->{
           removeCity();
        });
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback((onSwipeListener) adapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

        private String cityToAdd ="";
        public void showAlertAddCity(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(title!=null) builder.setTitle(title);
        if(message!=null) builder.setMessage(message);
        final View view = LayoutInflater.from(this).inflate(R.layout.add_location_layout,null);
        builder.setView(view);
        final TextView editTextAddCityName = view.findViewById(R.id.editTextCityName);
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cityToAdd = editTextAddCityName.getText().toString();
                Log.i("CityToADD---------",cityToAdd);
                addCity(cityToAdd);
                imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS,0);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS,0);
                Toast.makeText(MainActivity.this,"Cancel",Toast.LENGTH_LONG).show();
            }
        });
        builder.create().show();
    }

    public void addCity(String cityName){
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        Set<String> set = new LinkedHashSet<>();
        editor.clear().commit();
        Log.i("City to add",cityName);
        try {
            sleep(2000);
        }
        catch(Exception e)
        {

        }
        Call<CityWeather> cityWeather = weatherServices.getWeatherCity(cityName, Api.KEY);
        cityWeather.enqueue(new Callback<CityWeather>() {
            @Override
            public void onResponse(Call<CityWeather> call, Response<CityWeather> response) {
                if(response.code()==200){
                    CityWeather cityWeather = response.body();
                    cities.add(cityWeather);
                    for(int i=0; i < cities.size(); i++) {
                        set.add(cities.get(i).name);
                        Log.i("City",cities.get(i).name);
                    }

                    Log.i("Set",set.toString());
                    editor.putStringSet("cities",set );
                    editor.commit();
                    adapter.notifyItemInserted(cities.size()-1);
                    recyclerView.scrollToPosition(cities.size()-1);
                }else{
                    Toast.makeText(MainActivity.this,"Sorry, city not found",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CityWeather> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Sorry, weather services are currently unavailable",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void refreshData() {
        for (int i = 0; i < cities.size(); i++) {
           updateCity(cities.get(i).getName(), i);

        }
        swipeRefreshLayout.setRefreshing(false);
    }
    public void updateCity(String cityName, int index){
        Call<CityWeather> cityWeather = weatherServices.getWeatherCity(cityName, Api.KEY);
        cityWeather.enqueue(new Callback<CityWeather>() {
            @Override
            public void onResponse(Call<CityWeather> call, Response<CityWeather> response) {
                if(response.code()==200){
                    CityWeather cityWeather = response.body();
                    cities.remove(index);
                    cities.add(index,cityWeather);
                    adapter.notifyItemChanged(index);
                }
            }

            @Override
            public void onFailure(Call<CityWeather> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Sorry, can't refresh right now.",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void removeCity()
    {

        for(int i=0; i < cities.size(); i++) {
            if (cities.size() != 0)
            {
                if (cities.get(i).checkedornot == 2) {
                    Log.i(cities.get(i).name, " removed");
                    cities.remove(i);
                    adapter.notifyItemRemoved(i);
                    i--;
                }
            }
        }
      //  adapter.notifyItemRangeRemoved(0,cities.size());
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        Set<String> set = new LinkedHashSet<>();
        editor.clear().commit();
        for(int i=0; i < cities.size(); i++) {
            set.add(cities.get(i).name);
            Log.i("City",cities.get(i).name);
        }
        Log.i("Set after removed",set.toString());
        editor.putStringSet("cities",set );
        editor.commit();
    }
    public void showFabPrompt()
    {
        if (mFabPrompt != null)
        {
            return;
        }
       mFabPrompt = new MaterialTapTargetPrompt.Builder(MainActivity.this)
                .setTarget(findViewById(R.id.fabAddLocation))
                .setFocalPadding(R.dimen.dp40)
                .setPrimaryText("Tap the add button")
                .setPrimaryTextColour(getResources().getColor(R.color.colorAccent))
                .setBackgroundColour(getResources().getColor(R.color.colorYellow))
                .setSecondaryText("Get the weather of your favorite cities")
                .setSecondaryTextColour(getResources().getColor(R.color.colorPrimaryDark))
                .setBackButtonDismissEnabled(true)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setPromptBackground(new CirclePromptBackground())
                .setPromptStateChangeListener((prompt,state) -> {
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_DISMISSING )
                    {
                        mFabPrompt = null;

                    }
                })
                .create();
        mFabPrompt.show();
    }
    private List<CityWeather> getCities() {
        return new ArrayList<CityWeather>(){
            {
            }
        };
    }
}
