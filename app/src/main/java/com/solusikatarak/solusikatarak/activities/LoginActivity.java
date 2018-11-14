package com.solusikatarak.solusikatarak.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.solusikatarak.solusikatarak.api.APIConfig;
import com.solusikatarak.solusikatarak.api.MySingleton;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.models.Meta;
import com.solusikatarak.solusikatarak.models.Relawan;
import com.solusikatarak.solusikatarak.helper.SharedPrefManager;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etNomorhp, etKodenegara;
    private ProgressBar pbLogin;
    private Button btMasuk, btDaftar;
    private String nama, token,handphone;
    AlertDialog.Builder builder;
    Relawan relawan;
    Meta meta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        initView();
        changeColor(R.color.backcolor);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void changeColor(int colorPrimary) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), colorPrimary));
        }
    }

    private void initView() {
        etNomorhp = (EditText) findViewById(R.id.etNomorhp);
        etNomorhp.requestFocus();
        etKodenegara = (EditText)findViewById(R.id.etKodenegara);
        btMasuk = (Button) findViewById(R.id.btMasuk);
        btDaftar = (Button) findViewById(R.id.btDaftar);
        pbLogin = (ProgressBar)findViewById(R.id.pbLogin);
        builder = new AlertDialog.Builder(LoginActivity.this);

        btMasuk.setOnClickListener(this);
        btDaftar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btMasuk:
                pbLogin.setVisibility(View.VISIBLE);
                handphone = etKodenegara.getText().toString() + etNomorhp.getText().toString();
                token = " " + (int) Math.round(Math.random() * (999999 - 000000 + 1) + 000000);

                if (etNomorhp.equals("")) {
                    builder.setTitle("Error Message");
                    builder.setMessage("semua field harus diisi");
                    displayAlert("input error");
                } else if (etNomorhp.getText().length() < 9) {
                    builder.setTitle("Error Message");
                    builder.setMessage("Nomor HP tidak boleh kurang dari 9 karakter");
                    displayAlert("input error");
                }
                loginUser();
                break;
            case R.id.btDaftar:
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void loginUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConfig.LOGIN_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbLogin.setVisibility(View.INVISIBLE);
                meta = new Gson().fromJson(response, Meta.class);
                String status = meta.getStatus();
                String message = meta.getMessage();
                builder.setTitle("Server Response....");
                builder.setMessage(message);
                displayAlert(status);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("handphone", handphone);
                params.put("token", token);
                return params;
            }
        };
        MySingleton.getmInstance(LoginActivity.this).addToRequestQueue(stringRequest);
    }

    private void displayAlert(final String status) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (status.equals("input error")) {

                }else if (status.equalsIgnoreCase("Success")) {
                    Log.d("NAMA RELAWAN",meta.getRelawan().getNama().toString());
                    Intent intent = new Intent(LoginActivity.this, VerifTokenActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("nama", meta.getRelawan().getNama().toString());
                    bundle.putString("handphone", meta.getRelawan().getHandphone().toString());
                    bundle.putString("token", token);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if (status.equalsIgnoreCase("Fail")) {
                    etNomorhp.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
