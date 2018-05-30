package com.example.thoriq.sekolahkita.Prestasi;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thoriq.sekolahkita.Config.Server;
import com.example.thoriq.sekolahkita.Model.Kota;
import com.example.thoriq.sekolahkita.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Thoriq on 04/03/2018.
 */

public class AdapterPrestasi extends ArrayAdapter<ModelPrestasi>{
    private Context mCtx;

    //we are storing all the products in a list
    private List<ModelPrestasi> prestasiList;
    public AdapterPrestasi(List<ModelPrestasi> modelPrestasis, Context mCtx)
    {
        super(mCtx, R.layout.list_prestasi,modelPrestasis);
        this.prestasiList = modelPrestasis;
        this.mCtx = mCtx;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View list = inflater.inflate(R.layout.list_prestasi,null,true);
        TextView idPres,namaPres,tingkat;
        ImageView imageView;
        idPres = (TextView) list.findViewById(R.id.IdPrestasi);
        namaPres = (TextView) list.findViewById(R.id.namaPrestasi);
        tingkat = (TextView) list.findViewById(R.id.tingkatPrestasi);
        ModelPrestasi kotaKab = prestasiList.get(position);
        idPres.setText(kotaKab.getId());
        idPres.setTextColor(Color.RED);
        namaPres.setText(kotaKab.getNama());
        tingkat.setText("Tingkat "+kotaKab.getTingkatan());
        return list;
    }
}
