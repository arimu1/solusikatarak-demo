package com.solusikatarak.solusikatarak.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DaftarKecamatan {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("id_kabupaten")
    @Expose
    private String idKabupaten;
    @SerializedName("nama")
    @Expose
    private String nama;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdKabupaten() {
        return idKabupaten;
    }

    public void setIdKabupaten(String idKabupaten) {
        this.idKabupaten = idKabupaten;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
