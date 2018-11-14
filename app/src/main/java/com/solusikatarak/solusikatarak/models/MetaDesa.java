package com.solusikatarak.solusikatarak.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MetaDesa {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("daftar_desa")
    @Expose
    private List<DaftarDesa> daftarDesa = null;

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

    public List<DaftarDesa> getDaftarDesa() {
        return daftarDesa;
    }

    public void setDaftarDesa(List<DaftarDesa> daftarDesa) {
        this.daftarDesa = daftarDesa;
    }
}
