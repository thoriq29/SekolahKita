package com.example.thoriq.sekolahkita.ProfilSekolah;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.thoriq.sekolahkita.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;

public class Lokasi extends AppCompatActivity implements OnMapReadyCallback {


    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Boolean PermissionGranted = false;
    private static final float DEFAULT_ZOOM = 15f;
    double l;
    double ll;
    String lat;
    String lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasi);
        getLocationPermission();
        lat = getIntent().getStringExtra("lat");
        lng= getIntent().getStringExtra("lng");
    }

    private void moveCamera(LatLng latlang, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlang, zoom));
    }

    private void getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (PermissionGranted) {
                com.google.android.gms.tasks.Task<Location> location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                        if (task.isSuccessful()) {
                            Location c = (Location) task.getResult();
                            moveCamera(new LatLng(c.getLatitude(), c.getLongitude()),
                                    DEFAULT_ZOOM);
                        } else {
                            Toast.makeText(getApplicationContext(), "Tidak dapat mengakses lokasi anda!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {

        }
    }

    private void getLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(Lokasi.this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            PermissionGranted = true;
            init();
        } else {
            ActivityCompat.requestPermissions(Lokasi.this, permission, 1234);

        }
    }

    public void init() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }
    public void show()
    {

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng target = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
        mMap.addMarker(new MarkerOptions().position(target).title("Marker in "+lat));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(target));
        Float a=  mMap.getMaxZoomLevel();
        Float b = 17f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target,b));

        //if (PermissionGranted) {
          //  getDeviceLocation();
            //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    return;
            //}
            //mMap.setMyLocationEnabled(true);

        //}else if (!PermissionGranted)
        //{
         //   mMap =googleMap;
        //}
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGranted  =false;
        switch (requestCode)
        {
            case 1234:
            {

                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED )
                {
                    for (int i =0; i<grantResults.length;i++)
                    {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        {
                            PermissionGranted =false;
                            return;
                        }
                    }
                    PermissionGranted = true;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
