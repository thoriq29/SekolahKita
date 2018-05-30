package com.example.thoriq.sekolahkita.Fasilitas;

/**
 * Created by Thoriq on 04/03/2018.
 */

public class ModelFasilitas {
    String nama_fasilitas,foto_fasilitas;

    public ModelFasilitas(String nama_fasilitas, String foto_fasilitas) {
        this.nama_fasilitas = nama_fasilitas;
        this.foto_fasilitas = foto_fasilitas;

    }

    public String getNama_fasilitas() {
        return nama_fasilitas;
    }

    public String getFoto_fasilitas() {
        return foto_fasilitas;
    }
}
