package com.solusikatarak.solusikatarak.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Jadwal {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("hari")
    @Expose
    private String hari;
    @SerializedName("mulai")
    @Expose
    private String mulai;
    @SerializedName("selesai")
    @Expose
    private String selesai;
    @SerializedName("id_dokter")
    @Expose
    private int idDokter;

    public Jadwal(Integer id, String hari, String mulai, String selesai, int idDokter) {
        this.id = id;
        this.hari = hari;
        this.mulai = mulai;
        this.selesai = selesai;
        this.idDokter = idDokter;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getMulai() {
        return mulai;
    }

    public void setMulai(String mulai) {
        this.mulai = mulai;
    }

    public String getSelesai() {
        return selesai;
    }

    public void setSelesai(String selesai) {
        this.selesai = selesai;
    }

    public Integer getIdDokter() {
        return idDokter;
    }

    public void setIdDokter(Integer idDokter) {
        this.idDokter = idDokter;
    }

}
