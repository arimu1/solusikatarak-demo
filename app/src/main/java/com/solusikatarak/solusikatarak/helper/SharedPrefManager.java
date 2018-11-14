package com.solusikatarak.solusikatarak.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.solusikatarak.solusikatarak.api.Config;
import com.solusikatarak.solusikatarak.models.Meta;
import com.solusikatarak.solusikatarak.models.Relawan;

import java.io.IOException;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "auth";

    private static final String KEY_ID = "id";
    private static final String KEY_NAMA = "nama";
    private static final String KEY_HANDPHONE = "handphone";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_KTP_RELAWAN = "ktprelawan";

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(Relawan relawan) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, relawan.getId());
        editor.putString(KEY_NAMA, relawan.getNama());
        editor.putString(KEY_HANDPHONE, relawan.getHandphone());
        editor.putString(KEY_TOKEN, relawan.getToken());
        editor.putString(KEY_KTP_RELAWAN, relawan.getKtpUrlRelawan());
        editor.apply();
        return true;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_HANDPHONE, null) != null)
            return true;
        return false;
    }

    public Relawan getRelawan() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Relawan(
                sharedPreferences.getInt(KEY_ID, 0),
                sharedPreferences.getString(KEY_NAMA, null),
                sharedPreferences.getString(KEY_HANDPHONE, null),
                sharedPreferences.getString(KEY_TOKEN, null),
                sharedPreferences.getString(KEY_KTP_RELAWAN, null)
        );
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

            SharedPreferences pref = mCtx.getSharedPreferences(Config.SHARED_PREF, 0);
            SharedPreferences.Editor editFirebaseId = pref.edit();
            editFirebaseId.clear();
            editFirebaseId.apply();
        try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
        } catch (IOException e) {
            e.printStackTrace();
        }

        editor.clear();
        editor.apply();
        return true;
    }
}
