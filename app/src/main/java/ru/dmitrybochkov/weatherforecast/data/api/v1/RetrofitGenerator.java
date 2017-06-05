package ru.dmitrybochkov.weatherforecast.data.api.v1;


import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class RetrofitGenerator {

    private static final String API_BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "665259ea17fbe4ff85e17ae624176915";

    private static OpenWeatherAPI openWeatherAPI;


    private static OpenWeatherAPI createService() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("appid", API_KEY).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
            }).build();

        Retrofit retrofit =
                new Retrofit.Builder()
                        .client(okHttpClient)
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(OpenWeatherAPI.class);
    }


    public static OpenWeatherAPI getOpenWeatherAPI() {
        if (openWeatherAPI == null)
            openWeatherAPI = createService();
        return openWeatherAPI;
    }

}
