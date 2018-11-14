package com.solusikatarak.solusikatarak.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MetaPasien {
    @SerializedName("pasien")
    @Expose
    private List<Pasien> pasien = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Pasien> getPasien() {
        return pasien;
    }

    public void setPasien(List<Pasien> pasien) {
        this.pasien = pasien;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
