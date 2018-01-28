package com.example.danieljackson.weatherapp.ui.cities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
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

import org.w3c.dom.Text;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class CitiesFragment extends Fragment {

    private static final String TAG = CitiesFragment.class.getSimpleName();

    @BindView(R.id.list)
    RecyclerView recyclerView;

    @BindView(R.id.add_button)
    FloatingActionButton addCityButton;

    @Inject
    CitiesPresenter citiesPresenter;

    @Inject
    SystemMessaging systemMessaging;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeatherApplication.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addCityButton.setOnClickListener(v -> launchSearchDialog());

//        recyclerView.setAdapter();
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void launchSearchDialog() {
        systemMessaging.d(TAG, "Launching city search dialog...");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.search_title);
        builder.setMessage(R.string.enter_zip);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.zip_search_layout, null);
        builder.setView(dialogView);


        TextInputLayout searchCityLayout = (TextInputLayout) dialogView.findViewById(R.id.searchCityZipLayout);
        EditText zipEntryText = searchCityLayout.getEditText();
        ProgressBar progressBar = (ProgressBar) dialogView.findViewById(R.id.search_progress);

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
                        }

                        @Override
                        public void onSuccess(City city) {
                            systemMessaging.d(TAG, city.getCityName());
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            systemMessaging.e(TAG, e.getMessage());
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
}

