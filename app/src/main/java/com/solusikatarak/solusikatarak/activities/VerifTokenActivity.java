package com.solusikatarak.solusikatarak.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.solusikatarak.solusikatarak.api.APIConfig;
import com.solusikatarak.solusikatarak.BuildConfig;
import com.solusikatarak.solusikatarak.api.Config;
import com.solusikatarak.solusikatarak.api.MySingleton;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.models.Meta;
import com.solusikatarak.solusikatarak.models.Relawan;
import com.solusikatarak.solusikatarak.helper.SharedPrefManager;

import java.util.HashMap;
import java.util.Map;

public class VerifTokenActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private EditText etToken1, etToken2, etToken3, etToken4, etToken5, etToken6;
    private Button btKirim;
    private TextView tvHandphone, tvCountDown;
    private String nama, handphone, token;
    private String inputToken;
    AlertDialog.Builder builder;
    Relawan relawan;
    Meta meta;
    StringBuilder sb = new StringBuilder();
    CountDownTimer countDownTimer;

    private static final String TAG = VerifTokenActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verif_token);

        initView();

        countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvCountDown.setText("00:"+millisUntilFinished / 1000);
                tvCountDown.setClickable(false);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                tvCountDown.setClickable(true);
                tvCountDown.setLinkTextColor(Color.WHITE);
                tvCountDown.setText("Kirim");
            }

        }.start();

        nama = getIntent().getExtras().getString("nama");
        handphone = getIntent().getExtras().getString("handphone");
        token = getIntent().getExtras().getString("token");

        sendToken();
        tvHandphone.setText(handphone);
    }

    private void sendToken() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConfig.SMS_SEND_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SMS GATEWAY STATUS", response);
//                try {
//                    JSONObject getObject = new JSONObject(response);
//                    String code = getObject.getString("code");
//                    String message = getObject.getString("message");
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("api_key", BuildConfig.SMS_API_KEY);
                params.put("api_secret", BuildConfig.SMS_API_SECRET);
                params.put("to", handphone);
                params.put("from", getResources().getString(R.string.app_name));
                params.put("text", getResources().getString(R.string.sendtoken) + " : " + token);
                return params;
            }
        };
        MySingleton.getmInstance(VerifTokenActivity.this).addToRequestQueue(stringRequest);
    }

    private void initView() {
        etToken1 = (EditText) findViewById(R.id.etToken1);
        etToken1.addTextChangedListener(this);
        etToken2 = (EditText) findViewById(R.id.etToken2);
        etToken2.addTextChangedListener(this);
        etToken3 = (EditText) findViewById(R.id.etToken3);
        etToken3.addTextChangedListener(this);
        etToken4 = (EditText) findViewById(R.id.etToken4);
        etToken4.addTextChangedListener(this);
        etToken5 = (EditText) findViewById(R.id.etToken5);
        etToken5.addTextChangedListener(this);
        etToken6 = (EditText) findViewById(R.id.etToken6);
        etToken6.addTextChangedListener(this);
        btKirim = (Button) findViewById(R.id.btKirim);
        builder = new AlertDialog.Builder(VerifTokenActivity.this);
        tvHandphone = (TextView) findViewById(R.id.tvHandphone);
        tvCountDown = (TextView) findViewById(R.id.tvCountdown);
        tvCountDown.setOnClickListener(this);

        btKirim.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btKirim:
                inputToken = etToken1.getText().toString() +
                        etToken2.getText().toString() +
                        etToken3.getText().toString() +
                        etToken4.getText().toString() +
                        etToken5.getText().toString() +
                        etToken6.getText().toString();
                Log.d("ISI TOKEN INPUT", inputToken);
                checkToken();
            case R.id.tvCountdown:
                sendToken();
                countDownTimer.start();
                break;
        }
    }

    private void checkToken() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConfig.CHECK_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("APIResponse", response);
                meta = new Gson().fromJson(response, Meta.class);
                //Log.d("RESPONSE META", meta.getRelawan().getNama() + " " + meta.getRelawan().getHandphone() + " " + meta.getRelawan().getToken());
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
                params.put("nama", nama);
                params.put("handphone", handphone);
                params.put("token", inputToken);
                return params;
            }
        };
        MySingleton.getmInstance(this).addToRequestQueue(stringRequest);
    }

    private void displayAlert(final String status) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (status.equals("input error")) {

                } else if (status.equalsIgnoreCase("Success")) {
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(meta.getRelawan());
                    Intent intent = new Intent(VerifTokenActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("nama", meta.getRelawan().getNama().toString());
                    bundle.putString("handphone", meta.getRelawan().getHandphone().toString());
                    intent.putExtras(bundle);

                    //firebase
                    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                    Log.e(TAG, refreshedToken);
                    storeRegIdInPref(refreshedToken);
                    sendRegistrationToServer(refreshedToken);

                    startActivity(intent);
                } else if (status.equals("fail")) {
//
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }
    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConfig.STORE_FIREBASEID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG,response);
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
                    params.put("firebasetoken", token);
                    return params;
                }
            };
            MySingleton.getmInstance(VerifTokenActivity.this).addToRequestQueue(stringRequest);
        }
        Log.e(TAG, "sendRegistrationToServer: " + token);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == etToken1.getEditableText()) {
            etToken2.requestFocus();
        } else if (editable == etToken2.getEditableText()) {
            etToken3.requestFocus();
        } else if (editable == etToken3.getEditableText()) {
            etToken4.requestFocus();
        } else if (editable == etToken4.getEditableText()) {
            etToken5.requestFocus();
        } else if (editable == etToken5.getEditableText()) {
            etToken6.requestFocus();
        } else if (editable == etToken6.getEditableText()) {

        }

    }
}
