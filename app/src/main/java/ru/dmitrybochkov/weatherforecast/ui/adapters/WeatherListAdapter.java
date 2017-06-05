package ru.dmitrybochkov.weatherforecast.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import ru.dmitrybochkov.weatherforecast.R;
import ru.dmitrybochkov.weatherforecast.domain.Weather;

/**
 * Created by Dmitry Bochkov on 05.06.2017.
 */

public class WeatherListAdapter extends RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder> {

    private RealmList<Weather> weatherList;

    public WeatherListAdapter(RealmList<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.weather_layout, parent, false);
            return new WeatherViewHolder(v);
            }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        holder.date.setText(simpleDateFormat.format(new Date(weatherList.get(position).getDate() * 1000)));
        holder.temperature.setText(String.valueOf(weatherList.get(position).getTemp()));
    }

    @Override
    public int getItemCount() {
        return weatherList != null ? weatherList.size() : 0;
    }


    class WeatherViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        public TextView date;
        @BindView(R.id.temperature)
        public TextView temperature;

        WeatherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
