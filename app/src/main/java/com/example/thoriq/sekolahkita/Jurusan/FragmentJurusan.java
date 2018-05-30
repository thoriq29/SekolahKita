package com.example.thoriq.sekolahkita.Jurusan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.thoriq.sekolahkita.Config.Server;
import com.example.thoriq.sekolahkita.Prestasi.AdapterPrestasi;
import com.example.thoriq.sekolahkita.Prestasi.ModelPrestasi;
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

public class FragmentJurusan extends Fragment {
    List<ModelJurusan> jurusanList;
    String id_sekolah;
    TextView Error;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jurusan,container,false);
        jurusanList = new ArrayList<>();
        id_sekolah = getActivity().getIntent().getStringExtra("id_sekolah");
        Error= (TextView) view.findViewById(R.id.tvErrorJur);
        listView = (ListView) view.findViewById(R.id.listJurusan);
        LoadKota();
        return view;
    }

    private void LoadKota() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.SERVER_URL+"List/JurusanSekolah.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("null")) {
                            Error.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.INVISIBLE);
                        }else
                        {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray titipanarray = obj.getJSONArray("Jurusan");

                                for (int i = 0; i < titipanarray.length(); i++) {
                                    JSONObject titipanObj = titipanarray.getJSONObject(i);
                                    ModelJurusan semua = new ModelJurusan(titipanObj.getString("id_jurusan"), titipanObj.getString("nama_jurusan"),titipanObj.getString("akreditasi"),titipanObj.getString("jumlah_kelas"));
                                    jurusanList.add(semua);
                                }
                                AdapterJurusan listSemua = new AdapterJurusan(jurusanList, getActivity().getApplicationContext());
                                listView.setAdapter(listSemua);
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
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
                Map<String,String> params = new HashMap<String,String>();
                params.put("id_sekolah",id_sekolah);
                return params;
            }
        };
        Handler.getInstance(getActivity()).addToRequestQue(stringRequest);
    }
}
