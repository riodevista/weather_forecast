package ru.dmitrybochkov.weatherforecast;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.dmitrybochkov.weatherforecast.domain.City;


/**
 * Created by Dmitry Bochkov on 26.12.2016.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createObject(City.class, "Moscow");
                        realm.createObject(City.class, "Saint Petersburg");
                    }})
                .build();
//        Realm.deleteRealm(realmConfig);
        Realm.setDefaultConfiguration(realmConfig);
//        getSharedPreferences(AppConstants.PREFERENCES, MODE_PRIVATE).edit().putBoolean(AppConstants.SHOW_WEATHER_FORECAST, false).apply();

    }


}
