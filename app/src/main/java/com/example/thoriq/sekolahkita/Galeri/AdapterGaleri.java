package com.example.thoriq.sekolahkita.Galeri;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thoriq.sekolahkita.Config.Server;
import com.example.thoriq.sekolahkita.R;

import java.util.List;

/**
 * Created by Thoriq on 04/03/2018.
 */

public class AdapterGaleri extends RecyclerView.Adapter<AdapterGaleri.AdapterGaleriViewHolder>{


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<ModelGaleri> modelGaleri;

    //getting the context and product list with constructor
    public AdapterGaleri(Context mCtx, List<ModelGaleri> modelGaleri) {
        this.mCtx = mCtx;
        this.modelGaleri = modelGaleri;
    }



    @Override
    public AdapterGaleri.AdapterGaleriViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_galeri, null);
        return new AdapterGaleri.AdapterGaleriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterGaleri.AdapterGaleriViewHolder holder, int position) {
        //getting the product of the specified position
        final ModelGaleri semuaGaleri = modelGaleri.get(position);
        holder.nama.setText(semuaGaleri.getJudul());
        Glide.with(mCtx).load(Server.IMAGE_URL+semuaGaleri.getImage())
                .error(R.drawable.bannersmk1).into(holder.foto);
        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mCtx);
                View mView = layoutInflaterAndroid.inflate(R.layout.foto_guru, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(mCtx);


                alertDialogBuilderUserInput.setView(mView);
                final ImageView foto = (ImageView) mView.findViewById(R.id.detailFotoGuru);
                alertDialogBuilderUserInput
                        .setCancelable(true);
                Glide.with(mCtx).load(Server.IMAGE_URL+semuaGaleri.getImage()).into(foto);

                final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
                foto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mCtx, DetailGalerry.class);
                        intent.putExtra("url",Server.IMAGE_URL+semuaGaleri.getImage());
                        intent.putExtra("nama",semuaGaleri.getJudul());
                        view.getContext().startActivity(intent);
                        alertDialogAndroid.dismiss();
                    }
                });
            }
        });
        //binding the data with the viewholder views


    }


    @Override
    public int getItemCount() {
        return modelGaleri.size();
    }


    class AdapterGaleriViewHolder extends RecyclerView.ViewHolder {
        TextView nama;
        ImageView foto;
        CardView card;
        public AdapterGaleriViewHolder(View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.judul_foto);
            foto = itemView.findViewById(R.id.fotoGaleri);
            card = itemView.findViewById(R.id.cardView);


        }
    }
}


