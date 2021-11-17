package com.raywenderlich.loginandregister;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.raywenderlich.loginandregister.controller.HomeActivity;
import com.raywenderlich.loginandregister.databinding.ActivityMapsBinding;
import com.raywenderlich.loginandregister.model.MyPlacesResponse;
import com.raywenderlich.loginandregister.model.PlaceRequest;
import com.raywenderlich.loginandregister.repo.NetworkRepo;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private Button buttonLocations;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buttonLocations = findViewById(R.id.buttonGetLocations);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        Intent intent = getIntent();
//        int size = intent.getIntExtra("size",0);
//        PlaceRequest receives = (PlaceRequest) intent.getSerializableExtra("userLocation"+(size-1));
        // Add a marker in Sydney and move the camera

        String userToken = getIntent().getStringExtra("token");

        NetworkRepo.getInstance().getUserLocation("Bearer " + userToken).observe(this, new Observer<MyPlacesResponse>() {
            @Override
            public void onChanged(MyPlacesResponse myPlacesResponse) {
                System.out.println(myPlacesResponse);
                System.out.println(myPlacesResponse.getMessage());

                if (myPlacesResponse.getMessage() == null) {
                    int size = myPlacesResponse.getSuccess().getLocations().size() - 1;
                    LatLng place = new LatLng(myPlacesResponse.getSuccess().getLocations().get(size).getLatitude(),
                            myPlacesResponse.getSuccess().getLocations().get(size).getLongitude());
                    mMap.addMarker(new MarkerOptions().position(place).title("Your Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 10f));
                } else {
                    Toast.makeText(MapsActivity.this, myPlacesResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void buttonLocationsClick(View view) {
        String userToken = getIntent().getStringExtra("token");

        NetworkRepo.getInstance().getUserLocation("Bearer " + userToken).observe(this, new Observer<MyPlacesResponse>() {
            @Override
            public void onChanged(MyPlacesResponse myPlacesResponse) {
                System.out.println(myPlacesResponse);
                System.out.println(myPlacesResponse.getMessage());

                if (myPlacesResponse.getMessage() == null) {

                    for (int i = 0; i < myPlacesResponse.getSuccess().getLocations().size(); i++) {
                        Log.e("Location " + i, String.valueOf(myPlacesResponse.getSuccess().getLocations().get(i).getLatitude()));
                        Log.e("Location " + i, String.valueOf(myPlacesResponse.getSuccess().getLocations().get(i).getLongitude()));
                        Log.e("*****************", "*********************");
                        LatLng place = new LatLng(myPlacesResponse.getSuccess().getLocations().get(i).getLatitude(),
                                myPlacesResponse.getSuccess().getLocations().get(i).getLongitude());
                        mMap.addMarker(new MarkerOptions().position(place).title("Your Locations"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 10f));
                    }
                } else {
                    Toast.makeText(MapsActivity.this, myPlacesResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Intent intent = getIntent();
//        int size = intent.getIntExtra("size",0);
//        System.out.println("Received size: "+size);
//        for(int i =0; i< size; i++){
//            PlaceRequest receives = (PlaceRequest) intent.getSerializableExtra("userLocation"+i);
//            System.out.println(receives.getLatitude()+" "+receives.getLongitude());
//            LatLng place = new LatLng(receives.getLatitude(),  receives.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(place).title("Your Location"));
//            //.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_my_location_24));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
//        }
    }

}