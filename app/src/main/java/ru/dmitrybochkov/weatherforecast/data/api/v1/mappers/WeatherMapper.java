package ru.dmitrybochkov.weatherforecast.data.api.v1.mappers;

import io.realm.RealmList;
import ru.dmitrybochkov.weatherforecast.data.api.v1.pojo.ForecastResponse;
import ru.dmitrybochkov.weatherforecast.data.api.v1.pojo.WeatherList;
import ru.dmitrybochkov.weatherforecast.data.api.v1.pojo.WeatherResponse;
import ru.dmitrybochkov.weatherforecast.domain.Weather;

/**
 * Created by Dmitry Bochkov on 05.06.2017.
 */

public abstract class WeatherMapper {

    public static Weather getWeather(WeatherResponse weatherResponse) {
        return new Weather(
                weatherResponse.getWeather().get(0).getMain(),
                weatherResponse.getMain().getTemp(),
                weatherResponse.getMain().getHumidity(),
                weatherResponse.getMain().getPressure(),
                weatherResponse.getWeather().get(0).getIcon(),
                weatherResponse.getDt());
    }

    public static RealmList<Weather> getWeatherForecast(ForecastResponse forecastResponse) {
        RealmList<Weather> weathers = new RealmList<>();
        for (WeatherList weatherList:
             forecastResponse.getList()) {
            weathers.add(new Weather(
                    weatherList.getWeather().get(0).getMain(),
                    weatherList.getMain().getTemp(),
                    weatherList.getMain().getHumidity(),
                    weatherList.getMain().getPressure(),
                    weatherList.getWeather().get(0).getIcon(),
                    weatherList.getDt()
            ));
        }
        return weathers;
    }
}
