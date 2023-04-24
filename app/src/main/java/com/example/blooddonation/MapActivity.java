package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.util.ErrorDialogManager;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {


    Double a,b;
    GoogleMap gMap;
    FrameLayout map;
    Intent dataIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        dataIntent = getIntent();
        map = findViewById(R.id.map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                //set position of marker
                markerOptions.position(latLng);
                //set title of marker
                markerOptions.title(latLng.latitude+":"+latLng.longitude);

                //remove all markers
                googleMap.clear();
                //animating to ZOOM the marker
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                //Add marker on MAP
                googleMap.addMarker(markerOptions);

                a = latLng.latitude;
                b = latLng.longitude;


            }
        });


//        LatLng mapIndia = new LatLng(20.5937, 78.9629);
//
//        this.gMap.addMarker(new MarkerOptions().position(mapIndia).title("Marker in India"));
//        this.gMap.moveCamera(CameraUpdateFactory.newLatLng(mapIndia));
    }

    @Override
    public void onBackPressed() {
        Intent mapintent = new Intent(getApplicationContext(),CreateCamp.class);
//        SharedPreferences sp = getSharedPreferences("map", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putFloat("a", a.floatValue());
//        editor.putFloat("b", b.floatValue());
//        editor.commit();

        mapintent.putExtra("id",dataIntent.getStringExtra("id"));
        mapintent.putExtra("name", dataIntent.getStringExtra("name"));
        mapintent.putExtra("address", dataIntent.getStringExtra("address"));
        mapintent.putExtra("pin", dataIntent.getStringExtra("pin"));
        mapintent.putExtra("tag", dataIntent.getStringExtra("tag"));
        mapintent.putExtra("st", dataIntent.getStringExtra("st"));
        mapintent.putExtra("et", dataIntent.getStringExtra("et"));
        mapintent.putExtra("mono", dataIntent.getStringExtra("mono"));
       // mapintent.putExtra("location_str",dataIntent.getStringExtra("location_str"));
        mapintent.putExtra("datePickerob", dataIntent.getStringExtra("datePickerob"));

        mapintent.putExtra("ngoID", dataIntent.getStringExtra("ngoID"));
        mapintent.putExtra("ngoName", dataIntent.getStringExtra("ngoName"));

        mapintent.putExtra("a",a);
        mapintent.putExtra("b",b);
        startActivity(mapintent);
        finish();

    }
}