package ru.dmitrybochkov.weatherforecast.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import ru.dmitrybochkov.weatherforecast.R;
import ru.dmitrybochkov.weatherforecast.domain.City;

/**
 * Created by Dmitry Bochkov on 05.06.2017.
 */

//Пользуясь случаем, решил протестировать магический реалм адаптер

public class CitiesListAdapter extends RealmRecyclerViewAdapter<City, CitiesListAdapter.CityViewHolder> {

    private Set<City> cities = new HashSet<>();

    public CitiesListAdapter(OrderedRealmCollection<City> cities) {
        super(cities, true);
        setHasStableIds(true);
    }


    public void setCities(Set<City> cities) {
        this.cities = cities;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_layout, parent, false);
        return new CityViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        City city = getItem(position);
        holder.name.setText(city.getName());
    }

    class CityViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        public TextView name;

        CityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
