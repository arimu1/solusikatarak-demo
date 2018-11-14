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
import com.solusikatarak.solusikatarak.fragment.ListDokterFragment;
import com.solusikatarak.solusikatarak.models.Dokter;
import com.solusikatarak.solusikatarak.models.Jadwal;

public class CheckJadwalActivity extends AppCompatActivity {
Dokter dokter;

    public Jadwal getJadwal() {
        return jadwal;
    }

    public void setJadwal(Jadwal jadwal) {
        this.jadwal = jadwal;
    }

    Jadwal jadwal;

    public Dokter getDokter() {
        return dokter;
    }

    public void setDokter(Dokter dokter) {
        this.dokter = dokter;
    }

    private TextView textTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_jadwal);

        initView();

        changeColor(R.color.dashboardrelawan);
    }

    private void changeColor(int colorPrimary) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), colorPrimary));
        }

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorPrimary)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void initView() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Check Jadwal Dokter");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textTitle = (TextView) findViewById(R.id.textTitle);

        ListDokterFragment listDokterfragment = new ListDokterFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content, listDokterfragment);
        fragmentTransaction.commit();
    }

    public void setTextTitle(String title){
        textTitle.setText(title);
    }
}
