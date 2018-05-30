package com.example.thoriq.sekolahkita.ProfilSekolah;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.thoriq.sekolahkita.Adapter.AdapterKota;
import com.example.thoriq.sekolahkita.Adapter.AdapterMisi;
import com.example.thoriq.sekolahkita.CariKota;
import com.example.thoriq.sekolahkita.Config.Server;
import com.example.thoriq.sekolahkita.Model.Kota;
import com.example.thoriq.sekolahkita.Model.ModelMisi;
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
 * Created by Thoriq on 02/03/2018.
 */

public class DashbordFragment extends Fragment {

    String id_sekolah,lat,lng,nama_sekolah,alamat,web,akre,logo,email,fax,telpon;
    TextView Nama,Alamat,Web,Akre,Email,Fax,Telpon,Siswa,Guru,Jurusan,Kepsek,NIP;
    ImageView Logo,Foto;
    List<ModelMisi> misiList;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashbord,container,false);
        id_sekolah = getActivity().getIntent().getStringExtra("id_sekolah");
        Nama = (TextView) view.findViewById(R.id.namaSekolahProfil);
        Alamat = (TextView) view.findViewById(R.id.alamat);
        Web = (TextView) view.findViewById(R.id.web);
        Akre = (TextView) view.findViewById(R.id.Akreditasi);
        Logo = (ImageView) view.findViewById(R.id.logo);
        Kepsek = (TextView) view.findViewById(R.id.namaKepsek);
        Email = (TextView) view.findViewById(R.id.emailSek);
        Fax = (TextView) view.findViewById(R.id.fax);
        Telpon = (TextView) view.findViewById(R.id.telpon);
        Siswa = (TextView) view.findViewById(R.id.jmlhSiswa);
        Guru = (TextView) view.findViewById(R.id.jmlhGuru);
        Jurusan = (TextView) view.findViewById(R.id.jmlhJurusan);
        Foto = (ImageView) view.findViewById(R.id.fotokepSek);
        NIP = (TextView) view.findViewById(R.id.NIP);
    loadKepSek();
    loadDataSekolah();
        return view;
    }
    public void loadMisi()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.SERVER_URL+"List/misi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray titipanarray = obj.getJSONArray("semua_misi");

                            for (int i = 0; i < titipanarray.length(); i++) {
                                JSONObject titipanObj = titipanarray.getJSONObject(i);
                                ModelMisi semua = new ModelMisi(titipanObj.getString("nama_misi"));
                                misiList.add(semua);
                            }
                            AdapterMisi listSemua = new AdapterMisi(misiList, getActivity());
                            listView.setAdapter(listSemua);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("id_sekolah",id_sekolah);
                return params;
            }
        };
        Handler.getInstance(getActivity().getApplicationContext()).addToRequestQue(stringRequest);
    }
    private void loadDataSekolah()
    {

        StringRequest stringRequest =  new StringRequest(Request.Method.POST, Server.SERVER_URL + "List/SekolahQ.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("sekolahQ");
                            JSONObject data = jsonArray.getJSONObject(0);
                            nama_sekolah = data.getString("nama_sekolah");
                            alamat = data.getString("alamat");
                            logo = data.getString("foto_sekolah");
                            web = data.getString("website");
                            akre =  data.getString("akreditasi");
                            Alamat.setText(alamat);
                            Nama.setText(nama_sekolah);
                            Email.setText(data.getString("email"));
                            Telpon.setText(data.getString("no_telp"));
                            Fax.setText(data.getString("no_fax"));
                            Glide.with(getActivity()).load(Server.IMAGE_URL +logo).error(R.mipmap.ic_launcher).into(Logo);
                            Akre.setText(akre);
                            Web.setText(web);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("id_sekolah",id_sekolah);
                return params;
            }
        };
        Handler.getInstance(getActivity()).addToRequestQue(stringRequest);
    }
    private void loadKepSek()
    {
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, Server.SERVER_URL + "List/kepSek.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("kepSek");
                            JSONObject data = jsonArray.getJSONObject(0);

                            Guru.setText(data.getString("jmlh_guru"));
                            Siswa.setText(data.getString("jmlh_siswa"));
                            Glide.with(getActivity()).load(Server.IMAGE_URL+data.getString("foto")).into(Foto);
                            Jurusan.setText(data.getString("jmlh_jurusan"));
                            Kepsek.setText(data.getString("nama_kepsek"));
                            NIP.setText("NIP : "+data.getString("nip_kepsek"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        })
        {
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
