package ru.dmitrybochkov.weatherforecast.ui.presenters;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.dmitrybochkov.weatherforecast.data.api.v1.RetrofitGenerator;
import ru.dmitrybochkov.weatherforecast.data.api.v1.mappers.WeatherMapper;
import ru.dmitrybochkov.weatherforecast.data.api.v1.pojo.WeatherResponse;
import ru.dmitrybochkov.weatherforecast.domain.City;
import ru.dmitrybochkov.weatherforecast.domain.Weather;
import ru.dmitrybochkov.weatherforecast.ui.views.LauncherView;

/**
 * Created by Dmitry Bochkov on 03.06.2017.
 */

public class LauncherPresenter extends MvpBasePresenter<LauncherView> {

    private AtomicBoolean retrofitRequestProcessing = new AtomicBoolean(false);

    public void searchCity(String cityName) {
        if (!retrofitRequestProcessing.compareAndSet(false, true))
            return;
        if (isViewAttached())
            getView().showProgress();
        RetrofitGenerator.getOpenWeatherAPI().getCurrentWeather(cityName).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                switch (response.code()) {
                    case 200:
                        saveCityAndWeather(response.body());
                        if (isViewAttached())
                            getView().showWeatherForecast(response.body().getName());
                        break;
                    case 404:
                        if (isViewAttached())
                            getView().showCityNotFoundError();
                        break;
                    default:
                        if (isViewAttached())
                            getView().showUnknownError();
                }
                endRetrofitRequest();
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
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

    private void saveCityAndWeather(final WeatherResponse weatherResponse) {
        final City city = new City(weatherResponse.getName());
        Weather weather = WeatherMapper.getWeather(weatherResponse);
        city.setCurrentWeather(weather);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
                                     @Override
                                     public void execute(Realm realm) {
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
