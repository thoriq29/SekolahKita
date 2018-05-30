package com.example.thoriq.sekolahkita.Prestasi;

/**
 * Created by Thoriq on 04/03/2018.
 */

public class ModelPrestasi {
    String id,nama,tingkatan;

    public ModelPrestasi(String id, String nama, String tingkatan) {
        this.id = id;
        this.nama = nama;
        this.tingkatan = tingkatan;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getTingkatan() {
        return tingkatan;
    }
}
