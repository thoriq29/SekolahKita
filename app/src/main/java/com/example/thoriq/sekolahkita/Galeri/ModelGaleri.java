package com.example.thoriq.sekolahkita.Galeri;

/**
 * Created by Thoriq on 04/03/2018.
 */

public class ModelGaleri {
    String judul,image;

    public ModelGaleri(String judul, String image) {
        this.judul = judul;
        this.image = image;
    }

    public String getJudul() {
        return judul;
    }

    public String getImage() {
        return image;
    }
}
