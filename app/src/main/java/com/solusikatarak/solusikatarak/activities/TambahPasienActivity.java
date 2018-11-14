package com.solusikatarak.solusikatarak.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.fragment.FirstPasienFragment;
import com.solusikatarak.solusikatarak.fragment.MapFragment;
import com.solusikatarak.solusikatarak.models.Pasien;

public class TambahPasienActivity extends AppCompatActivity {
    Pasien pasien = new Pasien();
    MapFragment mapFragment;
    TextView textTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pasien);

        initView();

        changeColor(R.color.dashboardpasien);

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
        getSupportActionBar().setTitle("Data Pasien");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textTitle = (TextView) findViewById(R.id.textTitle);

        FirstPasienFragment firstPasienFragment = new FirstPasienFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content, firstPasienFragment);
        fragmentTransaction.commit();
//        mapFragment = new MapFragment();
//        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
//        fm.add(R.id.content, mapFragment);
//        fm.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public Pasien getPasien() {
        return pasien;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MapFragment.MY_PERMISSIONS_REQUEST_LOCATION){
            mapFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
