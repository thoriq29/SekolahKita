package com.example.thoriq.sekolahkita.ProfilSekolah;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.thoriq.sekolahkita.Config.Server;
import com.example.thoriq.sekolahkita.R;
import com.example.thoriq.sekolahkita.Volley.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thoriq on 23/02/2018.
 */

public class HomeFragment extends Fragment {

    String id_sekolah,lat,lng,nama_sekolah,alamat,web,akre,logo;
    TextView Nama,Alamat,Web,Akre;
    ImageView Logo;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_profil,container,false);
        ((AppCompatActivity)getActivity()).getSupportActionBar();
        id_sekolah = getActivity().getIntent().getStringExtra("id_sekolah");
        Nama = (TextView) view.findViewById(R.id.namaSekolahProfil);
        Alamat = (TextView) view.findViewById(R.id.alamat);
        Web = (TextView) view.findViewById(R.id.web);
        Akre = (TextView) view.findViewById(R.id.Akreditasi);
        Logo = (ImageView) view.findViewById(R.id.logo);
        loadDataSekolah();

        return view;
    }
    private void loadDataSekolah()
    {
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, Server.SERVER_URL + "List/SekolahQ.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("sekolahQ");
                            JSONObject data = jsonArray.getJSONObject(0);
                            nama_sekolah = data.getString("nama_sekolah");
                            alamat = data.getString("alamat");
                            logo = data.getString("foto_sekolah");
                            web = data.getString("website");
                            akre =  data.getString("akreditasi");

                            Alamat.setText(alamat);
                            Nama.setText(nama_sekolah);
                            Glide.with(getActivity()).load(logo).into(Logo);
                            Akre.setText(akre);
                            Web.setText(web);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
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
        Handler.getInstance(getActivity()).addToRequestQue(stringRequest);
    }
    public void load()
    {
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, Server.SERVER_URL + "List/latlong.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("lg");
                                    JSONObject data = jsonArray.getJSONObject(0);
                                    lat = data.getString("lat");
                                    lng = data.getString("lng");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
                Handler.getInstance(getActivity()).addToRequestQue(stringRequest);
            }
}
