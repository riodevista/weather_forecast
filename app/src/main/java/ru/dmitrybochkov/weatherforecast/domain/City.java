package ru.dmitrybochkov.weatherforecast.domain;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Dmitry Bochkov on 05.06.2017.
 */

public class City extends RealmObject{

    @PrimaryKey
    private String name;

    private Weather currentWeather;
    private RealmList<Weather> forecast = new RealmList<>();

    public City() {}


    public City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(Weather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public RealmList<Weather> getForecast() {
        return forecast;
    }

    public void setForecast(RealmList<Weather> forecast) {
        this.forecast = forecast;
    }
}
