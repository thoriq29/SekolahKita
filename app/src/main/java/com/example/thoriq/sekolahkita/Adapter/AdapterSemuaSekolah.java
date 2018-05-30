package com.example.thoriq.sekolahkita.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thoriq.sekolahkita.Config.Server;
import com.example.thoriq.sekolahkita.Model.SemuaSekolah;
import com.example.thoriq.sekolahkita.R;
import com.example.thoriq.sekolahkita.ProfilSekolah.ProfileSekolah;

import java.util.List;

/**
 * Created by Thoriq on 21/02/2018.
 */

public class AdapterSemuaSekolah extends RecyclerView.Adapter<AdapterSemuaSekolah.SemuaSekolahViewHolder>{


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<SemuaSekolah> sekolahList;

    //getting the context and product list with constructor
    public AdapterSemuaSekolah(Context mCtx, List<SemuaSekolah> sekolahList) {
        this.mCtx = mCtx;
        this.sekolahList = sekolahList;
    }



    @Override
    public SemuaSekolahViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_sekolah, null);
        return new SemuaSekolahViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SemuaSekolahViewHolder holder, int position) {
        //getting the product of the specified position
        SemuaSekolah semuaSekolah = sekolahList.get(position);

        //binding the data with the viewholder views
        holder.textViewId.setText(String.valueOf(semuaSekolah.getId()));
        holder.textViewNamaSekolah.setText(semuaSekolah.getNamasekolah());
        holder.textViewAlamat.setText(semuaSekolah.getAlamat());
        holder.textViewAkre.setText("Akreditasi : "+semuaSekolah.getAkreditasi());
        holder.textJenjang.setText(semuaSekolah.getJenjang());
        Glide.with(mCtx)
                .load(Server.IMAGE_URL+semuaSekolah.getImage())
                .into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(view.getContext(), ProfileSekolah.class);
                intent.putExtra("id_sekolah",holder.textViewId.getText().toString());
                intent.putExtra("jenjang",holder.textJenjang.getText().toString());
                ((Activity)mCtx).finish();
                view.getContext().startActivity(intent);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(view.getContext(), ProfileSekolah.class);
                intent.putExtra("id_sekolah",holder.textViewId.getText().toString());
                intent.putExtra("jenjang",holder.textJenjang.getText().toString());
                ((Activity)mCtx).finish();
                view.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return sekolahList.size();
    }


    class SemuaSekolahViewHolder extends RecyclerView.ViewHolder {

        TextView textViewId, textViewNamaSekolah, textViewAlamat, textViewAkre,textJenjang;
        ImageView imageView;
        CardView cardView;

        public SemuaSekolahViewHolder(View itemView) {
            super(itemView);

            textViewId = itemView.findViewById(R.id.idSekolah);
            textViewNamaSekolah = itemView.findViewById(R.id.namaSekolahProfil);
            textViewAlamat = itemView.findViewById(R.id.alamatSekolah);
            textViewAkre =itemView.findViewById(R.id.tvAkre);
            imageView = itemView.findViewById(R.id.fotoSekolah);
            cardView = itemView.findViewById(R.id.cardView);
            textJenjang = itemView.findViewById(R.id.tvJenjang);
        }
    }
}
