package com.example.thoriq.sekolahkita.Guru;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.thoriq.sekolahkita.Adapter.AdapterSemuaSekolah;
import com.example.thoriq.sekolahkita.Config.Server;
import com.example.thoriq.sekolahkita.Model.SemuaSekolah;
import com.example.thoriq.sekolahkita.ProfilSekolah.ProfileSekolah;
import com.example.thoriq.sekolahkita.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Thoriq on 26/02/2018.
 */

public class GuruAdapter extends RecyclerView.Adapter<GuruAdapter.GuruAdapterViewHolder>{


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<GuruModel> guruModelList;

    //getting the context and product list with constructor
    public GuruAdapter(Context mCtx, List<GuruModel> guruModelList) {
        this.mCtx = mCtx;
        this.guruModelList = guruModelList;
    }



    @Override
    public GuruAdapter.GuruAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_guru, null);
        return new GuruAdapter.GuruAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GuruAdapter.GuruAdapterViewHolder holder, int position) {
        //getting the product of the specified position
        final GuruModel semuaGuru = guruModelList.get(position);

        //binding the data with the viewholder views
        holder.textViewId.setText(semuaGuru.getId_guru());
        holder.textViewNamaGuru.setText(semuaGuru.getNama_guru());
        holder.textViewNamaSeklah.setText(semuaGuru.getNama_sekolah());
        Glide.with(mCtx).load(Server.IMAGE_URL+semuaGuru.getFoto_guru()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mCtx);
                View mView = layoutInflaterAndroid.inflate(R.layout.foto_guru, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(mCtx);


                alertDialogBuilderUserInput.setView(mView);
                final ImageView foto = (ImageView) mView.findViewById(R.id.detailFotoGuru);
                alertDialogBuilderUserInput
                        .setCancelable(true);
                Glide.with(mCtx).load(Server.IMAGE_URL+semuaGuru.getFoto_guru()).into(foto);

                final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();

                alertDialogAndroid.show();
                foto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogAndroid.dismiss();
                    }
                });
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return guruModelList.size();
    }


    class GuruAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView textViewId, textViewNamaGuru, textViewNamaSeklah;
        CircleImageView imageView;
        CardView cardView;

        public GuruAdapterViewHolder(View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.idGuru);
            textViewNamaGuru = itemView.findViewById(R.id.namaGuru);
            textViewNamaSeklah = itemView.findViewById(R.id.guruSekolah);
            imageView = itemView.findViewById(R.id.fotoGuru);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}
