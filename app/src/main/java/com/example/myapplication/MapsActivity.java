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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
    private TextView textView, textView1, textView2, textView3;
    private ProgressBar progressBar;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        textView = findViewById(R.id.longLAT);
        textView1 = findViewById(R.id.city);
        textView2 = findViewById(R.id.city1);
        textView3 = findViewById(R.id.condition);
        progressBar = findViewById(R.id.progress_circular);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void FetchWeather(final double lat, final double lon) {

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
                        String temp = response.body().getmMain().getmTemp().toString();
                        String city = response.body().getCountry();
                        String condition = response.body().getmWeather().get(0).getmDescription();
                        Double tempInt = Double.parseDouble(temp);
                        tempInt = tempInt - 273;

                        String Cel = "" + tempInt.intValue();
                        textView.setVisibility(View.VISIBLE);
                        textView1.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                        textView1.setText(Cel);
                        textView3.setText(condition);
                        if (!city.equals("")) {
                            textView2.setText(city);

                        } else {
                            textView2.setText("Unknown");
                        }

                        LatLng sydney = new LatLng(lat, lon);
                        MarkerOptions marker = new MarkerOptions().position(sydney).title(condition);

                        if (condition.equals("clear sky")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.clear_sky));

                        } else if (condition.equals("few clouds")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.few_clouds));

                        } else if (condition.equals("scattered clouds")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.scattered_clouds));

                        } else if (condition.equals("broken clouds")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.broken_clouds));

                        } else if (condition.equals("shower rain")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.shower_rain));

                        } else if (condition.equals("rain")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.rain));

                        } else if (condition.equals("thunderstorm")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.thunderstorm));

                        } else if (condition.equals("snow")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.snow));

                        } else if (condition.equals("mist")) {
                            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mist));

                        }
                        mMap.addMarker(marker);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(sydney));
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
                FetchWeather(latLng.latitude, latLng.longitude);
                mMap.clear();
                String LongLat = "" + Lat + "," + Long;
                textView.setText(LongLat);


            }
        });

    }


}
