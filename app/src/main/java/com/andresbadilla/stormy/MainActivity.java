package com.andresbadilla.stormy;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class MainActivity extends Activity {

    private static final String TAG =MainActivity.class.getSimpleName() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String apiKey = "4c7c4d8d89e13f09563ac7fb5d05de9c";
        double latitude = 37.8267;
        double longitude=-122.423;
        String forecastUrl = "https://api.forecast.io/forecast/"+apiKey+"/"+
                                latitude+","+longitude;

        if(isNetworkAvailable()) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        Log.v(TAG, response.body().string());
                        if (response.isSuccessful()) {

                        } else {
                            alertUserAboutError(R.string.error_title,R.string.error_message,R.string.error_ok);
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        }else{
            //Toast.makeText(this,getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
            alertUserAboutError(R.string.unNetwork_title, R.string.unNetwork_message, R.string.unNetwork_button);
        }

        Log.d(TAG, "Main UI code is running");
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
        //AlertDialogFragment dialog = new AlertDialogFragment();

        AlertDialogFragment dialog = AlertDialogFragment.getInstance(title,message,button);
        dialog.show(getFragmentManager(),"error_dialog");
    }


}
