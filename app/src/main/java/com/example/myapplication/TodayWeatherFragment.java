package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Common.Common;
import com.example.myapplication.Modal.WeatherResponse;
import com.example.myapplication.Retrofit.OpenWeatherMapAPI;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment {


    private TextView cityView;
    private ImageView imageView;
    private TextView tempView;
    private TextView descView;
    private TextView dateTimeView;
    private TextView windView;
    private TextView pressureView;
    private TextView humidityView;
    private TextView sunRiseView;
    private TextView sunSetView;
    private TextView coordView;


    private TextView feelTempView;
    private TextView maxTempView;
    private TextView minTempView;

    private LinearLayout weatherPanelView;

    private ProgressBar progressBar;
    CompositeDisposable compositeDisposable;
    OpenWeatherMapAPI weatherMapAPI;


    static TodayWeatherFragment instance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today_weather, container, false);

        cityView = view.findViewById(R.id.txtCityId);
        imageView = view.findViewById(R.id.image);
        tempView = view.findViewById(R.id.Temprature);
        descView = view.findViewById(R.id.Description);
        dateTimeView = view.findViewById(R.id.DateTime);
        windView = view.findViewById(R.id.wind);
        pressureView = view.findViewById(R.id.pressure);
        humidityView = view.findViewById(R.id.humidity);
        sunRiseView = view.findViewById(R.id.sunRise);
        sunSetView = view.findViewById(R.id.sunSet);
        coordView = view.findViewById(R.id.cord);
        feelTempView = view.findViewById(R.id.feelTemp);
        maxTempView = view.findViewById(R.id.maxTemp);
        minTempView = view.findViewById(R.id.minTemp);

        weatherPanelView = view.findViewById(R.id.weatherPanel);
        progressBar = view.findViewById(R.id.progress_circular);

        getWeatherInfo();
        return view;
    }

    private void getWeatherInfo() {

        compositeDisposable.add(weatherMapAPI.getWeather(
                (int) Common.current_location.getLatitude(),
                (int) Common.current_location.getLongitude(),
                Common.App_ID,
                "metric").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResponse>() {
                    @Override
                    public void accept(WeatherResponse weatherResponse) throws Exception {
                        Picasso.get().
                                load(new StringBuilder("https://openweathermap.org/img/wn/")
                                        .append(weatherResponse.getWeather().get(0).getIcon())
                                        .append(".png").toString())
                                .into(imageView);


                        cityView.setText(String.valueOf(weatherResponse.getSys().getCountry()));

                        tempView.setText(new StringBuilder(String.valueOf(weatherResponse.getMain().getTemp())).append("°C"));
                        feelTempView.setText(new StringBuilder(String.valueOf(weatherResponse.getMain().getFeels_like())).append("°C"));
                        minTempView.setText(new StringBuilder(String.valueOf(weatherResponse.getMain().getTemp_min())).append("°C"));
                        maxTempView.setText(new StringBuilder(String.valueOf(weatherResponse.getMain().getTemp_max())).append("°C"));

                        descView.setText(new StringBuilder("Weather in ").append(weatherResponse.getSys().getCountry()).append(" is ").append(weatherResponse.getWeather().get(0).getMain()));

                        dateTimeView.setText(Common.convertUnixTime(weatherResponse.getDt()));

                        windView.setText(new StringBuilder("Speed: ").append(weatherResponse.getWind().getSpeed()).append(" deg: ").append(weatherResponse.getWind().getDeg()));

                        pressureView.setText(new StringBuilder(String.valueOf(weatherResponse.getMain().getPressure())).append(" hPa") );

                        humidityView.setText(new StringBuilder(String.valueOf(weatherResponse.getMain().getHumidity())).append(" %"));

                        sunRiseView.setText(Common.convertUnixTime(weatherResponse.getSys().getSunrise()));

                        sunSetView.setText(Common.convertUnixTime(weatherResponse.getSys().getSunset()));

                        coordView.setText(new StringBuilder("[").append(weatherResponse.getCoord().getLat()).append(" , ").append(weatherResponse.getCoord().getLon()).append("]"));

                        progressBar.setVisibility(View.GONE);
                        weatherPanelView.setVisibility(View.VISIBLE);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getContext(), "error " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }


    public static TodayWeatherFragment getInstance() {
        if (instance == null) {
            instance = new TodayWeatherFragment();
        }
        return instance;
    }


    public TodayWeatherFragment() {
        // Required empty public constructor

        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = com.example.myapplication.Retrofit.Retrofit.getInstance();

        weatherMapAPI = retrofit.create(OpenWeatherMapAPI.class);
    }


}
