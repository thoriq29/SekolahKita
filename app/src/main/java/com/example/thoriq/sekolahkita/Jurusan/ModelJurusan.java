package com.example.thoriq.sekolahkita.Jurusan;

/**
 * Created by Thoriq on 04/03/2018.
 */

public class ModelJurusan {
    String id,nama,akre,kelas;

    public ModelJurusan(String id, String nama, String akre, String kelas) {
        this.id = id;
        this.nama = nama;
        this.akre = akre;
        this.kelas = kelas;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getAkre() {
        return akre;
    }

    public String getKelas() {
        return kelas;
    }
}
