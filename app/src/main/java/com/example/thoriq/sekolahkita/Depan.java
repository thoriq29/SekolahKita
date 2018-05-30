package com.example.thoriq.sekolahkita;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.thoriq.sekolahkita.ProfilSekolah.SemuaLokasiSekolah;

public class Depan extends AppCompatActivity implements View.OnClickListener{

    Button KeSekolah, LokasiSekolah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        KeSekolah = (Button) findViewById(R.id.btnBerdasarLokasi);
        LokasiSekolah = (Button) findViewById(R.id.btnSemuaLokasi);
        KeSekolah.setOnClickListener(this);
        LokasiSekolah.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == KeSekolah){
            startActivity(new Intent(getApplicationContext(),MenuUtama.class));

        }else if (view == LokasiSekolah)
        {
            startActivity(new Intent(getApplicationContext(),SemuaLokasiSekolah.class));
        }
    }
}
