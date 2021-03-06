package com.example.danieljackson.weatherapp.ui.cities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.danieljackson.weatherapp.R;
import com.example.danieljackson.weatherapp.WeatherApplication;
import com.example.danieljackson.weatherapp.data.strings.SystemMessaging;
import com.example.danieljackson.weatherapp.ui.cities.presenter.CitiesPresenter;
import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.DisposableSubscriber;

public class CitiesFragment extends Fragment {

    private static final String TAG = CitiesFragment.class.getSimpleName();

    @BindView(R.id.list)
    RecyclerView recyclerView;

    @BindView(R.id.add_button)
    FloatingActionButton addCityButton;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    CitiesPresenter citiesPresenter;

    @Inject
    SystemMessaging systemMessaging;

    private CitiesAdapter citiesAdapter;

    private CompositeDisposable compositeDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeatherApplication.getAppComponent().inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        compositeDisposable = new CompositeDisposable();

        addCityButton.setOnClickListener(v -> launchSearchDialog());

        DisposableSubscriber disposableSubscriber = new DisposableSubscriber<SortedSet<City>>() {
            @Override
            public void onNext(SortedSet<City> cities) {
                Log.d(TAG, "New cities received");
                swipeRefreshLayout.setRefreshing(false);
                if (citiesAdapter == null) {
                    citiesAdapter = new CitiesAdapter(cities);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(citiesAdapter);
                } else {
                    citiesAdapter.updateCities(cities);
                }
            }

            @Override
            public void onError(Throwable t) {
                systemMessaging.e(TAG, "", t);
            }

            @Override
            public void onComplete() {
                systemMessaging.e(TAG, "City update stream complete.  This shouldn't happen.");
            }
        };
        compositeDisposable.add(disposableSubscriber);

        citiesPresenter.getCityUpdateStream().observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableSubscriber);

        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.accent, R.color.primary_light);
        swipeRefreshLayout.setOnRefreshListener(() -> citiesPresenter.refreshCities());

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (citiesAdapter != null) {
                    for (City city : citiesAdapter.getCities()) {
                        if (citiesAdapter.getIdForCity(city) == viewHolder.getItemId()) {
                            citiesPresenter.deleteCity(city);
                            citiesAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
                Log.e(TAG, "No city found for " + viewHolder.getItemId());
            }
        });

        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    private void launchSearchDialog() {
        systemMessaging.d(TAG, "Launching city search dialog...");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.search_title);
        builder.setMessage(R.string.enter_zip);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.zip_search_layout, null);
        builder.setView(dialogView);


        TextInputLayout searchCityLayout = dialogView.findViewById(R.id.searchCityZipLayout);
        EditText zipEntryText = searchCityLayout.getEditText();
        ProgressBar progressBar = dialogView.findViewById(R.id.search_progress);
        CityCardView cityCardView = dialogView.findViewById(R.id.search_card_view);

        searchCityLayout.setHintEnabled(true);
        searchCityLayout.setHint("Five digit zip, e.g. 37215");

        builder.setPositiveButton("Find", (dialog, which) -> {
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });

        Dialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {

            Button positive = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(view -> {
                if (zipEntryText.getText().length() < 5) {
                    searchCityLayout.setError("Please enter a 5 digit zip.");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    searchCityLayout.setVisibility(View.GONE);
                    positive.setEnabled(false);

                    Single<City> cityResult = citiesPresenter.searchForCityByZip(zipEntryText.getText().toString());
                    cityResult.observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<City>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            systemMessaging.d(TAG, "Subscribed");
                            addDisposable(d);
                        }

                        @Override
                        public void onSuccess(City city) {
                            systemMessaging.d(TAG, city.getCityName());

                            progressBar.setVisibility(View.GONE);

                            cityCardView.setCity(city);
                            cityCardView.setVisibility(View.VISIBLE);

                            positive.setText(R.string.add);
                            positive.setEnabled(true);

                            positive.setOnClickListener(view -> {
                                citiesPresenter.addCity(city);
                                dialog.dismiss();
                            });

                        }

                        @Override
                        public void onError(Throwable e) {
                            systemMessaging.e(TAG, "Error in search result", e);
                            progressBar.setVisibility(View.GONE);
                            searchCityLayout.setVisibility(View.VISIBLE);
                            if (e instanceof HttpException && ((HttpException) e).code() == 404) {
                                searchCityLayout.setError(getString(R.string.city_not_found));
                            } else {
                                searchCityLayout.setError(getString(R.string.generic_server_issue));
                            }
                            positive.setEnabled(true);
                        }
                    });
                }
            });

            Button negativeButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(v -> dialog.dismiss());
        });

        dialog.show();
    }


    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }
}

