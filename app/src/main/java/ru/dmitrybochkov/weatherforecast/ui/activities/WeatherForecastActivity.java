package ru.dmitrybochkov.weatherforecast.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmList;
import ru.dmitrybochkov.weatherforecast.AppConstants;
import ru.dmitrybochkov.weatherforecast.R;
import ru.dmitrybochkov.weatherforecast.domain.City;
import ru.dmitrybochkov.weatherforecast.domain.Weather;
import ru.dmitrybochkov.weatherforecast.ui.adapters.WeatherListAdapter;
import ru.dmitrybochkov.weatherforecast.ui.presenters.WeatherForecastPresenter;
import ru.dmitrybochkov.weatherforecast.ui.views.WeatherForecastView;

/**
 * Created by Dmitry Bochkov on 05.06.2017.
 */

public class WeatherForecastActivity extends MvpActivity<WeatherForecastView, WeatherForecastPresenter> implements WeatherForecastView{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.top_image)
    ImageView topImage;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.weather_list)
    RecyclerView weatherList;
    @BindView(R.id.temperature)
    TextView temperature;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.humidity)
    TextView humidity;
    @BindView(R.id.pressure)
    TextView pressure;
    @BindView(R.id.weather_icon)
    ImageView weatherIcon;

    private SharedPreferences sharedPreferences;
    private Realm realm;

    private City city;

    @NonNull
    @Override
    public WeatherForecastPresenter createPresenter() {
        return new WeatherForecastPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences(AppConstants.PREFERENCES, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(AppConstants.SHOW_WEATHER_FORECAST, true).apply();

        realm = Realm.getDefaultInstance();

        String cityName = getIntent().getStringExtra("city");

        city = realm.where(City.class).equalTo("name", cityName).findFirst();

        setUpViews();

        updateWeatherForecast();
    }

    @Override
    public boolean onSupportNavigateUp() {
        sharedPreferences.edit().putBoolean(AppConstants.SHOW_WEATHER_FORECAST, false).apply();
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        weatherList.setAdapter(null);
        realm.removeAllChangeListeners();
    }

    private void setUpViews() {
        //Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(city.getName());

        //Top image
        Glide.with(this)
            .load("https://pp.userapi.com/c637631/v637631282/53a3d/XgOdBAJj12o.jpg")
            .into(topImage);

        //RecyclerView with weather
        weatherList.setLayoutManager(new LinearLayoutManager(this));
        weatherList.setAdapter(new WeatherListAdapter(city.getForecast()));

        //Current weather
        updateCurrentWeatherViews();

        //SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.updateWeatherForecast(city.getName());
            }
        });

        //Update RecyclerView when forecast updated
        city.getForecast().addChangeListener(new OrderedRealmCollectionChangeListener<RealmList<Weather>>() {
            @Override
            public void onChange(RealmList<Weather> weathers, OrderedCollectionChangeSet changeSet) {
                weatherList.getAdapter().notifyDataSetChanged();
            }
        });
    }

    private void updateCurrentWeatherViews() {
        Glide.with(this)
                .load(getString(R.string.icons_url, city.getCurrentWeather().getIconId()))
                .into(weatherIcon);
        temperature.setText(getString(R.string.temp, String.valueOf(city.getCurrentWeather().getTemp())));
        humidity.setText(getString(R.string.humidity, String.valueOf(city.getCurrentWeather().getHumidity())));
        pressure.setText(getString(R.string.pressure, String.valueOf(city.getCurrentWeather().getPressure())));
        description.setText(city.getCurrentWeather().getDescription());
    }

    private void updateWeatherForecast() {
        presenter.updateWeatherForecast(city.getName());
    }

    @Override
    public void showConnectionError() {
        Toast.makeText(this, R.string.connection_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showUnknownError() {
        Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
    }
}
