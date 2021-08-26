package com.weather.bremachitra.checkweather.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.weather.bremachitra.checkweather.R;
import com.weather.bremachitra.checkweather.interfaces.onSwipeListener;
import com.weather.bremachitra.checkweather.models.CityWeather;
import com.weather.bremachitra.checkweather.utils.Weathericon;
import com.squareup.picasso.Picasso;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Cityweatheradapter extends RecyclerView.Adapter<Cityweatheradapter.ViewHolder> implements onSwipeListener {
    private Boolean[] chkArr;
    private List<CityWeather> cities;
    private int layoutReference;
    private OnItemClickListener onItemClickListener;
    private Activity activity;
    private View parentView;

    public Cityweatheradapter(List<CityWeather> cities, int layoutReference, Activity activity,OnItemClickListener onItemClickListener) {
        this.cities = cities;
        this.layoutReference = layoutReference;
        this.activity = activity;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public Cityweatheradapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        parentView = parent;
        View view = LayoutInflater.from(activity).inflate(layoutReference,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Cityweatheradapter.ViewHolder holder, int position) {
        Log.i("Position",""+position);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Log.i("clk called on position",""+position);
                                                   if( holder.checkBox.isChecked())
                                                   {
                                                       Log.i("position to remove",""+cities.get(position));
                                                       cities.get(position).checkedornot= 2;
                                                       Log.i("set to 2 to remove",cities.get(position).getName());
                                                   }
                                                   else
                                                   {
                                                       cities.get(position).checkedornot= 0;
                                                       Log.i("set to 0 dont remove",cities.get(position).getName());
                                                   }
                                               }
                                           }

        );
        holder.bind(cities.get(position),onItemClickListener);
    }

    @Override

    public int getItemCount() {
        return cities.size();
    }

    @Override
    public void onItemDelete(final int position) {
        CityWeather tempCity = cities.get(position);
        cities.remove(position);
        notifyItemRemoved(position);

        Snackbar.make(parentView, "Removed", Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> {
                    addItem(position, tempCity);
                    //new MainActivity().recyclerScrollTo(position);
                }).show();

    }
    public void addItem(int position, CityWeather city) {
        Log.i("addItem city.......",city.getName().toString());
        cities.add(position, city);
        notifyItemInserted(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.textViewCardCityName)
        TextView textViewCityName;
        @BindView(R.id.textViewCardCurrentTemp)
        TextView textViewCurrentTemp;
        @BindView(R.id.textViewCardMaxTemp)
        TextView textViewMaxTemp;
        @BindView(R.id.textViewCardMinTemp)
        TextView  textViewMinTemp;
        @BindView(R.id.cardViewWeather) CardView cardViewWeather;
        @BindView(R.id.textViewCardWeatherDescription) TextView textViewDescription;
        @BindView(R.id.imageViewCardWeatherIcon)
        ImageView weatherIcon;
        @BindView(R.id.checkBox)
        CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final CityWeather cityWeather, final OnItemClickListener onItemClickListener){
            Log.i("CityWeather----",""+cityWeather.toString());
            try {
              textViewCityName.setText(cityWeather.getName() + ", " + cityWeather.getSys().getCountry());

                textViewDescription.setText(cityWeather.getWeather().get(0).getDescription());
                int current = (int) (((double)cityWeather.main.getTemp()) - 273.15)*9/5 + 32;
                int minTemp = (int) ((((double)cityWeather.main.getTemp_min()) - 273.15)*9/5 + 32);
                int maxTemp =  (int)(((double)cityWeather.main.getTemp_max()) - 273.15)*9/5 + 32;
                textViewCurrentTemp.setText(current +"°");
                textViewMinTemp.setText(minTemp + "°");
                textViewMaxTemp.setText(maxTemp+ "°");
                String weatherDes = cityWeather.getWeather().get(0).getMain();
                Picasso.get().load(Weathericon.getIcon(weatherDes)).into(weatherIcon);
                cardViewWeather.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(cityWeather,getAdapterPosition(),cardViewWeather);
                    }
                });
            }
            catch(Exception e)
            {
                Log.i("Exception",""+e.toString());
            }
        }
    }

        public interface  OnItemClickListener
        {
        void onItemClick(CityWeather cityweather,int position,View view);
        }
}


