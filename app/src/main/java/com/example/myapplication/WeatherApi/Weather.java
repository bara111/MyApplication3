package com.example.myapplication.WeatherApi;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("main")
    String mMain;


    public String getmMain() {
        return mMain;
    }

    public void setmMain(String mMain) {
        this.mMain = mMain;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    @SerializedName("description")
    String mDescription;
}
