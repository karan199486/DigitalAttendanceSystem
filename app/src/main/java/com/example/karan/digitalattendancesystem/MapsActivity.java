package com.example.karan.digitalattendancesystem;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Double latitude,longitude;
    String rollno;

    List<StudentDetails> studentDetailslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String longi = getIntent().getStringExtra("longitude");
        String lati = getIntent().getStringExtra("latitude");
        if(lati!= null)
        latitude = Double.parseDouble(lati);
        if(longi!= null)
        longitude = Double.parseDouble(longi);
        rollno = getIntent().getStringExtra("rollno");
        studentDetailslist = (ArrayList<StudentDetails>)getIntent().getSerializableExtra("list_of_students");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       if(studentDetailslist==null){
           LatLng studlocation = new LatLng(latitude,longitude);
           mMap.addMarker(new MarkerOptions().position(studlocation).title(rollno));
           mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(studlocation,15));
       }
       else
       {
           for(StudentDetails details : studentDetailslist){
              Double longitude = Double.parseDouble(details.getLongitude());
              Double latitude = Double.parseDouble(details.getLatitude());
               String rollno = details.getRollno();
               LatLng studlocation = new LatLng(latitude,longitude);
               mMap.addMarker(new MarkerOptions().position(studlocation).title(rollno));
               mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(studlocation,15));

           }
       }

    }




}
