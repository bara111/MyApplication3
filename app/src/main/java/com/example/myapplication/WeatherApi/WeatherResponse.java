package com.example.myapplication.WeatherApi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {
    @SerializedName("weather")
    List<Weather> mWeather;

    public List<Weather> getmWeather() {
        return mWeather;
    }


    public void setmWeather(List<Weather> mWeather) {
        this.mWeather = mWeather;
    }

    public WeatherMain getmMain() {
        return mMain;
    }

    public void setmMain(WeatherMain mMain) {
        this.mMain = mMain;
    }

    @SerializedName("main")
    WeatherMain mMain;
}
