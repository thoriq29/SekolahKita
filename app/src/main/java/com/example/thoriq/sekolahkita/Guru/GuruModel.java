package com.example.thoriq.sekolahkita.Guru;

/**
 * Created by Thoriq on 26/02/2018.
 */

public class GuruModel {
    String id_guru,nama_guru,nama_sekolah,foto_guru;

    public GuruModel(String id_guru, String nama_guru, String nama_sekolah, String foto_guru) {
        this.id_guru = id_guru;
        this.nama_guru = nama_guru;
        this.nama_sekolah = nama_sekolah;
        this.foto_guru = foto_guru;
    }

    public String getId_guru() {
        return id_guru;
    }

    public String getNama_guru() {
        return nama_guru;
    }

    public String getNama_sekolah() {
        return nama_sekolah;
    }

    public String getFoto_guru() {
        return foto_guru;
    }
}
