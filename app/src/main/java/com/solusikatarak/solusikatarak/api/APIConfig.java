package com.solusikatarak.solusikatarak.api;

import com.solusikatarak.solusikatarak.BuildConfig;

public class APIConfig {
    public static final String BASE_IP = "10.0.3.2";
//    192.168.100.54
    public static final String BASE_URL = "https://solusikatarak.000webhostapp.com/";
//    public static final String BASE_URL = "http://"+BASE_IP+"/solusikatarak/public/";
    public static final String REG_API = BASE_URL+"/api/relawan"; //POST
    public static final String LOGIN_API = BASE_URL+"/api/login"; //POST
    public static final String SMS_SEND_API = "https://rest.nexmo.com/sms/json"; //POST
    public static final String CHECK_TOKEN = BASE_URL+"/api/token"; //POST
    public static final String ADD_PASIEN_API = BASE_URL+"/api/pasien"; //POST
    public static final String DAERAH_INDONESIA_API = "http://dev.farizdotid.com/api/daerahindonesia/provinsi"; //GET
    public static final String JADWAL_API = BASE_URL+"/api/jadwal"; //GET
    public static final String VIEW_PASIEN_API = BASE_URL+"/api/lihatpasien"; //POST
    public static final String VIEW_DOKTER_API = BASE_URL+"/api/lihatdokter"; //GET
    public static final String MAP_STATIC = "https://maps.googleapis.com/maps/api/staticmap?size=600x300&zoom=15&markers=";
    public static final String UPDATE_FOTO_PASIEN_API = BASE_URL+"/api/pasien/updatefoto"; //POST
    public static final String UPDATE_PASIEN_API = BASE_URL+"/api/pasien/update"; //POST
    public static final String DELETE_PASIEN_API = BASE_URL+"/api/pasien/delete"; //POST
    public static final String JADWAL_DOKTER_API = BASE_URL+"/api/jadwaldokter"; //POST

    public static final String CHECK_SALDO_API = BASE_URL+"/api/saldorelawan"; //POST

    public static final String GOOGLE_MAP_KEY = BuildConfig.GOOGLE_MAP_KEY;

    public static final String DASHBOARD_API = BASE_URL+"/api/dashboard";
    public static final String STORE_FIREBASEID = BASE_URL+"/api/firebasetoken"; //POST
    public static final String KONFIRMASI_PEMBAYARAN_API = BASE_URL+"/api/konfirmasipembayaran"; //POST
}
