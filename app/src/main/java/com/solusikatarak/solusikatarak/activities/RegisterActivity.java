package com.solusikatarak.solusikatarak.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.solusikatarak.solusikatarak.api.APIConfig;
import com.solusikatarak.solusikatarak.api.MySingleton;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.api.VolleyMultipartRequest;
import com.solusikatarak.solusikatarak.helper.SharedPrefManager;
import com.solusikatarak.solusikatarak.models.Relawan;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etNama, etKodenegara, etNomorhp;
    private Button btMasuk, btDaftar, btFotoKtp;
    private String nama, handphone, token;
    private ImageView ivKtp;
    AlertDialog.Builder builder;
    private ProgressBar pbRegister;
    private final int IMG_KTP = 0;
    private Bitmap bitmapKtp;
    Uri ktpPath;
    private Relawan relawan;

    //String reg_url = "http://192.168.100.11/solusikatarak/public/api/relawan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        initView();
        changeColor(R.color.backcolor);

    }

    private void changeColor(int colorPrimary) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), colorPrimary));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void initView() {
        etNama = (EditText) findViewById(R.id.etNama);
        etKodenegara = (EditText) findViewById(R.id.etKodenegara);
        etNomorhp = (EditText) findViewById(R.id.etNomorhp);
        btMasuk = (Button) findViewById(R.id.btMasuk);
        btDaftar = (Button) findViewById(R.id.btDaftar);
        btFotoKtp = (Button) findViewById(R.id.btFotoKtp);
        pbRegister = (ProgressBar) findViewById(R.id.pbRegister);
        ivKtp = (ImageView) findViewById(R.id.ivKtp);

        builder = new AlertDialog.Builder(RegisterActivity.this);

        btMasuk.setOnClickListener(this);
        btDaftar.setOnClickListener(this);
        btFotoKtp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btMasuk:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btDaftar:
                pbRegister.setVisibility(View.VISIBLE);
                nama = etNama.getText().toString();
                handphone = etKodenegara.getText().toString() + etNomorhp.getText().toString();
                token = " " + (int) Math.round(Math.random() * (999999 - 000000 + 1) + 000000);
                Log.d("Random Token", token);
                if (nama.equals("") || etNomorhp.equals("")) {
                    builder.setTitle("Error Message");
                    builder.setMessage("semua field harus diisi");
                    displayAlert("input error");
                } else if (etNomorhp.getText().length() < 9) {
                    builder.setTitle("Error Message");
                    builder.setMessage("Nomor HP tidak boleh kurang dari 9 karakter");
                    displayAlert("input error");
                }
                registerUser();
                break;
            case R.id.btFotoKtp:
                Intent photoKtpIntent = new Intent();
                photoKtpIntent.setType("image/*");
                photoKtpIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(photoKtpIntent, IMG_KTP);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_KTP && resultCode == RESULT_OK && data != null) {
            ktpPath = data.getData();

            bitmapKtp = decodeBitmap(getApplicationContext(), ktpPath, 4);

            Glide.with(this).load(ktpPath).into(ivKtp);
            ivKtp.setVisibility(View.VISIBLE);
        }
    }

    private static Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                fileDescriptor.getFileDescriptor(), null, options);

        Log.d(TAG, options.inSampleSize + " sample method bitmap ... "
                + actuallyUsableBitmap.getWidth() + " " + actuallyUsableBitmap.getHeight());

        return actuallyUsableBitmap;
    }

    private void registerUser() {
        Log.d("RESPONSE MESSAGE", String.valueOf(bitmapKtp));
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConfig.REG_API, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
////                    Log.d("APIResponse", response);
//                try {
//                    JSONObject getObject = new JSONObject(response);
//                    String code = getObject.getString("code");
//                    String message = getObject.getString("message");
//                    builder.setTitle("Server Response....");
//                    builder.setMessage(message);
//                    displayAlert(code);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json");
//                params.put("nama", nama);
//                params.put("handphone", handphone);
//                params.put("token", token);
//                return params;
//            }
//        };
//        MySingleton.getmInstance(RegisterActivity.this).addToRequestQueue(stringRequest);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIConfig.REG_API,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject getObject = new JSONObject(new String(response.data));
                            String code = getObject.getString("code");
                            String message = getObject.getString("message");
                            Log.d("RESPONSE MESSAGE", message);
                            builder.setTitle("Server Response....");
                            builder.setMessage(message);
                            displayAlert(code);
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
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("handphone", handphone);
                params.put("token", token);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("ktprelawan", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmapKtp)));
                return params;
            }

        };
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    private void displayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (code.equals("input error")) {

                } else if (code.equals("Success")) {
                    Intent intent = new Intent(RegisterActivity.this, VerifTokenActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("nama", nama);
                    bundle.putString("handphone", handphone);
                    bundle.putString("token", token);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (code.equals("Fail")) {
                    etNama.setText("");
                    etNomorhp.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}

