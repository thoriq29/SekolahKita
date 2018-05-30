package com.example.thoriq.sekolahkita.ProfilSekolah;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.thoriq.sekolahkita.Config.Server;
import com.example.thoriq.sekolahkita.Model.LatLangs;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SemuaLokasiSekolah extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> latLngs;
    List<LatLangs> latLangses;
    MarkerOptions markerOptions;
    LatLng latLng,center;
    CameraPosition cameraPosition;
    String nama,id,jenjang;
    String Id,Title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semua_lokasi_sekolah);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        markerOptions =  new MarkerOptions();
        latLngs = new ArrayList<>();

    }

    public void AddMarkers(final LatLng latLng, final String title)
    {
        markerOptions.position(latLng);
        markerOptions.title(title);
        mMap.addMarker(markerOptions);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                nama = marker.getTitle();
                SELECTID();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent =  new Intent(getApplicationContext(),ProfileSekolah.class);
                        intent.putExtra("id_sekolah",id);
                        intent.putExtra("jenjang",jenjang);
                        finish();
                        startActivity(intent);
                    }
                }, 1000);
                //Intent intent =  new Intent(getApplicationContext(),ProfileSekolah.class);
                //intent.putExtra("nama_sekolah",marker.getTitle());
                //finish();
                //startActivity(intent);
            }
        });
    }
    public void getMarkers()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.SERVER_URL + "List/AllLatLng.php"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject     jsonObject = new JSONObject(response);
                    String getObj = jsonObject.getString("latlng");
                    JSONArray array = new JSONArray(getObj);
                    for (int i=0; i <array.length(); i++)
                    {
                        JSONObject jsonObject1 = array.getJSONObject(i);
                        Id = jsonObject1.getString("id_sekolah");
                        Title =jsonObject1.getString("nama_sekolah");
                        latLng = new LatLng(Double.parseDouble(jsonObject1.getString("lat")),Double.parseDouble(jsonObject1.getString("lng")));
                        AddMarkers(latLng,Title);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Handler.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }
    public void SELECTID()
    {
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, Server.SERVER_URL + "List/SekolahQQ.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("sekolahQ");
                            JSONObject data = jsonArray.getJSONObject(0);
                            id = data.getString("id_sekolah");
                            jenjang =data.getString("nama_jenjang");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("nama",nama);
                return params;
            }
        };
        Handler.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }
    void a()
    {
        for (int i=0; i<latLangses.size();i++)
        {
            mMap.addMarker(markerOptions);
            LatLng l = new LatLng(-6.90564,106.93064);
            Float b = 13f;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l,b));
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        center = new LatLng(-6.935222,106.925999);
        cameraPosition =  new CameraPosition.Builder().target(center).zoom(9).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        getMarkers();
    }
}
