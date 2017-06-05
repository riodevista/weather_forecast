package ru.dmitrybochkov.weatherforecast.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import ru.dmitrybochkov.weatherforecast.AppConstants;
import ru.dmitrybochkov.weatherforecast.R;
import ru.dmitrybochkov.weatherforecast.domain.City;
import ru.dmitrybochkov.weatherforecast.ui.adapters.CitiesListAdapter;
import ru.dmitrybochkov.weatherforecast.ui.common.RecyclerItemClickListener;
import ru.dmitrybochkov.weatherforecast.ui.presenters.LauncherPresenter;
import ru.dmitrybochkov.weatherforecast.ui.views.LauncherView;

/**
 * Created by Dmitry Bochkov on 03.06.2017.
 */

public class LauncherActivity extends MvpActivity<LauncherView, LauncherPresenter> implements LauncherView{

    private CitiesListAdapter citiesListAdapter;

    @BindView(R.id.cities_list)
    RecyclerView citiesList;
    @BindView(R.id.city_name_input)
    EditText cityName;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private SharedPreferences sharedPreferences;
    private Realm realm;


    @NonNull
    @Override
    public LauncherPresenter createPresenter() {
        return new LauncherPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        citiesList.setAdapter(null);
        realm.close();
    }

    private void init() {
        sharedPreferences = getSharedPreferences(AppConstants.PREFERENCES, MODE_PRIVATE);
        realm = Realm.getDefaultInstance();
        setUpRecyclerView();

        cityName.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_GO) {
                            presenter.searchCity(cityName.getText().toString());
                            return true;
                        }
                        return false;
                    }
                });

        if (sharedPreferences.getBoolean(AppConstants.SHOW_WEATHER_FORECAST, false)) {
            String cityName = sharedPreferences.getString(AppConstants.ACTIVE_CITY, null);
            if (cityName != null)
                showWeatherForecast(cityName);
        }

    }

    private void setUpRecyclerView() {
        citiesList.setLayoutManager(new LinearLayoutManager(this));
        citiesList.setAdapter(citiesListAdapter = new CitiesListAdapter(realm.where(City.class).findAll()));
        citiesList.setHasFixedSize(true);

        citiesList.addOnItemTouchListener(new RecyclerItemClickListener(this, citiesList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        City city = citiesListAdapter.getItem(position);
                        presenter.searchCity(city.getName());
                    }

                    @Override public void onLongItemClick(View view, int position) {
                    }
        }));
    }

    @Override
    public void showConnectionError() {
        Toast.makeText(this, R.string.connection_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCityNotFoundError() {
        Toast.makeText(this, R.string.city_is_not_found, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showWeatherForecast(String cityName) {
        sharedPreferences.edit().putString(AppConstants.ACTIVE_CITY, cityName).apply();
        startWeatherForecastActivity(cityName);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showUnknownError() {
        Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
    }

    private void startWeatherForecastActivity(String cityName) {
        Intent intent = new Intent(this, WeatherForecastActivity.class);
        intent.putExtra("city", cityName);
        startActivity(intent);
    }
}
