package com.example.thoriq.sekolahkita.Galeri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thoriq.sekolahkita.R;

public class DetailGalerry extends AppCompatActivity {

    String url,title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_galerry);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("nama");
        ImageView imageView = (ImageView) findViewById(R.id.detailimg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Glide.with(this).load(url).into(imageView);
    }
}
