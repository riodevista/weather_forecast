package ru.dmitrybochkov.weatherforecast.domain;

import io.realm.RealmObject;

/**
 * Created by Dmitry Bochkov on 05.06.2017.
 */

public class Weather extends RealmObject {

    private String description;
    private double temp;
    private double humidity;
    private double pressure;
    private String iconId;
    private long date;

    public Weather() {}

    public Weather(String description, double temp, double humidity, double pressure, String iconId, long date) {
        this.description = description;
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.iconId = iconId;
        this.date = date;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
