package com.andresbadilla.stormy.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andresbadilla.stormy.R;
import com.andresbadilla.stormy.weather.Current;
import com.andresbadilla.stormy.weather.Day;
import com.andresbadilla.stormy.weather.Forecast;
import com.andresbadilla.stormy.weather.Hour;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends Activity {

    private static final String TAG =MainActivity.class.getSimpleName();
    private Forecast mForecast;

    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";

    @Bind(R.id.timeLabel)
    TextView mTimeLabel;
    @Bind(R.id.temperatureLabel)
    TextView mTemperatureLabel;
    @Bind(R.id.humidityValue)
    TextView mHumidityValue;
    @Bind(R.id.precipValue)
    TextView mPrecipValue;
    @Bind(R.id.summaryLabel)
    TextView mSummaryLabel;
    @Bind(R.id.iconImageView)
    ImageView mIconImageView;
    @Bind(R.id.refreshImageView)
    ImageView mRefreshImageView;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final double latitude = 9.9894;
        final double longitude=-84.1082;

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getForecast(latitude,longitude);
            }
        });

        getForecast(latitude,longitude);

        Log.d(TAG, "Main ui code is running");
    }

    private void getForecast(double latitude, double longitude) {
        String apiKey = "4c7c4d8d89e13f09563ac7fb5d05de9c";
        String forecastUrl = "https://api.forecast.io/forecast/"+apiKey+"/"+
                                latitude+","+longitude;

        if(isNetworkAvailable()) {
            toggleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    alertUserAboutError(R.string.error_title,R.string.error_message,R.string.error_ok);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    try {
                        String jsonData =response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError(R.string.error_title,R.string.error_message,R.string.error_ok);
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        }else{
            //Toast.makeText(this,getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
            alertUserAboutError(R.string.unNetwork_title, R.string.unNetwork_message, R.string.unNetwork_button);
        }
    }

    private void toggleRefresh() {
       if(mProgressBar.getVisibility()==View.INVISIBLE){
           mProgressBar.setVisibility(View.VISIBLE);
           mRefreshImageView.setVisibility(View.INVISIBLE);
       }else{
           mProgressBar.setVisibility(View.INVISIBLE);
           mRefreshImageView.setVisibility(View.VISIBLE);
       }
    }

    private void updateDisplay() {
        mTemperatureLabel.setText(mForecast.getCurrent().getTemperature()+"");
        mTimeLabel.setText("At "+ mForecast.getCurrent().getFormattedTime()+ " it will be");
        mHumidityValue.setText(mForecast.getCurrent().getHumidity()+"%");
        mPrecipValue.setText(mForecast.getCurrent().getPrecipChance()+ "%");
        mSummaryLabel.setText(mForecast.getCurrent().getSummary());
        mIconImageView.setImageDrawable(getResources().getDrawable(mForecast.getCurrent().getIconId()));
    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException{
        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));

        return forecast;
    }

    private Day[] getDailyForecast(String jsonData) throws JSONException{

        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];

        for (int i = 0; i<data.length(); i++ ){
            JSONObject jsonDay = data.getJSONObject(i);
            Day day = new Day();

            day.setSummary(jsonDay.getString("summary"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setTime(jsonDay.getLong("time"));
            day.setTimeZone(timezone);

            days[i] = day;
        }
        return days;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException{
       JSONObject forecast = new JSONObject(jsonData);
       String timezone = forecast.getString("timezone");
       JSONObject hourly = forecast.getJSONObject("hourly");
       JSONArray data = hourly.getJSONArray("data");

       Hour[] hours = new Hour[data.length()];

       for (int i = 0; i<data.length(); i++ ){
           JSONObject jsonHour = data.getJSONObject(i);
           Hour hour = new Hour();

           hour.setSummary(jsonHour.getString("summary"));
           hour.setTemperature(jsonHour.getDouble("temperature"));
           hour.setIcon(jsonHour.getString("icon"));
           hour.setTime(jsonHour.getLong("time"));
           hour.setTimeZone(timezone);

           hours[i] = hour;
       }


        return hours;
    }


    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG,"From Current: "+timezone);

        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimeZone(timezone);

        Log.d(TAG, current.getFormattedTime());

        return current;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;

        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }

        
        return isAvailable;
    }

    private void alertUserAboutError(int title, int message, int button) {
        AlertDialogFragment dialog = AlertDialogFragment.getInstance(title,message,button);
        dialog.show(getFragmentManager(),"error_dialog");
    }

    @OnClick (R.id.dailyButton)
    public void startDailyActivity(View view){
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST,mForecast.getDailyForecast());
        startActivity(intent);

    }

    @OnClick(R.id.hourlyButton)
    public void startHourlyActivity(View view){
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putExtra(HOURLY_FORECAST, mForecast.getHourlyForecast());
        startActivity(intent);
    }
}
