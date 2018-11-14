package com.solusikatarak.solusikatarak.models;

import android.graphics.Bitmap;

import com.solusikatarak.solusikatarak.api.APIConfig;

import java.io.ByteArrayOutputStream;

public class Pasien {
    private  int id, status, id_dokter;

    public int getId_dokter() {
        return id_dokter;
    }

    public void setId_dokter(int id_dokter) {
        this.id_dokter = id_dokter;
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String Nama, NIK, Jalan, Provinsi, Kabupaten, Kecamatan, Desa, Gender, TTL, jadwal, nomorhp, ktpurl, bpjsurl, mapurl;
    private Bitmap bitmapKtp, bitmapBpjs;
    private double latitude, longitude;

    public String getKtpurl() {
        return APIConfig.BASE_URL+ktpurl.replace("\\","/");
    }
//+"?" + (int) Math.round(Math.random() * (999999 - 000000 + 1) + 000000)
    public String getMapurl() {
        return APIConfig.MAP_STATIC+getLatitude()+","+getLongitude()+"&key="+APIConfig.GOOGLE_MAP_KEY;
    }

    public String getBpjsurl() {
        return APIConfig.BASE_URL+bpjsurl.replace("\\","/");
    }

    public String getAlamat(){
        return Jalan+", "+Provinsi+", "+Kabupaten+", "+Kecamatan+", "+Desa;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Pasien(){}
    public Pasien(int id, String nama, String NIK, String jalan, String provinsi,
                  String kabupaten, String kecamatan, String desa,
                  String gender, String TTL, String jadwal, String nomorhp, String ktpurl, String bpjsurl, double latitude, double longitude, int status) {
        this.id = id;
        Nama = nama;
        this.NIK = NIK;
        Jalan = jalan;
        Provinsi = provinsi;
        Kabupaten = kabupaten;
        Kecamatan = kecamatan;
        Desa = desa;
        Gender = gender;
        this.TTL = TTL;
        this.jadwal = jadwal;
        this.nomorhp = nomorhp;
        this.ktpurl = ktpurl;
        this.bpjsurl = bpjsurl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public void setNIK(String NIK) {
        this.NIK = NIK;
    }


    public void setProvinsi(String provinsi) {
        Provinsi = provinsi;
    }

    public void setKabupaten(String kabupaten) {
        Kabupaten = kabupaten;
    }

    public void setKecamatan(String kecamatan) {
        Kecamatan = kecamatan;
    }

    public void setDesa(String desa) {
        Desa = desa;
    }


    public void setTTL(String TTL) {
        this.TTL = TTL;
    }

    public void setJadwal(String jadwal) {
        this.jadwal = jadwal;
    }

    public void setNomorhp(String nomorhp) {
        this.nomorhp = nomorhp;
    }

    public void setBitmapKtp(Bitmap bitmapKtp) {
        this.bitmapKtp = bitmapKtp;
    }

    public void setBitmapBpjs(Bitmap bitmapBpjs) {
        this.bitmapBpjs = bitmapBpjs;
    }

    public String getNIK() {
        return NIK;
    }

    public String getJalan() {
        return Jalan;
    }

    public void setJalan(String jalan) {
        Jalan = jalan;
    }

    public String getProvinsi() {
        return Provinsi;
    }

    public String getKabupaten() {
        return Kabupaten;
    }

    public String getKecamatan() {
        return Kecamatan;
    }

    public String getDesa() {
        return Desa;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getTTL() {
        return TTL;
    }

    public String getJadwal() {
        return jadwal;
    }

    public String getNomorhp() {
        return nomorhp;
    }

    public Bitmap getBitmapKtp() {
        return bitmapKtp;
    }

    public Bitmap getBitmapBpjs() {
        return bitmapBpjs;
    }


}
