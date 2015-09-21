package com.andresbadilla.stormy.weather;

import com.andresbadilla.stormy.R;

/**
 * Created by Andres Badilla on 9/18/2015.
 */
public class Forecast {

    private Current mCurrent;
    private Hour[] mHourlyForecast;
    private Day[] mDailyForecast;

    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public Hour[] getHourlyForecast() {
        return mHourlyForecast;
    }

    public void setHourlyForecast(Hour[] hourlyForecast) {
        mHourlyForecast = hourlyForecast;
    }

    public Day[] getDailyForecast() {
        return mDailyForecast;
    }

    public void setDailyForecast(Day[] dailyForecast) {
        mDailyForecast = dailyForecast;
    }

    public static int getIconId(String iconString){
        //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night

        int iconId = R.drawable.clear_day;

        if(iconString.equals("clear-night")) iconId = R.drawable.clear_night;
        else if(iconString.equals("rain")) iconId = R.drawable.rain;
        else if(iconString.equals("snow")) iconId = R.drawable.snow;
        else if (iconString.equals("sleet")) iconId = R.drawable.sleet;
        else if (iconString.equals("wind")) iconId = R.drawable.wind;
        else if(iconString.equals("fog")) iconId=R.drawable.fog;
        else if(iconString.equals("cloudy")) iconId = R.drawable.cloudy;
        else if(iconString.equals("partly-cloudy")) iconId = R.drawable.partly_cloudy;
        else iconId = R.drawable.cloudy_night;


        return iconId;
    }
}
