package com.solusikatarak.solusikatarak.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.api.APIConfig;
import com.solusikatarak.solusikatarak.api.MySingleton;
import com.solusikatarak.solusikatarak.fragment.ListPasienFragment;
import com.solusikatarak.solusikatarak.helper.PasienAdapter;
import com.solusikatarak.solusikatarak.helper.RecyclerTouchListener;
import com.solusikatarak.solusikatarak.helper.SharedPrefManager;
import com.solusikatarak.solusikatarak.models.MetaPasien;
import com.solusikatarak.solusikatarak.models.MetaProvinsi;
import com.solusikatarak.solusikatarak.models.Pasien;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LihatPasienActivity extends AppCompatActivity {

Pasien pasien;
    ProgressDialog pd;

    public Pasien getPasien() {
        return pasien;
    }

    public void setPasien(Pasien pasien) {
        this.pasien = pasien;
    }

    TextView textTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_pasien);
        initView();


        changeColor(R.color.green2);
    }

    private void changeColor(int colorPrimary) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), colorPrimary));
        }

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorPrimary)));
    }

    private void initView() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("My Pasien");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textTitle = (TextView) findViewById(R.id.textTitle);

        ListPasienFragment homefragment = new ListPasienFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content, homefragment);
        fragmentTransaction.commit();
    }

    public void setTextTitle(String title){
        textTitle.setText(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
