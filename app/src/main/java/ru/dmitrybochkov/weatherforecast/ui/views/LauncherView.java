package ru.dmitrybochkov.weatherforecast.ui.views;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by Dmitry Bochkov on 03.06.2017.
 */

public interface LauncherView extends MvpView {

    void showConnectionError();

    void showCityNotFoundError();

    void showWeatherForecast(String cityName);

    void showProgress();

    void hideProgress();

    void showUnknownError();
}
