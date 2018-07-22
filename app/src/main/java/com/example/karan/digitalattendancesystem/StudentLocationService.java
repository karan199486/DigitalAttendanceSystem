package com.example.karan.digitalattendancesystem;

/*
this class is made to get the location of student when student request for attendance
 */

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class StudentLocationService extends Service{


    Context context;
    private LocationListener locationListener;
    private LocationManager locationManager;

    public StudentLocationService(){}


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        getLocation();

        return START_STICKY;
    }

    @SuppressLint("MissingPermission")
    private void getLocation()
    {

        locationManager = (LocationManager)this.getSystemService(StudentLocationService.LOCATION_SERVICE);
        locationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                /*Alerter.create(RequestLocationUpdatesActivity.this)
                        .setText("Longitude : " + lastUpdatedLocation.getLongitude() + "\t" + "Latitude : " + lastUpdatedLocation.getLatitude())
                        .enableIconPulse(true)
                        .setBackgroundColorRes(R.color.colorAccent)
                        .setDuration(2000)
                        .enableProgress(true)
                        .show();*/
                Intent intent = new Intent("receive_location_update");
                intent.putExtra("longitude", location.getLongitude());
                intent.putExtra("latitude", location.getLatitude());
                sendBroadcast(intent);
                stopSelf();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider)
            {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);
                Toast.makeText(getApplicationContext(),"please enable the gps in location setting",Toast.LENGTH_SHORT).show();
            }

        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }
}
