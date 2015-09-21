package com.andresbadilla.stormy.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andres Badilla on 9/18/2015.
 */
public class Hour implements Parcelable {

    private long mTime;
    private String mSummary;
    private double mTemperature;
    private String mIcon;
    private String mTimeZone;

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public int getTemperature() {
        return (int) Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public String getIcon() {
        return mIcon;
    }

    public int getIconId(){
        return Forecast.getIconId(mIcon);
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getHour(){
        SimpleDateFormat formatter = new SimpleDateFormat("h a");
        Date date = new Date(mTime*1000);
        return formatter.format(date);
    }

    @Override
    public int describeContents() {
        return 0; //Not in use

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mTime);
        parcel.writeString(mSummary);
        parcel.writeDouble(mTemperature);
        parcel.writeString(mIcon);
        parcel.writeString(mTimeZone);
    }

    private Hour(Parcel in){
        mTime=in.readLong();
        mSummary=in.readString();
        mTemperature = in.readDouble();
        mIcon=in.readString();
        mTimeZone=in.readString();

    }

    public Hour(){}

    public static final Creator<Hour> CREATOR = new Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel parcel) {
            return new Hour(parcel);
        }

        @Override
        public Hour[] newArray(int i) {
            return new Hour[i];
        }
    };
}
