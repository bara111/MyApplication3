package com.example.myapplication;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.WeatherApi.Weather;
import com.example.myapplication.WeatherApi.WeatherMain;
import com.example.myapplication.WeatherApi.WeatherResponse;
import com.example.myapplication.WeatherApi.WeatherService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private TextView textView, textView1;
    private ProgressBar progressBar;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        textView = findViewById(R.id.longLAT);
        textView1 = findViewById(R.id.city);
        progressBar= findViewById(R.id.progress_circular);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    private void FetchWeather(int lat,int lon) {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        textView1.setVisibility(View.GONE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);

        service.get("829740af696dc0258609d2d0a6a8472a", lat + "", lon + "")
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        String temp=response.body().getmMain().getmTemp().toString();
                        textView.setVisibility(View.VISIBLE);
                        textView1.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                        textView1.setText( temp);
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {

                    }
                });

    }






    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                int Lat = (int) latLng.latitude;
                int Long = (int) latLng.longitude;
                FetchWeather(Lat,Long);
                mMap.clear();
                String LongLat = "" + Lat + "," + Long;
                textView.setText(LongLat);

                LatLng sydney = new LatLng(Lat, Long);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(sydney));
/*
               find_weather(Lat,Long);
*/
            }
        });
        // Add a marker in Sydney and move the camera

    }


   /* public void find_weather(int lat, int lon) {
        String url = "https://samples.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=829740af696dc0258609d2d0a6a8472a";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    String temp = String.valueOf(main_object.getDouble("temp"));
                    //String country = response.getString("country");

                    double temp_int = Double.parseDouble(temp);
                    double centi = (temp_int - 32) / 1.8000;
                    centi = Math.round(centi);
                    int i = (int) centi;
                    String Temp = "" + i;
                    //textView1.setText(country);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", null);
            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);


    }*/
}
