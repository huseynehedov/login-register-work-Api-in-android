package com.raywenderlich.loginandregister;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.raywenderlich.loginandregister.databinding.ActivityMapsBinding;
import com.raywenderlich.loginandregister.model.PlaceRequest;

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
        Intent intent = getIntent();
        int size = intent.getIntExtra("size",0);
        PlaceRequest receives = (PlaceRequest) intent.getSerializableExtra("userLocation"+(size-1));
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(receives.getLatitude(), receives.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f));
    }

    public void buttonLocationsClick(View view){
        Intent intent = getIntent();
        int size = intent.getIntExtra("size",0);
        System.out.println("Received size: "+size);
        for(int i =0; i< size; i++){
            PlaceRequest receives = (PlaceRequest) intent.getSerializableExtra("userLocation"+i);
            System.out.println(receives.getLatitude()+" "+receives.getLongitude());
            LatLng place = new LatLng(receives.getLatitude(),  receives.getLongitude());
            mMap.addMarker(new MarkerOptions().position(place).title("Your Location"));
            //.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_my_location_24));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        }
    }

}