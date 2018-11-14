package com.solusikatarak.solusikatarak.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.api.APIConfig;
import com.solusikatarak.solusikatarak.api.Config;
import com.solusikatarak.solusikatarak.api.MySingleton;
import com.solusikatarak.solusikatarak.fragment.DashboardFragment;
import com.solusikatarak.solusikatarak.fragment.HomeFragment;
import com.solusikatarak.solusikatarak.helper.SharedPrefManager;
import com.solusikatarak.solusikatarak.utils.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private View headerLayout;
    private int jumlahpasien, fee, saldo;
    ProgressDialog pd;
    //private CardView cvAddpasien;

    private TextView tvNamauser, tvHandphoneuser, textTitle, tvSaldo;
    private CircleImageView fotoKtpRelawan;

    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, SharedPrefManager.getInstance(getApplicationContext()).getRelawan().getId()+"");
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
        //Firebase
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    checkFirebaseToken();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Info: " + message, Toast.LENGTH_LONG).show();

//                    txtMessage.setText(message);
                }
            }
        };
        checkFirebaseToken();
        //

        setContentView(R.layout.activity_main);

        pd = new ProgressDialog(this);
        pd.setMessage("Loading.........");
        pd.show();

        initView();

        Log.d("INI ADALAH NAMA USER", SharedPrefManager.getInstance(this).getRelawan().getNama());

        tvNamauser.setText(SharedPrefManager.getInstance(this).getRelawan().getNama().toString());
        tvHandphoneuser.setText(SharedPrefManager.getInstance(this).getRelawan().getHandphone().toString());

        saldoData();
        changeColor(R.color.colorPrimary);
    }
    private void initView() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        textTitle = (TextView) findViewById(R.id.textTitle);

//        cvAddpasien = (CardView)findViewById(R.id.cvAddpasien);
//        cvAddpasien.setOnClickListener(this);
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout = navigationView.getHeaderView(0);

        tvNamauser = headerLayout.findViewById(R.id.tvNamauser);
        tvHandphoneuser = headerLayout.findViewById(R.id.tvHandphoneuser);
        tvSaldo = headerLayout.findViewById(R.id.tvSaldo);
        fotoKtpRelawan = headerLayout.findViewById(R.id.fotoKtpRelawan);
        Glide.with(getApplicationContext())
                .load(SharedPrefManager.getInstance(this).getRelawan().getKtprelawan().toString())
                .into(fotoKtpRelawan);
        Log.e(TAG, SharedPrefManager.getInstance(this).getRelawan().getKtprelawan().toString());

        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home");

        DashboardFragment dashboardFragment = new DashboardFragment();
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        fm.add(R.id.dashboard, dashboardFragment);
        fm.commit();

        HomeFragment homefragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content, homefragment);
        fragmentTransaction.commit();
    }

    //Firebase Tes
    private void checkFirebaseToken() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e(TAG, "Firebase reg id: " + regId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }
    //

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void changeColor(int colorPrimary) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), colorPrimary));
        }

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorPrimary)));
    }

    private void saldoData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConfig.CHECK_SALDO_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                    Log.d("APIResponse", response);
                try {
                    pd.dismiss();
                    JSONObject getObject = new JSONObject(response);
                    jumlahpasien = getObject.getInt("jumlahpasien");
                    fee = getObject.getInt("fee");
                    saldo = getObject.getInt("saldo");
                    Log.d("COUNT", String.valueOf(getObject.getInt("saldo")));
                    tvSaldo.setText("Rp."+String.valueOf(getObject.getInt("saldo"))); //sementara

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String id = SharedPrefManager.getInstance(getApplicationContext()).getRelawan().getId()+"";
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                return params;
            }
        };
        MySingleton.getmInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                deleteFirebaseToken();
                break;

            case R.id.profile:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("JUMLAHPASIEN", jumlahpasien);
                intent.putExtra("FEE", fee);
                intent.putExtra("SALDO", saldo);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void deleteFirebaseToken() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConfig.STORE_FIREBASEID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,response);
                logout();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String id = SharedPrefManager.getInstance(getApplicationContext()).getRelawan().getId()+"";
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("firebasetoken", "Logout");
                return params;
            }
        };
        MySingleton.getmInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }

    private void logout() {
        SharedPrefManager.getInstance(this).logout();
        finish();
        startActivity(new Intent(this, LoginActivity.class));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cvAddpasien:
//                TambahPasienFragment addpasienfragment = new TambahPasienFragment();
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.add(R.id.content, addpasienfragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
        }
    }
}

