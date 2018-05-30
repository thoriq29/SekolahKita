package com.example.thoriq.sekolahkita.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.style.TtsSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thoriq.sekolahkita.Config.Server;
import com.example.thoriq.sekolahkita.MenuUtama;
import com.example.thoriq.sekolahkita.Model.Kota;
import com.example.thoriq.sekolahkita.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterKota extends ArrayAdapter<Kota> {


    private Context mCtx;

    //we are storing all the products in a list
    private List<Kota> kotaList;
    public AdapterKota(List<Kota> kotaKabs, Context mCtx)
    {
        super(mCtx, R.layout.card_kota_prov,kotaKabs);
        this.kotaList = kotaKabs;
        this.mCtx = mCtx;
    }
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View list = inflater.inflate(R.layout.card_kota_prov,null,true);
        TextView idProv,idKota,namaKota,namaProv;
        ImageView imageView;
        idProv = (TextView)list.findViewById(R.id.IdProvProv);
        idKota = (TextView) list.findViewById(R.id.IdKotaProv);
        namaKota = (TextView) list.findViewById(R.id.NamaKotaProv);
        namaProv = (TextView) list.findViewById(R.id.NamaProv);
        imageView = (ImageView) list.findViewById(R.id.fotoKotaProv);
        Kota kotaKab = kotaList.get(position);
        idKota.setText(kotaKab.getIdkotakab());
        idKota.setTextColor(Color.RED);
        idProv.setText(kotaKab.getIdprovinsi());
        namaKota.setText(kotaKab.getNamakotakab());
        namaProv.setText(kotaKab.getNamaprovinsi());
        Picasso.with(mCtx)
                .load(Server.IMAGE_URL+kotaKab.getFoto())
                .into(imageView);
        return list;
    }
}
