package com.example.thoriq.sekolahkita.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.thoriq.sekolahkita.Model.Kota;
import com.example.thoriq.sekolahkita.Model.ModelMisi;
import com.example.thoriq.sekolahkita.R;

import java.util.List;

/**
 * Created by Thoriq on 03/03/2018.
 */

public class AdapterMisi extends ArrayAdapter<ModelMisi> {
    private Context mCtx;

    //we are storing all the products in a list
    private List<ModelMisi> misiList;
    public AdapterMisi(List<ModelMisi> misiList, Context mCtx)
    {
        super(mCtx, R.layout.misi,misiList);
        this.misiList = misiList;
        this.mCtx = mCtx;
    }


    @Override
    public View getView(int position,  View convertView,ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View list = inflater.inflate(R.layout.misi,null,true);
        TextView nama = (TextView) list.findViewById(R.id.namaMisi);
        ModelMisi modelMisi = misiList.get(position);
        nama.setText(modelMisi.getMisi());
        return list;
    }
}
