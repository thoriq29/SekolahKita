package com.example.thoriq.sekolahkita.Fasilitas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.thoriq.sekolahkita.Config.Server;
import com.example.thoriq.sekolahkita.Guru.GuruAdapter;
import com.example.thoriq.sekolahkita.Guru.GuruModel;
import com.example.thoriq.sekolahkita.R;
import com.example.thoriq.sekolahkita.Volley.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thoriq on 04/03/2018.
 */

public class FasilitasFragment extends Fragment{

    RecyclerView recyclerView;
    List<ModelFasilitas> modelFasilitasList;
    TextView Error;
    String id_sekolah;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fasilitas,container,false);
        modelFasilitasList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyFasilitas);
        Error = (TextView) view.findViewById(R.id.tvErrorFasilitas);
        id_sekolah = getActivity().getIntent().getStringExtra("id_sekolah");
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager l = new LinearLayoutManager(getActivity());
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Error.setVisibility(View.INVISIBLE);
        load();
        return view;
    }
    public void load()
    {
        StringRequest s = new StringRequest(Request.Method.POST, Server.SERVER_URL+"List/FasilitasSekolah.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("null")) {
                            Error.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        }
                        else
                        {

                            Error.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            try
                            {
                                JSONArray j = new JSONArray(response);
                                for(int i=0;i<j.length(); i++)
                                {
                                    JSONObject jObj = j.getJSONObject(i);
                                    modelFasilitasList.add(new ModelFasilitas(
                                            jObj.getString("nama_fasilitas"),
                                            jObj.getString("foto")
                                    ));
                                }
                                AdapterFasilitas adapter = new AdapterFasilitas(getActivity(),modelFasilitasList);
                                recyclerView.setAdapter(adapter);
                            }catch (JSONException e)
                            {

                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("id_sekolah",id_sekolah);
                return params;
            }
        };
        Handler.getInstance(getActivity()).addToRequestQue(s);
    }
}
