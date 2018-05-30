package com.example.thoriq.sekolahkita.Jurusan;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thoriq.sekolahkita.Prestasi.ModelPrestasi;
import com.example.thoriq.sekolahkita.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thoriq on 04/03/2018.
 */

public class AdapterJurusan extends ArrayAdapter<ModelJurusan> {
    private Context mCtx;

    //we are storing all the products in a list
    private List<ModelJurusan> jurusanList;

    public AdapterJurusan(List<ModelJurusan> modelJurusen, Context mCtx)
    {
        super(mCtx,R.layout.list_jurusan,modelJurusen);
        this.jurusanList = modelJurusen;
        this.mCtx = mCtx;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View list = inflater.inflate(R.layout.list_jurusan,null,true);
        TextView idJur,namaJur,AkreJur,KelasJur;

        idJur = (TextView) list.findViewById(R.id.idJurusan);
        namaJur = (TextView) list.findViewById(R.id.namaJurusan);
        AkreJur = (TextView) list.findViewById(R.id.akreJurusan);
        KelasJur = (TextView) list.findViewById(R.id.kelasJurusan);
        ModelJurusan jurusan = jurusanList.get(position);
        idJur.setText(jurusan.getId());
        namaJur.setText(jurusan.getNama());
        AkreJur.setText("Akreditasi : "+jurusan.getAkre());
        KelasJur.setText(jurusan.getKelas()+ " Kelas");
        return list;
    }
}
