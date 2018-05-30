package com.example.thoriq.sekolahkita;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.thoriq.sekolahkita.Adapter.AdapterSemuaSekolah;
import com.example.thoriq.sekolahkita.Config.Server;
import com.example.thoriq.sekolahkita.Volley.Handler;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CariSemuaSekolah extends AppCompatActivity implements View.OnClickListener{

    RecyclerView recyclerView;
    EditText search;
    Button Jenjang,Wilayah,Status,CobaLagi;
    Toolbar toolbar;
    ProgressBar bar;
    TextView Error;
    String nama_kota;
    SharedPreferences sharedPreferences;
    Boolean keduaX =false;
    String tingkat;
    String wilayah ="KOTA";
    String status;
    String id_ses ="0";
    private InterstitialAd mInterstitialAd;
    public static final String my_shared_pref = "my_share_pref";
    public static final String session_status =     "session_status";
    List<com.example.thoriq.sekolahkita.Model.SemuaSekolah> semuaSekolahList;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semua_sekolah);

        init();


    }
    public void init()
    {
        recyclerView = (RecyclerView) findViewById(R.id.recySemuaSekolah);
        Jenjang = (Button) findViewById(R.id.btnPilihJenjang);
        nama_kota = getIntent().getStringExtra("nama_kota");
        tingkat = getIntent().getStringExtra("jenjang");
        status = getIntent().getStringExtra("status");
        Wilayah= (Button) findViewById(R.id.btnPilihWilayah);
        Status = (Button) findViewById(R.id.btnPilihStatus);
        Jenjang.setOnClickListener(this);
        Wilayah.setOnClickListener(this);
        Status.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MenuUtama.class);
                intent.putExtra("nama_kota", nama_kota);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                loadInterstitialAd();
            }
        });



        recyclerView.setHasFixedSize(true);
        CobaLagi = (Button) findViewById(R.id.btnCobaLagi);
        CobaLagi.setVisibility(View.INVISIBLE);
        CobaLagi.setOnClickListener(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        semuaSekolahList = new ArrayList<>();
        Error = (TextView) findViewById(R.id.tvErrorKota);
        Error.setVisibility(View.INVISIBLE);
        if (tingkat.equals("Semua Tingkatan"))
        {
            loadDataFirst();
        }else
        {
            loadDataNotFirst();
        }
        sharedPreferences = getSharedPreferences(my_shared_pref, Context.MODE_PRIVATE);
        keduaX =sharedPreferences.getBoolean(session_status,false);
        id_ses = sharedPreferences.getString("id_ses",null);
        if(keduaX)
        {

        }else
        {
            ShowCaseProvinsi();
        }
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }
    private void loadDataFirst()
    {
        semuaSekolahList.clear();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Mohon Tunggu");
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        StringRequest s = new StringRequest(Request.Method.POST, Server.SERVER_URL+"List/semuaSekolah1.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("null")) {
                            Timer t = new Timer();
                            t.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            },1000);
                            Error.setVisibility(View.VISIBLE);
                            Error.setText("Data Tidak Ditemukan");
                            CobaLagi.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            Error.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            CobaLagi.setVisibility(View.INVISIBLE);
                            Timer t = new Timer();
                            t.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            },1000);

                            try
                            {
                                JSONArray j = new JSONArray(response);
                                for(int i=0;i<j.length(); i++)
                                {
                                    JSONObject jObj = j.getJSONObject(i);

                                    semuaSekolahList.add(new com.example.thoriq.sekolahkita.Model.SemuaSekolah(
                                            jObj.getInt("id_sekolah"),
                                            jObj.getString("nama_sekolah"),
                                            jObj.getString("alamat"),
                                            jObj.getString("akreditasi"),
                                            jObj.getString("foto_sekolah"),
                                            jObj.getString("nama_jenjang")
                                    ));
                                }
                                AdapterSemuaSekolah adapter = new AdapterSemuaSekolah(CariSemuaSekolah.this,semuaSekolahList);
                                recyclerView.setAdapter(adapter);
                            }catch (JSONException e)
                            {

                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError)
                {
                    Error.setVisibility(View.VISIBLE);
                    Error.setText("Gagal Tersambung Ke Jaringan, Silahkan Coba Lagi");
                    CobaLagi.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("nama_jenjang",tingkat);
                params.put("nama_wilayah",wilayah);
                params.put("nama_kota", nama_kota);
                params.put("nama_status",status);
                return params;
            }
        };
        Handler.getInstance(getApplicationContext()).addToRequestQue(s);
    }
    private void loadDataNotFirst()
    {
        semuaSekolahList.clear();
        StringRequest s = new StringRequest(Request.Method.POST, Server.SERVER_URL+"List/semuaSekolah.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("null")) {
                            Error.setVisibility(View.VISIBLE);
                            Error.setText("Data Tidak Ditemukan");
                            CobaLagi.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            CobaLagi.setVisibility(View.INVISIBLE);
                            Error.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            try
                            {
                                JSONArray j = new JSONArray(response);
                                for(int i=0;i<j.length(); i++)
                                {
                                    JSONObject jObj = j.getJSONObject(i);
                                    semuaSekolahList.add(new com.example.thoriq.sekolahkita.Model.SemuaSekolah(
                                            jObj.getInt("id_sekolah"),
                                            jObj.getString("nama_sekolah"),
                                            jObj.getString("alamat"),
                                            jObj.getString("akreditasi"),
                                            jObj.getString("foto_sekolah"),
                                            jObj.getString("nama_jenjang")
                                    ));
                                }
                                AdapterSemuaSekolah adapter = new AdapterSemuaSekolah(CariSemuaSekolah.this,semuaSekolahList);
                                recyclerView.setAdapter(adapter);
                            }catch (JSONException e)
                            {

                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError)
                {
                    Error.setVisibility(View.VISIBLE);
                    Error.setText("Gagal Tersambung Ke Jaringan, Silahkan Coba Lagi");
                    CobaLagi.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("nama_jenjang",tingkat);
                params.put("nama_kota", nama_kota);
                params.put("nama_wilayah",wilayah);
                params.put("nama_status",status);
                return params;
            }
        };
        Handler.getInstance(getApplicationContext()).addToRequestQue(s);
    }
    public void ShowCaseProvinsi()
    {
        TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.btnPilihJenjang),"Pilih Jenjang","Pilih Tingkatan Sekolah. Mulai dari SD,SMP,SMA hingga SMK")
                        .outerCircleColor(R.color.bg_blue)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextColor(R.color.white)
                        .titleTextSize(20)
                        .descriptionTextSize(14)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(false)
                        .targetRadius(45),
                new TapTargetView.Listener()
                {
                    @Override
                    public void onTargetClick(TapTargetView view){
                        super.onTargetClick(view);
                        ShowCaseCariKotaLain();
                    }
                });

    }
    public void ShowCaseCariKotaLain()
    {
        TapTargetView.showFor(this,TapTarget.forView(findViewById(R.id.btnPilihWilayah),"Cari Kota dan Kabupaten di Provinsi Lain","Telusuri Sekolah di Kota dan Kabupaten Lain Diseluruh Indonesia")
                        .outerCircleColor(R.color.bg_blue)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextColor(R.color.white)
                        .titleTextSize(20)
                        .descriptionTextSize(15)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(false)
                        .targetRadius(45),
                new TapTargetView.Listener()
                {
                    @Override
                    public void onTargetClick(TapTargetView view){
                        super.onTargetClick(view);
                        ShowCaseStatus();
                    }
                });

    }
    public void ShowCaseStatus()
    {

        TapTargetView.showFor(this,TapTarget.forView(findViewById(R.id.btnPilihStatus),"Pilih Status Sekolah","Pilih Status Sekolah. Mulai dari NEGERI Dan SWASTA")
                        .outerCircleColor(R.color.bg_blue)
                        .outerCircleAlpha(0.96f)
                        .titleTextColor(R.color.white)
                        .titleTextSize(20)
                        .descriptionTextSize(15)
                        .descriptionTextColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(false)
                        .targetRadius(45),
                new TapTargetView.Listener()
                {
                    @Override
                    public void onTargetClick(TapTargetView view){
                        super.onTargetClick(view);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(session_status,true);
                        editor.putString("id_ses","1");
                        editor.commit();
                    }
                });

    }
    @Override
    public void onClick(View view)
    {
        if (view == Jenjang)
        {
            showDialogTingkatan();
        }
        else if (view == Status)
        {
            showDialogStatus();
        }else if (view == Wilayah)
        {
            Intent intent = new Intent(getApplicationContext(),MenuUtama.class);
            intent.putExtra("nama_kota", nama_kota);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            loadInterstitialAd();
            //showDialogWilayah();
        }else if (view == CobaLagi)
        {
            if (tingkat.equals("Semua Tingkatan"))
            {
                loadDataFirst();
            }else
            {
                loadDataNotFirst();
            }
        }
    }
    public void showDialogTingkatan()
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(CariSemuaSekolah.this);
        View parentView = getLayoutInflater().inflate(R.layout.dialog_tingkatan,null);
        final TextView SemuaTingkatan = (TextView) parentView.findViewById(R.id.tvSemuaTingkatan);
        final Button Pilih = (Button) parentView.findViewById(R.id.btnPilih);
        final Button Batal = (Button) parentView.findViewById(R.id.btnBatal);
        final TextView SD = (TextView) parentView.findViewById(R.id.tvSD);
        final TextView SMP = (TextView) parentView.findViewById(R.id.tvSMP);
        final TextView SMA = (TextView) parentView.findViewById(R.id.tvSMA);
        final TextView SMK = (TextView) parentView.findViewById(R.id.tvSMK);
        final String sma = SMA.getText().toString();
        final String smp = SMP.getText().toString();
        final String smk = SMK.getText().toString();
        final String sd = SD.getText().toString();
        final String semua = SemuaTingkatan.getText().toString();
        if(tingkat.equals(semua))
        {
            SemuaTingkatan.setTextColor(Color.BLACK);
            SemuaTingkatan.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
        }else if(tingkat.equals(sd))
        {
            SD.setTextColor(Color.BLACK);
            SD.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
        }else if (tingkat.equals(smp))
        {
            SMP.setTextColor(Color.BLACK);
            SMP.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
        }else if(tingkat.equals(sma))
        {
            SMA.setTextColor(Color.BLACK);
            SMA.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
        }else if(tingkat.equals(smk))
        {
            SMK.setTextColor(Color.BLACK);
            SMK.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
        }
        SemuaTingkatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SemuaTingkatan.setTextColor(Color.BLACK);
                SemuaTingkatan.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
                SD.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SMP.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SMA.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SMK.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SMK.setTextColor(Color.parseColor("#757575"));
                SD.setTextColor(Color.parseColor("#757575"));
                SMP.setTextColor(Color.parseColor("#757575"));
                SMA.setTextColor(Color.parseColor("#757575"));
            }
        });
        SD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SD.setTextColor(Color.BLACK);
                SD.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
                SemuaTingkatan.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SMP.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SMA.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SMK.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SMK.setTextColor(Color.parseColor("#757575"));
                SemuaTingkatan.setTextColor(Color.parseColor("#757575"));
                SMP.setTextColor(Color.parseColor("#757575"));
                SMA.setTextColor(Color.parseColor("#757575"));
            }
        });
        SMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SMP.setTextColor(Color.BLACK);
                SMP.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
                SD.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SemuaTingkatan.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SMA.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SMK.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SMK.setTextColor(Color.parseColor("#757575"));
                SD.setTextColor(Color.parseColor("#757575"));
                SemuaTingkatan.setTextColor(Color.parseColor("#757575"));
                SMA.setTextColor(Color.parseColor("#757575"));
            }
        });
        SMA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SMA.setTextColor(Color.BLACK);
                SMA.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
                SD.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SMP.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SemuaTingkatan.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SMK.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SMK.setTextColor(Color.parseColor("#757575"));
                SD.setTextColor(Color.parseColor("#757575"));
                SMP.setTextColor(Color.parseColor("#757575"));
                SemuaTingkatan.setTextColor(Color.parseColor("#757575"));
            }
        });
        SMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SMK.setTextColor(Color.BLACK);
                SMK.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
                SD.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SMP.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SMA.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SemuaTingkatan.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                SemuaTingkatan.setTextColor(Color.parseColor("#757575"));
                SD.setTextColor(Color.parseColor("#757575"));
                SMP.setTextColor(Color.parseColor("#757575"));
                SMA.setTextColor(Color.parseColor("#757575"));
            }
        });
        bottomSheetDialog.setContentView(parentView);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) parentView.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        bottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1000,getResources().getDisplayMetrics()));
        bottomSheetDialog.show();
        Pilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SemuaTingkatan.getCurrentTextColor() == Color.BLACK)
                {
                    tingkat = "Semua Tingkatan";
                    loadDataFirst();
                }else if(SD.getCurrentTextColor() == Color.BLACK)
                {
                    tingkat = "SD";
                    loadDataNotFirst();
                }else if (SMP.getCurrentTextColor() == Color.BLACK)
                {
                    tingkat = "SMP";
                    loadDataNotFirst();
                }else if (SMA.getCurrentTextColor() == Color.BLACK)
                {
                    tingkat = "SMA";
                    loadDataNotFirst();
                }else if (SMK.getCurrentTextColor() == Color.BLACK)
                {
                    tingkat = "SMK";
                    loadDataNotFirst();
                }

                bottomSheetDialog.dismiss();
            }
        });
        Batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
    }
    public void showDialogStatus()
    {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(CariSemuaSekolah.this);
        View parentView = getLayoutInflater().inflate(R.layout.dialog_status,null);
        final Button Pilih = (Button) parentView.findViewById(R.id.btnPilihStatus);
        final Button Batal = (Button) parentView.findViewById(R.id.btnBatal);
        final TextView Negeri = (TextView) parentView.findViewById(R.id.tvNegeri);
        final TextView Swasta = (TextView) parentView.findViewById(R.id.tvSwasta);
        final String negeri = Negeri.getText().toString();
        final String swasta = Swasta.getText().toString();
        if (status.equals(negeri))
        {
            Negeri.setTextColor(Color.BLACK);
            Negeri.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
        }else if (status.equals(swasta))
        {
            Swasta.setTextColor(Color.BLACK);
            Swasta.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
        }
        Negeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Negeri.setTextColor(Color.BLACK);
                Negeri.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
                Swasta.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                Swasta.setTextColor(Color.parseColor("#757575"));
            }
        });
        Swasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Swasta.setTextColor(Color.BLACK);
                Swasta.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
                Negeri.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                Negeri.setTextColor(Color.parseColor("#757575"));
            }
        });
        bottomSheetDialog.setContentView(parentView);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) parentView.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        bottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1000,getResources().getDisplayMetrics()));
        bottomSheetDialog.show();
        Pilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Negeri.getCurrentTextColor() == Color.BLACK)
                {
                    status = "NEGERI";
                    if (tingkat.equals("Semua Tingkatan"))
                    {
                        loadDataFirst();
                    }else
                    {
                        loadDataNotFirst();
                    }
                }else if (Swasta.getCurrentTextColor() == Color.BLACK)
                {
                    status = "SWASTA";
                    if (tingkat.equals("Semua Tingkatan"))
                    {
                        loadDataFirst();
                    }else
                    {
                        loadDataNotFirst();
                    }
                }
                bottomSheetDialog.dismiss();
            }
        });
        Batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
    }
    public void showDialogWilayah()
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(CariSemuaSekolah.this);
        View parentView = getLayoutInflater().inflate(R.layout.dialog_wilayah,null);
        final Button Pilih = (Button) parentView.findViewById(R.id.btnPilihWil);
        final Button Batal = (Button) parentView.findViewById(R.id.btnBatal);
        final TextView Kota = (TextView) parentView.findViewById(R.id.tvKota);
        final TextView Kabu = (TextView) parentView.findViewById(R.id.tvKabupaten);
        final String kota = Kota.getText().toString();
        final String kab = Kabu.getText().toString();
        if (wilayah.equals(kota))
        {
            Kota.setTextColor(Color.BLACK);
            Kota.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
        }else if (wilayah.equals(kab))
        {
            Kabu.setTextColor(Color.BLACK);
            Kabu.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
        }
        Kota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kota.setTextColor(Color.BLACK);
                Kota.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
                Kabu.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                Kabu.setTextColor(Color.parseColor("#757575"));
            }
        });
        Kabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kabu.setTextColor(Color.BLACK);
                Kabu.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_black_24dp,0);
                Kota.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                Kota.setTextColor(Color.parseColor("#757575"));
            }
        });
        bottomSheetDialog.setContentView(parentView);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) parentView.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        bottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1000,getResources().getDisplayMetrics()));
        bottomSheetDialog.show();
        Pilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Kota.getCurrentTextColor() == Color.BLACK)
                {
                    wilayah = "KOTA";
                    loadDataNotFirst();
                }else if (Kabu.getCurrentTextColor() == Color.BLACK)
                {
                    wilayah = "KABUPATEN";
                    loadDataNotFirst();
                }
                bottomSheetDialog.dismiss();
            }
        });
        Batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
    }
    public void logout()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(this.session_status,false);
        editor.putString("id_ses",null);
        editor.commit();
    }
    public void loadInterstitialAd()
    {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4615847918321667/9524115312");
        mInterstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mInterstitialAd.isLoaded())
                {
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),MenuUtama.class);
        intent.putExtra("nama_kota", nama_kota);
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        loadInterstitialAd();
    }

}
