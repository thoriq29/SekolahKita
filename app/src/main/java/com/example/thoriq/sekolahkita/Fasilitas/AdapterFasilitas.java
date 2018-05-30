package com.example.thoriq.sekolahkita.Fasilitas;

import android.content.Context;
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
import com.example.thoriq.sekolahkita.Guru.GuruAdapter;
import com.example.thoriq.sekolahkita.Guru.GuruModel;
import com.example.thoriq.sekolahkita.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Thoriq on 04/03/2018.
 */

public class AdapterFasilitas  extends RecyclerView.Adapter<AdapterFasilitas.AdapterFasilitasViewHolder>{


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<ModelFasilitas> modelFasilitas;

    //getting the context and product list with constructor
    public AdapterFasilitas(Context mCtx, List<ModelFasilitas> guruModelList) {
        this.mCtx = mCtx;
        this.modelFasilitas = guruModelList;
    }



    @Override
    public AdapterFasilitas.AdapterFasilitasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_fasilitas, null);
        return new AdapterFasilitas.AdapterFasilitasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterFasilitas.AdapterFasilitasViewHolder holder, int position) {
        //getting the product of the specified position
        final ModelFasilitas semuaFasilitas = modelFasilitas.get(position);
        holder.nama.setText(semuaFasilitas.getNama_fasilitas());
        Glide.with(mCtx).load(Server.IMAGE_URL+semuaFasilitas.getFoto_fasilitas()).into(holder.foto);

        //binding the data with the viewholder views


    }


    @Override
    public int getItemCount() {
        return modelFasilitas.size();
    }


    class AdapterFasilitasViewHolder extends RecyclerView.ViewHolder {
        TextView nama;
        ImageView foto;
        CardView card;
        public AdapterFasilitasViewHolder(View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.namaFasilitas);
            foto = itemView.findViewById(R.id.fotoFasilitas);
            card = itemView.findViewById(R.id.cardView);


        }
    }
}

