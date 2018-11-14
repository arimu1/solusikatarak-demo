package com.solusikatarak.solusikatarak.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MetaKabupaten {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("daftar_kecamatan")
    @Expose
    private List<DaftarKabupaten> daftarKabupaten = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DaftarKabupaten> getDaftarKabupaten() {
        return daftarKabupaten;
    }

    public void setDaftarKabupaten(List<DaftarKabupaten> daftarKabupaten) {
        this.daftarKabupaten = daftarKabupaten;
    }
}
