package com.example.thoriq.sekolahkita.ProfilSekolah;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.thoriq.sekolahkita.Config.Server;
import com.example.thoriq.sekolahkita.Eskul.EskulFragment;
import com.example.thoriq.sekolahkita.Fasilitas.FasilitasFragment;
import com.example.thoriq.sekolahkita.Galeri.FragmentGaleri;
import com.example.thoriq.sekolahkita.Guru.GuruFragment;
import com.example.thoriq.sekolahkita.Jurusan.FragmentJurusan;
import com.example.thoriq.sekolahkita.Prestasi.FragmentPrestasi;
import com.example.thoriq.sekolahkita.R;
import com.example.thoriq.sekolahkita.Volley.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileSekolah extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    String id_sekolah;
    TextView Nama,Alamat;
    ImageView FotoSekolah;
    String nama,alamat,foto,jenjang;
    NavigationView navigationView;
    Fragment fragment = null;
    Class fragmentClass;
    FragmentTransaction tx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_sekolah);
        init();

    }
    public void init()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        id_sekolah = getIntent().getStringExtra("id_sekolah");
        jenjang = getIntent().getStringExtra("jenjang");
        loadDataSekolah();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerL);
        navigationView = (NavigationView) findViewById(R.id.viewD);
        View hView =  navigationView.inflateHeaderView(R.layout.header);
        Nama = (TextView) hView.findViewById(R.id.namaSekolahHeader);
        Alamat = (TextView) hView.findViewById(R.id.alamatSekolahHeader);
        FotoSekolah = (ImageView) hView.findViewById(R.id.fotoSekolahHeader);
        toggle =new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawer(navigationView);
        if (jenjang.equals("SMK"))
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_profile);

        }else if (jenjang.equals("SMA"))
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_profile);

        }else if (jenjang.equals("SMP"))
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_profile_smp);
        }else
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_profile_smp);
        }
        tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.content, new DashbordFragment());
        tx.commit();
    }
    public void selectedItemDrawe(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.home:
                fragmentClass =DashbordFragment.class;
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.eskul:
                fragmentClass = EskulFragment.class;
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.peta:
                fragmentClass = PetaFragment.class;
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.fasilitas:
                fragmentClass = FasilitasFragment.class;
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.galeri:
                fragmentClass = FragmentGaleri.class;
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.prestasi:
                fragmentClass = FragmentPrestasi.class;
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.jurusan:
                fragmentClass = FragmentJurusan.class;
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.guru:
                fragmentClass = GuruFragment.class;
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            default:
                fragmentClass = DashbordFragment.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content,fragment).commit();
        item.setChecked(true);
        setTitle("Sekolah Kita");
        drawerLayout.closeDrawers();
    }
    private void setupDrawer(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectedItemDrawe(item);
                return true;
            }
        });
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
                            nama = data.getString("nama_sekolah");
                            alamat = data.getString("alamat");
                            foto = data.getString("foto_sekolah");

                            Alamat.setText(alamat);
                            Nama.setText(nama);
                            Glide.with(getApplicationContext()).load(Server.IMAGE_URL+foto).into(FotoSekolah);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
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
        Handler.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerL);
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
        {
            if (checkNavigationMenu() !=0)
            {
                navigationView.setCheckedItem(R.id.home);
                tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.content, new DashbordFragment());
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                tx.commit();
            }else
            {
                super.onBackPressed();
            }
        }

    }
    private int checkNavigationMenu()
    {
        Menu menu = navigationView.getMenu();
        for (int i =0; i<menu.size(); i++)
        {
            if (menu.getItem(i).isChecked())
                return i;
        }
        return -0;
    }
}
