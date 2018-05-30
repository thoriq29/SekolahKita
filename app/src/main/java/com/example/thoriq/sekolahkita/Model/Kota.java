package com.example.thoriq.sekolahkita.Model;

/**
 * Created by Thoriq on 16/02/2018.
 */

public class Kota {
    String idprovinsi,idkotakab,namakotakab,namaprovinsi,foto;

    public Kota(String idprovinsi, String idkotakab, String namakotakab, String namaprovinsi, String foto) {
        this.idprovinsi = idprovinsi;
        this.idkotakab = idkotakab;
        this.namakotakab = namakotakab;
        this.namaprovinsi = namaprovinsi;
        this.foto = foto;
    }

    public String getIdprovinsi() {
        return idprovinsi;
    }

    public String getIdkotakab() {
        return idkotakab;
    }

    public String getNamakotakab() {
        return namakotakab;
    }

    public String getNamaprovinsi() {
        return namaprovinsi;
    }

    public String getFoto() {
        return foto;
    }
}
