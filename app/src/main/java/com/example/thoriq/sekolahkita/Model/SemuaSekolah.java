package com.example.thoriq.sekolahkita.Model;

/**
 * Created by Thoriq on 21/02/2018.
 */

public class SemuaSekolah {
    private int id;
    private String namasekolah,alamat,akreditasi,image,jenjang;

    public SemuaSekolah(int id, String namasekolah, String alamat, String akreditasi, String image,String jenjang) {
        this.id = id;
        this.namasekolah = namasekolah;
        this.alamat = alamat;
        this.akreditasi = akreditasi;
        this.image = image;
        this.jenjang = jenjang;
    }

    public int getId() {
        return id;
    }

    public String getNamasekolah() {
        return namasekolah;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getAkreditasi() {
        return akreditasi;
    }

    public String getImage() {
        return image;
    }

    public String getJenjang() {
        return jenjang;
    }
}
