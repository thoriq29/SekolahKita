package com.example.thoriq.sekolahkita.Guru;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.thoriq.sekolahkita.Config.Server;
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
 * Created by Thoriq on 23/02/2018.
 */

public class GuruFragment extends Fragment {

    RecyclerView recyclerView;
    TextView Error;
    List<GuruModel> guruModelList;
    String id_sekolah;
    EditText search;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guru,container,false);
        id_sekolah = getActivity().getIntent().getStringExtra("id_sekolah");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyGuru);
        guruModelList= new ArrayList<>();
        Error = (TextView) view.findViewById(R.id.tvErrorGuru);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager l = new LinearLayoutManager(getActivity());
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        Error.setVisibility(View.INVISIBLE);
        loadData();
        return view;
    }
    private void loadData()
    {
        StringRequest s = new StringRequest(Request.Method.POST, Server.SERVER_URL+"List/GuruSekolah.php",
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
                                    guruModelList.add(new GuruModel(
                                            jObj.getString("id_guru"),
                                            jObj.getString("nama_guru"),
                                            jObj.getString("nama_sekolah"),
                                            jObj.getString("foto_guru")
                                    ));
                                }
                                GuruAdapter adapter = new GuruAdapter(getActivity(),guruModelList);
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
