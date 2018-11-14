package com.solusikatarak.solusikatarak.activities;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.helper.SharedPrefManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    TextView textTitle, tvNama, tvNomorhp, tvSaldo, tvPasien, tvFee;
    CircleImageView ivProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initView();

        changeColor(R.color.orangesk);


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
        getSupportActionBar().setTitle("Profile Saya");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int jumlahpasien = getIntent().getIntExtra("JUMLAHPASIEN",0);
        int fee = getIntent().getIntExtra("FEE",0);
        int saldo = getIntent().getIntExtra("SALDO",0);

        textTitle = (TextView) findViewById(R.id.textTitle);
        tvNama = (TextView) findViewById(R.id.tvNama);
        tvNomorhp = (TextView) findViewById(R.id.tvNomorhp);
        tvSaldo = (TextView) findViewById(R.id.tvSaldo);
        tvPasien = (TextView) findViewById(R.id.tvPasien);
        tvFee = (TextView) findViewById(R.id.tvFee);
        ivProfile = (CircleImageView) findViewById(R.id.ivProfile);

        tvNama.setText(SharedPrefManager.getInstance(getApplicationContext()).getRelawan().getNama().toString());
        tvNomorhp.setText(SharedPrefManager.getInstance(getApplicationContext()).getRelawan().getHandphone().toString());
        Glide.with(getApplicationContext())
                .load(SharedPrefManager.getInstance(this).getRelawan().getKtprelawan().toString())
                .into(ivProfile);
        tvSaldo.setText("Rp."+saldo);
        tvFee.setText(fee+"/Pasien");
        tvPasien.setText(jumlahpasien+"");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
