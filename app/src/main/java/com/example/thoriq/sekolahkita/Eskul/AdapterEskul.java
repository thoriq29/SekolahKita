package com.example.thoriq.sekolahkita.Eskul;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.thoriq.sekolahkita.Jurusan.ModelJurusan;
import com.example.thoriq.sekolahkita.R;

import java.util.List;

/**
 * Created by Thoriq on 05/03/2018.
 */

public class AdapterEskul extends ArrayAdapter<ModelEskul> {

    private Context mCtx;
    private List<ModelEskul> eskulList;

    public AdapterEskul(List<ModelEskul> eskulList, Context mCtx)
    {
        super(mCtx, R.layout.list_jurusan,eskulList);
        this.eskulList = eskulList;
        this.mCtx = mCtx;
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View list = inflater.inflate(R.layout.list_eskul,null,true);

        TextView nama,jenis;
        nama = (TextView) list.findViewById(R.id.namaEskul);
        jenis =(TextView) list.findViewById(R.id.jenisEskul);
        ModelEskul eskul = eskulList.get(position);
        nama.setText(eskul.getNama());
        jenis.setText(eskul.getJenis());
        return list;
    }
}
