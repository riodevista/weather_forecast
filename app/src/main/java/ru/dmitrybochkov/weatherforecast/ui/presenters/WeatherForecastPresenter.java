package ru.dmitrybochkov.weatherforecast.ui.presenters;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.dmitrybochkov.weatherforecast.data.api.v1.RetrofitGenerator;
import ru.dmitrybochkov.weatherforecast.data.api.v1.mappers.WeatherMapper;
import ru.dmitrybochkov.weatherforecast.data.api.v1.pojo.ForecastResponse;
import ru.dmitrybochkov.weatherforecast.domain.City;
import ru.dmitrybochkov.weatherforecast.domain.Weather;
import ru.dmitrybochkov.weatherforecast.ui.views.WeatherForecastView;

/**
 * Created by Dmitry Bochkov on 05.06.2017.
 */

public class WeatherForecastPresenter extends MvpBasePresenter<WeatherForecastView> {

    private AtomicBoolean retrofitRequestProcessing = new AtomicBoolean(false);


    public void updateWeatherForecast(final String cityName) {
        if (!retrofitRequestProcessing.compareAndSet(false, true))
            return;
        RetrofitGenerator.getOpenWeatherAPI().getWeatherForecast(cityName).enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                if (response.isSuccessful())
                    saveWeatherForecast(cityName, response.body());
                else
                    if (isViewAttached())
                        getView().showUnknownError();
                endRetrofitRequest();
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                if (t instanceof UnknownHostException) {
                    if (isViewAttached())
                        getView().showConnectionError();
                } else {
                    if (isViewAttached())
                        getView().showUnknownError();
                }
                endRetrofitRequest();
            }
        });
    }

    private void saveWeatherForecast(final String cityName, ForecastResponse body) {
        final RealmList<Weather> weatherList = WeatherMapper.getWeatherForecast(body);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                List<Weather> forecast = realm.copyToRealmOrUpdate(weatherList);
                City city = realm.where(City.class).equalTo("name", cityName).findFirst();
                city.getForecast().addAll(forecast);
                realm.copyToRealmOrUpdate(city);
                realm.close();
            }
        });
    }

    private void endRetrofitRequest() {
        if (isViewAttached())
            getView().hideProgress();
        retrofitRequestProcessing.set(false);
    }
}
