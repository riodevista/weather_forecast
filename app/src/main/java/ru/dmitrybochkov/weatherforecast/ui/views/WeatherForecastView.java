package ru.dmitrybochkov.weatherforecast.ui.views;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by Dmitry Bochkov on 05.06.2017.
 */

public interface WeatherForecastView extends MvpView {

    void showConnectionError();

//    void showProgress();

    void hideProgress();

    void showUnknownError();
}
