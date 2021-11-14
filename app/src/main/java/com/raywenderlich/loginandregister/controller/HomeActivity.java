package com.raywenderlich.loginandregister.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.raywenderlich.loginandregister.MapsActivity;
import com.raywenderlich.loginandregister.R;
import com.raywenderlich.loginandregister.model.MyPlacesResponse;
import com.raywenderlich.loginandregister.model.PlaceResponse;
import com.raywenderlich.loginandregister.model.PlaceRequest;
import com.raywenderlich.loginandregister.repo.NetworkRepo;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LocationRequest locationRequest;
    String getMyLatitude;
    String getMyLongitude ;
    private double latitude;
    private double longitude;
    private String userToken;
    private WebView webViewSoft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        webViewSoft = (WebView) findViewById(R.id.webViewSoft);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("UsersInfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        webViewSoft.setWebViewClient(new WebViewClient());
        webViewSoft.getSettings().setJavaScriptEnabled(true);
        webViewSoft.loadUrl("https://softsolution.az/");

        userToken = sharedPreferences.getString("user", null);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

    }

    @Override
    public void onBackPressed() {
        if(webViewSoft.canGoBack()){
            webViewSoft.goBack();
        }else{
            super.onBackPressed();
        }
    }

    public void buttonExitClick(View view){
        editor.remove("user");
        editor.commit();
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isGpsEnabled()) {

                    getCurrentLocation();

                } else {

                    turnOnGps();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_toolbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_userLocations) {

            NetworkRepo.getInstance().getUserLocation("Bearer "+userToken).observe(this, new Observer<MyPlacesResponse>() {
                @Override
                public void onChanged(MyPlacesResponse myPlacesResponse) {
                    System.out.println(myPlacesResponse);
                    System.out.println(myPlacesResponse.getMessage());

                    if(myPlacesResponse.getMessage() == null){
                        Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
                        intent.putExtra("size",myPlacesResponse.getSuccess().getLocations().size());
                        System.out.println("Size is " + myPlacesResponse.getSuccess().getLocations().size());
                        for(int i=0; i< myPlacesResponse.getSuccess().getLocations().size(); i++){
                            Log.e("Location "+i, String.valueOf(myPlacesResponse.getSuccess().getLocations().get(i).getLatitude()));
                            Log.e("Location "+i, String.valueOf(myPlacesResponse.getSuccess().getLocations().get(i).getLongitude()));
                            Log.e("*****************", "*********************");
                            PlaceRequest request = new PlaceRequest(myPlacesResponse.getSuccess().getLocations().get(i).getLatitude(),
                                    myPlacesResponse.getSuccess().getLocations().get(i).getLongitude());
                            intent.putExtra("userLocation"+i, request);
                        }
                        startActivity(intent);
                    }else{
                        Toast.makeText(HomeActivity.this, myPlacesResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else if (item.getItemId() == R.id.action_add_location) {
            getCurrentLocation();
            PlaceRequest request = new PlaceRequest(latitude, longitude);
            NetworkRepo.getInstance().addLocation("Bearer "+userToken, request).observe(this, new Observer<PlaceResponse>() {
                @Override
                public void onChanged(PlaceResponse placeResponse) {
                    System.out.println(placeResponse);
                    System.out.println(placeResponse.getError());
                    if(placeResponse.getError() == null){
                        Toast.makeText(HomeActivity.this, "Added Location", Toast.LENGTH_SHORT).show();
                        System.out.println(placeResponse.getSuccess().getMessage());
                    }else{
                        StringBuilder sb = new StringBuilder();

                        if(placeResponse.getError().getLatitude() != null){
                            for(String s: placeResponse.getError().getLatitude()){
                                sb.append(s + '\n');
                            }
                        }
                        if(placeResponse.getError().getLongitude() != null){
                            for(String s: placeResponse.getError().getLongitude()){
                                sb.append(s + '\n');
                            }
                        }
                        Toast.makeText(HomeActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCurrentLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGpsEnabled()) {

                    LocationServices.getFusedLocationProviderClient(HomeActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(HomeActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        latitude = locationResult.getLocations().get(index).getLatitude();
                                        longitude = locationResult.getLocations().get(index).getLongitude();
                                        getMyLatitude = String.valueOf(latitude);
                                        getMyLongitude = String.valueOf(longitude);
                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGps();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGps() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(HomeActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(HomeActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGpsEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return isEnabled;
    }

}