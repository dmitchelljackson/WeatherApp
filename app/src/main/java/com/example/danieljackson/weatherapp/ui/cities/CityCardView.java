package com.example.danieljackson.weatherapp.ui.cities;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danieljackson.weatherapp.R;
import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;
import com.example.danieljackson.weatherapp.util.MeasurementConversionUtil;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityCardView extends CardView {

    @BindView(R.id.shimmer_view)
    ShimmerFrameLayout shimmerFrameLayout;

    @BindView(R.id.card_city_name_line)
    TextView cityTextView;

    @BindView(R.id.card_temp_line)
    TextView tempTextView;

    @BindView(R.id.card_description_line)
    TextView descriptionTextView;

    @BindView(R.id.weather_image)
    ImageView imageView;

    private City city;

    public CityCardView(Context context) {
        super(context);
        init();
    }

    public CityCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CityCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addGlobalLayoutListener(() -> ButterKnife.bind(CityCardView.this));
        setOnClickListener(v -> Toast.makeText(getContext(), R.string.feature_coming_soon, Toast.LENGTH_LONG).show());
    }

    public void setCity(City city) {
        addGlobalLayoutListener(() -> {
            this.city = city;

            if (city.getDescription() != null && !city.getDescription().isEmpty()) {
                shimmerFrameLayout.stopShimmerAnimation();
            } else {
                shimmerFrameLayout.startShimmerAnimation();
            }

            setTextViews(city);
            Picasso.with(getContext()).load(city.getIconUrl()).fit().into(imageView);
        });
    }

    private void setTextViews(City city) {
        cityTextView.setText(getContext().getString(R.string.city_name_format, city.getCityName(), city.getZipCode()));
        if(city.getDescription() != null) {
            descriptionTextView.setText(capitalizeFirstLetter(city.getDescription()));
        } else {
            descriptionTextView.setText("");
        }
        tempTextView.setText(buildTempString(city));
    }

    private String buildTempString(City city) {
        if(city.getCurrentTemp() != null && city.getWindSpeed() != null && city.getScale() != null && city.getWindDirection() != null) {
            return getContext().getString(R.string.temp_cardview_string,
                    MeasurementConversionUtil.convertFromKelvin(city.getScale(), city.getCurrentTemp()).intValue(),
                    city.getWindDirection().toString(),
                    MeasurementConversionUtil.convertSpeed(city.getScale(), city.getWindSpeed()).intValue(),
                    city.getScale().getSpeedName());
        } else {
            return "";
        }
    }

    private String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    private void addGlobalLayoutListener(Runnable runnable) {
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                CityCardView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                runnable.run();
            }
        });
    }
}
