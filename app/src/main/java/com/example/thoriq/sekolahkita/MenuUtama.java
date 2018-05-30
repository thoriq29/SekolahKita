package com.example.thoriq.sekolahkita;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.thoriq.sekolahkita.Config.Server;
import com.example.thoriq.sekolahkita.Volley.Handler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.valueOf;

public class MenuUtama extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    String idKota ="1";
    String namaKota;
    String namaProv ;
    ArrayList<String> Users;
    ArrayList<String> Kota;
    AdView adView;
    Button CariKota,CobaLagi;
    Spinner spinnerKota,SpinnerJenjang,SpinnerStatus,SpinnerProv;
    String[] DATA = {"Pilih Kota/Kabupaten dulu"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);
        SpinnerProv = (Spinner) findViewById(R.id.spinnerProv);
        spinnerKota = (Spinner) findViewById(R.id.spinnerKota);
        SpinnerJenjang = (Spinner) findViewById(R.id.spinnerJenjang);
        SpinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        CobaLagi = (Button) findViewById(R.id.btnCobaLagi);
        CobaLagi.setOnClickListener(this);
        setSupportActionBar(toolbar);
        Users = new ArrayList<String>();
        Kota = new ArrayList<String>();
        idKota = getIntent().getStringExtra("nama_kota");
        namaKota = getIntent().getStringExtra("nama_kota");
        CariKota = (Button) findViewById(R.id.btnCariSekolah);
        CariKota.setOnClickListener(this);
        loadSPProv();
        fillJejang();
        fillStatus();
        SpinnerProv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                namaProv =valueOf(SpinnerProv.getSelectedItem());
                LoadKota();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        MobileAds.initialize(this,"ca-app-pub-4615847918321667~6323167091");
        adView = (AdView) findViewById(R.id.adView);
        loadAd();
        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);
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
    public void fillJejang()
    {
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Semua Tingkatan");
        spinnerArray.add("SD");
        spinnerArray.add("SMP");
        spinnerArray.add("SMA");
        spinnerArray.add("SMK");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerJenjang.setAdapter(adapter);

    }
    public void fillStatus()
    {
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("NEGERI");
        spinnerArray.add("SWASTA");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerStatus.setAdapter(adapter);
    }
    private void loadSPProv()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,Server.SERVER_URL+"List/provinsi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CobaLagi.setVisibility(View.INVISIBLE);
                        CariKota.setVisibility(View.VISIBLE);
                        JSONObject j = null;
                        try
                        {
                            j = new JSONObject(response);
                            JSONArray array = j.getJSONArray("provinsi");
                            getNamaProvinsi(array);
                        }catch (JSONException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError)
                {
                    Toast.makeText(getApplicationContext(),"Tidak Dapat Tersambung ke Internet",Toast.LENGTH_SHORT).show();
                    CariKota.setVisibility(View.INVISIBLE);
                    CobaLagi.setVisibility(View.VISIBLE);
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();

                return params;
            }
        };
        Handler.getInstance(MenuUtama.this).addToRequestQue(stringRequest);
    }
    private void getNamaProvinsi(JSONArray j)
    {
        Users.add("Pilih Provinsi");
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list

                Users.add(json.getString("nama_provinsi"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        SpinnerProv.setAdapter(new ArrayAdapter<String>(MenuUtama.this, android.R.layout.simple_spinner_dropdown_item, Users));
    }
    private void LoadKota()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Server.SERVER_URL+"List/kota.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try
                        {
                            j = new JSONObject(response);
                            JSONArray array = j.getJSONArray("kota");
                            getNamaKota(array);
                        }catch (JSONException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError)
                {
                    Toast.makeText(getApplicationContext(),"Tidak Dapat Tersambung ke Internet",Toast.LENGTH_SHORT).show();
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("nama_prov",namaProv);
                return params;
            }
        };
        Handler.getInstance(MenuUtama.this).addToRequestQue(stringRequest);
    }

    private void getNamaKota(JSONArray j)
    {
        Kota.clear();
        Kota.add("Pilih Kota/Kabupaten");
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list

                Kota.add(json.getString("nama_kota_kab"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        spinnerKota.setAdapter(new ArrayAdapter<String>(MenuUtama.this, android.R.layout.simple_spinner_dropdown_item, Kota));
    }
    @Override
    public void onClick(View view) {
        if(view == CariKota){
            if (SpinnerProv.getSelectedItem().toString().equals("Pilih Provinsi"))
            {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Perhatian!");
                alertDialogBuilder.setMessage("Silahkan "+SpinnerProv.getSelectedItem().toString()+" Terlebih Dahulu");
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }else if (spinnerKota.getSelectedItem().toString().equals("Pilih Kota/Kabupaten"))
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Perhatian!");
                alertDialogBuilder.setMessage("Silahkan "+spinnerKota.getSelectedItem().toString()+" Terlebih Dahulu");
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }else if (SpinnerJenjang.getSelectedItem().toString().equals("Semua Tingkatan") )
            {
                Intent intent =  new Intent(getApplicationContext(),CariSemuaSekolah.class);
                intent.putExtra("nama_kota",spinnerKota.getSelectedItem().toString());
                intent.putExtra("jenjang",SpinnerJenjang.getSelectedItem().toString());
                intent.putExtra("status",SpinnerStatus.getSelectedItem().toString());
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }else
            {
                Intent i =  new Intent(getApplicationContext(),CariSemuaSekolah.class);
                i.putExtra("nama_kota",spinnerKota.getSelectedItem().toString());
                i.putExtra("jenjang",SpinnerJenjang.getSelectedItem().toString());
                i.putExtra("status",SpinnerStatus.getSelectedItem().toString());
                finish();
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        }else if (view == CobaLagi)
        {
            loadSPProv();
        }

    }
}
