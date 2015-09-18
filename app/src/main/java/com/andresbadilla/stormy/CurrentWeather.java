package com.andresbadilla.stormy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Andres Badilla on 9/17/2015.
 */
public class CurrentWeather {
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipChance;
    private String mSummary;
    private String mTimeZone;

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperature() {
        return (int)Math.round((mTemperature-32)/1.8);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public int getHumidity() {
        return (int)Math.round(mHumidity*100);
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public int getPrecipChance() {
        return (int)Math.round(mPrecipChance*100);
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getFormattedTime(){

        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimeZone));
        return formatter.format(new Date(getTime()*1000));
    }

    public int getIconId(){
        //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night

        int iconId = R.drawable.clear_day;

        if(mIcon.equals("clear-night")) iconId = R.drawable.clear_night;
        else if(mIcon.equals("rain")) iconId = R.drawable.rain;
        else if(mIcon.equals("snow")) iconId = R.drawable.snow;
        else if (mIcon.equals("sleet")) iconId = R.drawable.sleet;
        else if (mIcon.equals("wind")) iconId = R.drawable.wind;
        else if(mIcon.equals("fog")) iconId=R.drawable.fog;
        else if(mIcon.equals("cloudy")) iconId = R.drawable.cloudy;
        else if(mIcon.equals("partly-cloudy")) iconId = R.drawable.partly_cloudy;
        else iconId = R.drawable.cloudy_night;


        return iconId;
    }
}
