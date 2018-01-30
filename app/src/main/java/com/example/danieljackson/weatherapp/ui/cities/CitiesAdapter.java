package com.example.danieljackson.weatherapp.ui.cities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.danieljackson.weatherapp.R;
import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {

    private List<City> cities;

    public CitiesAdapter(SortedSet<City> cities) {
        setCities(cities);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.city_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cityCardView.setCity(cities.get(position));
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    @Override
    public long getItemId(int position) {
        return cities.get(position).getCityId();
    }

    public void updateCities(SortedSet<City> cities) {
        setCities(cities);
        notifyDataSetChanged();
    }

    private void setCities(SortedSet<City> cities) {
        this.cities = new ArrayList<>();
        this.cities.addAll(cities);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CityCardView cityCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cityCardView = (CityCardView) itemView;
        }
    }
}


