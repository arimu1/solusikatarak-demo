package com.solusikatarak.solusikatarak.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MetaKecamatan {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("daftar_kecamatan")
    @Expose
    private List<DaftarKecamatan> daftarKecamatan = null;

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

    public List<DaftarKecamatan> getDaftarKecamatan() {
        return daftarKecamatan;
    }

    public void setDaftarKecamatan(List<DaftarKecamatan> daftarKecamatan) {
        this.daftarKecamatan = daftarKecamatan;
    }
}
