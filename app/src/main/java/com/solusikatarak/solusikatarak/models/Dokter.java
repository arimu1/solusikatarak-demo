package com.solusikatarak.solusikatarak.models;

import com.solusikatarak.solusikatarak.api.APIConfig;

public class Dokter {
    private int id, role;
    private String nama, email, fotodokter;

    public Dokter(int id, int role, String nama, String email, String fotodokter) {
        this.id = id;
        this.role = role;
        this.nama = nama;
        this.email = email;
        this.fotodokter = fotodokter;
    }

    public String getFotoDokterUrl() {
        return APIConfig.BASE_URL+ fotodokter.replace("\\","/");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFotodokter() {
        return fotodokter;
    }

    public void setFotodokter(String fotodokter) {
        this.fotodokter = fotodokter;
    }

}
