package com.solusikatarak.solusikatarak.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.solusikatarak.solusikatarak.api.APIConfig;

public class Relawan {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("handphone")
    @Expose
    private String handphone;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("ktprelawan")
    @Expose
    private String ktprelawan;


    public Relawan(int id, String nama, String handphone, String token, String ktprelawan) {

        this.id = id;
        this.nama = nama;
        this.handphone = handphone;
        this.token = token;
        this.ktprelawan = ktprelawan;

    }

    public int getId() {
        return id;
    }

    public String getHandphone() {
        return handphone;
    }

    public String getNama() {
        return nama;
    }

    public String getToken() {
        return token;
    }
    public String getKtprelawan() {
        return ktprelawan;
    }

    public String getKtpUrlRelawan(){
        return APIConfig.BASE_URL+ ktprelawan.replace("\\","/");
    }

}
