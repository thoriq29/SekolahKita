package com.example.thoriq.sekolahkita;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.thoriq.sekolahkita.Adapter.AdapterKota;
import com.example.thoriq.sekolahkita.Config.Server;
import com.example.thoriq.sekolahkita.Model.Kota;
import com.example.thoriq.sekolahkita.Volley.Handler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CariKota extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    List<Kota> kotaList;
    ConnectivityManager conMgr;
    TextView error;
    EditText search;
    ListView listView;
    ImageButton X;
    AdView adView;
    String idKota,namaKota,namaProv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari_kota);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        X = (ImageButton) findViewById(R.id.img);
        X.setOnClickListener(this);
        MobileAds.initialize(this,"ca-app-pub-4615847918321667~6323167091");
        adView = (AdView) findViewById(R.id.adView);
        loadAd();
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        search = (EditText) findViewById(R.id.SearchData);
        kotaList = new ArrayList<>();
        idKota = getIntent().getStringExtra("nama_kota");
        namaKota = getIntent().getStringExtra("nama_kota");
        namaProv = getIntent().getStringExtra("nama_prov");
        listView = (ListView) findViewById(R.id.listViewKota);
        if(search.isFocused())
        {
            X.setVisibility(View.VISIBLE);
        }else {
            X.setVisibility(View.INVISIBLE);
        }
        ListOnItemClick();
        cekKoneksi();
        LoadKota();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MenuUtama.class);
                intent.putExtra("nama_kota",idKota);
                intent.putExtra("nama_kota",namaKota);
                intent.putExtra("nama_prov",namaProv);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            String a = editable.toString();
                if (a.isEmpty())
                {
                    kotaList.clear();
                    LoadKota();
                    X.setVisibility(View.INVISIBLE);
                }else {
                    kotaList.clear();
                    LoadDataFiltered();
                    X.setVisibility(View.VISIBLE);
                }
            }
        });

    }
    public void LoadKota()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.SERVER_URL+"List/KotaProv.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray titipanarray = obj.getJSONArray("semuaKotaKab");

                                for (int i = 0; i < titipanarray.length(); i++) {
                                    JSONObject titipanObj = titipanarray.getJSONObject(i);
                                    Kota semua = new Kota(titipanObj.getString("id_provinsi"), titipanObj.getString("id_kota_kab"),titipanObj.getString("nama_kota_kab"),titipanObj.getString("nama_provinsi"), titipanObj.getString("foto"));
                                    kotaList.add(semua);
                                }
                                 AdapterKota listSemua = new AdapterKota(kotaList, CariKota.this);
                                listView.setAdapter(listSemua);
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isConnected()
                        && conMgr.getActiveNetworkInfo().isAvailable())
                {
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                }else
                {
                    Toast.makeText(getApplicationContext(),"Tidak Ada Koneksi Internet",Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                return params;
            }
        };
        Handler.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }
    public void LoadDataFiltered()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.SERVER_URL+"List/KotaProvFiltered.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray titipanarray = obj.getJSONArray("semuaKotaKab");

                            for (int i = 0; i < titipanarray.length(); i++) {
                                JSONObject titipanObj = titipanarray.getJSONObject(i);
                                Kota semua = new Kota(titipanObj.getString("id_provinsi"), titipanObj.getString("id_kota_kab"),titipanObj.getString("nama_kota_kab"),titipanObj.getString("nama_provinsi"), titipanObj.getString("foto"));
                                kotaList.add(semua);
                            }
                            AdapterKota listSemua = new AdapterKota(kotaList, CariKota.this);
                            listView.setAdapter(listSemua);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isConnected()
                        && conMgr.getActiveNetworkInfo().isAvailable())
                {
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                }else
                {
                    Toast.makeText(getApplicationContext(),"Tidak Ada Koneksi Internet",Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("nama",search.getText().toString());
                return params;
            }
        };
        Handler.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }
    public void cekKoneksi()
    {
        conMgr =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isConnected()
                    && conMgr.getActiveNetworkInfo().isAvailable())
            {

            }else
            {
                Toast.makeText(getApplicationContext(),"Tidak Ada Koneksi Internet",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void ListOnItemClick()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idKota = (TextView) view.findViewById(R.id.IdKotaProv);
                TextView namaKota = (TextView) view.findViewById(R.id.NamaKotaProv);
                TextView namaProv = (TextView) view.findViewById(R.id.NamaProv);
                Intent intent = new Intent(getApplicationContext(),CariSemuaSekolah.class);
                intent.putExtra("nama_kota",idKota.getText().toString());
                intent.putExtra("nama_kota",namaKota.getText().toString());
                intent.putExtra("nama_prov",namaProv.getText().toString());
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == X)
        {
            search.setText("");
        }
    }
    public void loadAd()
    {
        adView.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }
        });
    }
}
