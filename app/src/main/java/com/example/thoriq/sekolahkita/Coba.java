package com.example.thoriq.sekolahkita;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.thoriq.sekolahkita.Config.Server;
import com.example.thoriq.sekolahkita.Volley.Handler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thoriq on 01/03/2018.
 */

public class Coba extends AppCompatActivity {
    AdView adView,adView1;
    LinearLayout layout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_profil);
        MobileAds.initialize(this,"ca-app-pub-4615847918321667~6323167091");
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        loadAd();
    }
    public void loadAd()
    {
        for (int i=0; i< 10; i++)
        {
            adView1 = new AdView(this);
            adView1.setAdSize(AdSize.SMART_BANNER);
            adView1.setAdUnitId("ca-app-pub-4615847918321667/9420582656");
            AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

            // Optionally populate the ad request builder.
            adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

            // Add the AdView to the view hierarchy.
            layout.addView(adView1);

            // Start loading the ad.
            adView1.loadAd(adRequestBuilder.build());

            setContentView(layout);
        }
    }
}
