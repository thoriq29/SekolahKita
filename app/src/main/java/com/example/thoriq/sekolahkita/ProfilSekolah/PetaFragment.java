package com.example.thoriq.sekolahkita.ProfilSekolah;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.thoriq.sekolahkita.Config.Server;
import com.example.thoriq.sekolahkita.R;
import com.example.thoriq.sekolahkita.Volley.Handler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thoriq on 23/02/2018.
 */

public class PetaFragment extends Fragment implements OnMapReadyCallback {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    GoogleMap mMap;
    private Boolean PermissionGranted =false;
    MarkerOptions markerOptions;
    LatLng latLng,center;
    CameraPosition cameraPosition;
    String id_sekolah,Title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lokasi, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        markerOptions =  new MarkerOptions();
        id_sekolah = getActivity().getIntent().getStringExtra("id_sekolah");
        getMarkers();
        return view;
    }
    private void getLocationPermission()
    {
        String[] permission  ={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(getActivity(),FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            PermissionGranted =true;
            init();
        }else
        {
            ActivityCompat.requestPermissions(getActivity(),permission,1234);

        }
    }
    public void init()
    {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        Toast.makeText(getActivity(),"Map is Ready",Toast.LENGTH_SHORT).show();
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

    public void getMarkers()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.SERVER_URL + "List/latlong.php"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("lg");
                    JSONObject data = jsonArray.getJSONObject(0);
                        Title =data.getString("nama_sekolah");
                        latLng = new LatLng(Double.parseDouble(data.getString("lat")),Double.parseDouble(data.getString("lng")));
                        AddMarkers(latLng,Title);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("id_sekolah",id_sekolah);
                return params;
            }
        };
        Handler.getInstance(getActivity().getApplicationContext()).addToRequestQue(stringRequest);
    }
    public void AddMarkers(final LatLng latLng, final String title)
    {
        markerOptions.position(latLng);
        markerOptions.title(title);
        mMap.addMarker(markerOptions);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                cameraPosition =  new CameraPosition.Builder().target(latLng).zoom(18).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
        cameraPosition =  new CameraPosition.Builder().target(latLng).zoom(13f).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        center = new LatLng(-6.935222,106.925999);

        getMarkers();
    }

}
