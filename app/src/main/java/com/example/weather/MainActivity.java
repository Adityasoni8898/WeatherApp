package com.example.weather;

import androidx.annotation.LongDef;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ImageView iconImage;
    EditText etSearch;
    Button btRun;
    TextView cityName;
    TextView tvCondition;
    TextView tvTemp;
    TextView tvFeelTemp;
    TextView tvCountry;
    TextView tvDate;
    TextView tvHumidity;
    TextView tvUv;
    ImageView ivBackground;

    private LocationManager locationManager;
    private int permissionCode = 1;
    private String city = "mumbai";
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconImage = findViewById(R.id.iv_weatherImage);
        etSearch = findViewById(R.id.et_search);
        btRun = findViewById(R.id.bt_run);
        cityName = findViewById(R.id.tv_city_name);
        tvCondition = findViewById(R.id.tv_condition);
        tvTemp = findViewById(R.id.tv_temp);
        tvFeelTemp = findViewById(R.id.tv_feel_temp);
        tvCountry = findViewById(R.id.tv_country);
        tvDate = findViewById(R.id.tv_date);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvUv = findViewById(R.id.tv_uv);
        ivBackground = findViewById(R.id.iv_background);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, permissionCode);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        //city = getCityName(location.getLongitude(), location.getLatitude());


        btRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city = etSearch.getText().toString().trim();
                city = toTitleCase(city);
                Log.d("mainmain", city);

                new RetrofitInstance().weatherApiServiceImp.getData(city).enqueue(new Callback<WeatherData>() {
                    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
                    @Override
                    public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                        if(response.isSuccessful()) {
                            Log.d("mainmain", "onResponse: " + response.body().getCurrent().getHumidity());
                            cityName.setText(response.body().getLocation().getName());
                            tvCountry.setText(response.body().getLocation().getCountry());
                            tvCondition.setText(response.body().getCurrent().getCondition().getText().toUpperCase(Locale.ROOT));
                            tvTemp.setText(String.valueOf(response.body().getCurrent().getTemp_c()) + "°");
                            tvFeelTemp.setText("feels like " + response.body().getCurrent().getFeelslike_c() + "°");
                            tvDate.setText(toDate(response.body().getLocation().getLocaltime()));
                            tvHumidity.setText(String.valueOf(response.body().getCurrent().getHumidity())+"%");
                            tvUv.setText(String.valueOf(response.body().getCurrent().getUv()));

                            switch (response.body().getCurrent().getCondition().getText()) {
                                case "Moderate or heavy rain shower":
                                    tvCondition.setText("Rain shower");
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.rainy));
                                    ivBackground.setBackgroundResource(R.color.rainy);
                                    break;
                                case "Rainy":
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.rainy));
                                    ivBackground.setBackgroundResource(R.color.rainy);
                                    break;
                                case "Partly cloudy":
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.partially_cloudy));
                                    ivBackground.setBackgroundResource(R.color.partially_cloudy);
                                    break;
                                case "Clear":
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.clear));
                                    ivBackground.setBackgroundResource(R.color.clear);
                                    break;
                                case "Cloudy":
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.cloudy));
                                    ivBackground.setBackgroundResource(R.color.cloudy);
                                    break;
                                case "Sunny":
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.orange));
                                    ivBackground.setBackgroundResource(R.color.orange);
                                    break;
                                case "Mist":
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.mist_blue));
                                    ivBackground.setBackgroundResource(R.color.mist_blue);
                                    break;
                                default:
                                    getWindow().setStatusBarColor(getResources().getColor(R.color.white));
                                    ivBackground.setBackgroundResource(R.color.white);
                            }

                            Glide.with(MainActivity.this)
                                    .load("https:"+response.body().getCurrent().getCondition().getIcon())
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .into(iconImage);
                        } else{
                            Toast.makeText(context, "Wrong city name", Toast.LENGTH_LONG).show();
                            Log.d("mainmain", "Error");
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherData> call, Throwable t) {
                        Log.d("mainmain", "Error");
                        Toast.makeText(context, "Data unavailable", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });



        new RetrofitInstance().weatherApiServiceImp.getData(city).enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if(response.isSuccessful()) {
                    Log.d("mainmain", "onResponse: " + response.body().getCurrent().getHumidity());
                    cityName.setText(response.body().getLocation().getName());
                    tvCountry.setText(response.body().getLocation().getCountry());
                    tvCondition.setText(response.body().getCurrent().getCondition().getText().toUpperCase(Locale.ROOT));
                    tvTemp.setText(String.valueOf(response.body().getCurrent().getTemp_c()) + "°");
                    tvFeelTemp.setText("feels like " + response.body().getCurrent().getFeelslike_c() + "°");
                    tvDate.setText(toDate(response.body().getLocation().getLocaltime()));
                    tvHumidity.setText(String.valueOf(response.body().getCurrent().getHumidity())+"%");
                    tvUv.setText(String.valueOf(response.body().getCurrent().getUv()));

                    switch (response.body().getCurrent().getCondition().getText()) {
                        case "Moderate or heavy rain shower":
                            tvCondition.setText("Rain shower");
                            getWindow().setStatusBarColor(getResources().getColor(R.color.rainy));
                            ivBackground.setBackgroundResource(R.color.rainy);
                            break;
                        case "Rainy":
                            getWindow().setStatusBarColor(getResources().getColor(R.color.rainy));
                            ivBackground.setBackgroundResource(R.color.rainy);
                            break;
                        case "Partly cloudy":
                            getWindow().setStatusBarColor(getResources().getColor(R.color.partially_cloudy));
                            ivBackground.setBackgroundResource(R.color.partially_cloudy);
                            break;
                        case "Clear":
                            getWindow().setStatusBarColor(getResources().getColor(R.color.clear));
                            ivBackground.setBackgroundResource(R.color.clear);
                            break;
                        case "Cloudy":
                            getWindow().setStatusBarColor(getResources().getColor(R.color.cloudy));
                            ivBackground.setBackgroundResource(R.color.cloudy);
                            break;
                        case "Sunny":
                            getWindow().setStatusBarColor(getResources().getColor(R.color.orange));
                            ivBackground.setBackgroundResource(R.color.orange);
                            break;
                        case "Mist":
                            getWindow().setStatusBarColor(getResources().getColor(R.color.mist_blue));
                            ivBackground.setBackgroundResource(R.color.mist_blue);
                            break;
                        default:
                            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
                            ivBackground.setBackgroundResource(R.color.white);
                    }

                    Glide.with(MainActivity.this)
                            .load("https:"+response.body().getCurrent().getCondition().getIcon())
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .into(iconImage);
                } else{
                    Toast.makeText(context, "Wrong city name", Toast.LENGTH_LONG).show();
                    Log.d("mainmain", "Error");
                }
            }
            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Log.d("mainmain", "Error");
                Toast.makeText(context, "Data unavailable", Toast.LENGTH_SHORT).show();
            }
        });

    }



    public String getCityName(double longitude, double latitude) {
        Log.d("mainmaina", "Error");
        String cityName = "Mumbai";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try{
            List<Address> addressList = gcd.getFromLocation(latitude, longitude, 10);
            for(Address address : addressList) {
                if(address != null) {
                    if(address.getLocality() != null && !address.getLocality().equals("")) {
                        cityName = address.getLocality();
                    } else {
                        Toast.makeText(context, "City not found", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

    public static String toTitleCase(String input) {
        input = input.toLowerCase();
        char c =  input.charAt(0);
        String s = new String("" + c);
        String f = s.toUpperCase();
        return f + input.substring(1);
    }

    public static String toDate(String date){
        StringBuilder newDate = new StringBuilder(date);
        String year = newDate.substring(0, 4);
        String month = newDate.substring(5, 7);
        String daate = newDate.substring(8, 10);
        String moonth;
        switch (month) {
            case "01":
                moonth = "January";
                break;
            case "02":
                moonth = "Feburay";
                break;
            case "03":
                moonth = "March";
                break;
            case "04":
                moonth = "April";
                break;
            case "05":
                moonth = "May";
                break;
            case "06":
                moonth = "June";
                break;
            case "07":
                moonth = "July";
                break;
            case "08":
                moonth = "August";
                break;

            case "09":
                moonth = "September";
                break;
            case "10":
                moonth = "October";
                break;
            case "11":
                moonth = "November";
                break;
            case "12":
                moonth = "December";
                break;

            default:
                moonth = month;
                break;
        }
        return(daate + " " + moonth + " " + year);
    }

}