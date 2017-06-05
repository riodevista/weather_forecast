package ru.dmitrybochkov.weatherforecast.data.api.v1;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.dmitrybochkov.weatherforecast.data.api.v1.pojo.ForecastResponse;
import ru.dmitrybochkov.weatherforecast.data.api.v1.pojo.WeatherResponse;


public interface OpenWeatherAPI {

    @GET("weather")
    Call<WeatherResponse> getCurrentWeather(@Query("q") String cityName);
    @GET("forecast")
    Call<ForecastResponse> getWeatherForecast(@Query("q") String cityName);


}