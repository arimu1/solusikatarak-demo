package com.solusikatarak.solusikatarak.fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.api.APIConfig;
import com.solusikatarak.solusikatarak.api.MySingleton;
import com.solusikatarak.solusikatarak.api.VolleyMultipartRequest;
import com.solusikatarak.solusikatarak.helper.SharedPrefManager;
import com.solusikatarak.solusikatarak.models.MetaDesa;
import com.solusikatarak.solusikatarak.models.MetaJadwal;
import com.solusikatarak.solusikatarak.models.MetaKabupaten;
import com.solusikatarak.solusikatarak.models.MetaKecamatan;
import com.solusikatarak.solusikatarak.models.MetaProvinsi;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class TambahPasienFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {
    private ImageButton fotoktp, fotobpjs, btSave;
    private EditText etNama, etNik, etJalan, etTtl, etNomorhp;
    private RadioButton rbPria,rbWanita;
    private RadioGroup rbGender;
    private Spinner spProvinsi, spKabupaten, spKecamatan, spDesa, spJadwal;
    private final int IMG_KTP = 0, IMG_BPJS = 1;
    private Bitmap bitmapKtp, bitmapBpjs;
    private String kelamin, part_image_ktp, part_image_bpjs;
    AlertDialog.Builder builder;
    private ImageView ivKtp, ivBpjs;
    MetaProvinsi metaProvinsi;
    MetaKabupaten metaKabupaten;
    MetaKecamatan metaKecamatan;
    MetaDesa metaDesa;
    MetaJadwal metaJadwal;
    ProgressDialog pd;
    Uri ktpPath, bpjsPath;
    private ArrayList<String> provinsi;

    public TambahPasienFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tambah_pasien, container, false);
        //ArrayList
        provinsi = new ArrayList<String>();
        //ImageButton
        fotoktp = (ImageButton) rootView.findViewById(R.id.fotoktp);
        fotoktp.setOnClickListener(this);
        fotobpjs = (ImageButton) rootView.findViewById(R.id.fotobpjs);
        fotobpjs.setOnClickListener(this);
        btSave = (ImageButton) rootView.findViewById(R.id.btSave);
        btSave.setOnClickListener(this);
        //Edit Text
        etNama = (EditText) rootView.findViewById(R.id.etNama);
        etNik = (EditText) rootView.findViewById(R.id.etNik);
        etJalan = (EditText) rootView.findViewById(R.id.etJalan);
        etTtl = (EditText) rootView.findViewById(R.id.etTtl);
        etNomorhp = (EditText) rootView.findViewById(R.id.etNomorhp);

        builder = new AlertDialog.Builder(getActivity());
        //ImageView
        ivKtp = (ImageView) rootView.findViewById(R.id.ivKtp);
        ivBpjs = (ImageView) rootView.findViewById(R.id.ivBpjs);
        //Spinner
        spProvinsi = (Spinner) rootView.findViewById(R.id.spProvinsi);
        spProvinsi.setOnItemSelectedListener(this);
        spKabupaten = (Spinner) rootView.findViewById(R.id.spKabupaten);
        spKabupaten.setOnItemSelectedListener(this);
        spKecamatan = (Spinner) rootView.findViewById(R.id.spKecamatan);
        spKecamatan.setOnItemSelectedListener(this);
        spDesa = (Spinner) rootView.findViewById(R.id.spDesa);
        spDesa.setOnItemSelectedListener(this);
        spJadwal = (Spinner) rootView.findViewById(R.id.spJadwal);
        spJadwal.setOnItemSelectedListener(this);
        //Radio
        rbGender = (RadioGroup) rootView.findViewById(R.id.rbGender);
        rbGender.setOnCheckedChangeListener(this);
        rbPria = (RadioButton) rootView.findViewById(R.id.rbPria);
        rbWanita = (RadioButton) rootView.findViewById(R.id.rbWanita);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading.........");


        getActivity().setTitle("Tambah Pasien");
        getDataJadwal();
        getDataProvinsi();
        return rootView;
    }

    private void getDataProvinsi() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConfig.DAERAH_INDONESIA_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                metaProvinsi = new Gson().fromJson(response, MetaProvinsi.class);
                Log.d("META PROVINSI", metaProvinsi.getSemuaprovinsi().get(1).getNama());
                for (int i=0; i<metaProvinsi.getSemuaprovinsi().size();i++){
                    provinsi.add(metaProvinsi.getSemuaprovinsi().get(i).getNama());
                }
                spProvinsi.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,provinsi));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getmInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private String getIdProvinsi(int position){
        String id="";
        id = metaProvinsi.getSemuaprovinsi().get(position).getId();
        return id;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        switch (adapterView.getId()){
            case R.id.spProvinsi:
                getDataKabupaten(getIdProvinsi(position));
                break;
            case R.id.spKabupaten:
                getDataKecamatan(getIdKabupaten(position));
                break;
            case R.id.spKecamatan:
                getDataDesa(getIdKecamatan(position));
                break;
            case R.id.spDesa:
                break;
            case R.id.spJadwal:
                break;
        }
    }

    private void getDataJadwal() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConfig.JADWAL_API , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            ArrayList<String> jadwal = new ArrayList<>();
            metaJadwal = new Gson().fromJson(response, MetaJadwal.class);
            Log.d("META JADWAL", metaJadwal.getJadwal().get(0).getHari());
            for (int i=0;i<metaJadwal.getJadwal().size();i++){
                jadwal.add(metaJadwal.getJadwal().get(i).getHari()+" "+metaJadwal.getJadwal().get(i).getMulai()+"-"+metaJadwal.getJadwal().get(i).getSelesai());
            }
            spJadwal.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, jadwal));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getmInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void getDataDesa(String idKecamatan) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConfig.DAERAH_INDONESIA_API+"/kabupaten/kecamatan/"+idKecamatan+"/desa",
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<String> desa = new ArrayList<String>();
                metaDesa = new Gson().fromJson(response, MetaDesa.class);
                Log.d("META DESA", metaDesa.getDaftarDesa().get(1).getNama());
                for (int i = 0; i<metaDesa.getDaftarDesa().size(); i++){
                    desa.add(metaDesa.getDaftarDesa().get(i).getNama());
                }
                spDesa.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,desa));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getmInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private String getIdDesa(int position){
        String id="";
        id = metaDesa.getDaftarDesa().get(position).getId();
        return id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getDataKabupaten(String idProvinsi) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConfig.DAERAH_INDONESIA_API+"/"+idProvinsi+"/kabupaten", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<String> kabupaten = new ArrayList<String>();
                metaKabupaten = new Gson().fromJson(response, MetaKabupaten.class);
                Log.d("META KABUPATEN", metaKabupaten.getDaftarKabupaten().get(1).getNama());
                for (int i = 0; i<metaKabupaten.getDaftarKabupaten().size(); i++){
                    kabupaten.add(metaKabupaten.getDaftarKabupaten().get(i).getNama());
                }
                spKabupaten.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,kabupaten));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getmInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private String getIdKabupaten(int position){
        String id="";
        id = metaKabupaten.getDaftarKabupaten().get(position).getId();
        return id;
    }

    private void getDataKecamatan(String idKabupaten) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConfig.DAERAH_INDONESIA_API+"/kabupaten/"+idKabupaten+"/kecamatan", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<String> kecamatan = new ArrayList<String>();
                metaKecamatan = new Gson().fromJson(response, MetaKecamatan.class);
                Log.d("META KECAMATAN", metaKecamatan.getDaftarKecamatan().get(1).getNama());
                for (int i = 0; i<metaKecamatan.getDaftarKecamatan().size(); i++){
                    kecamatan.add(metaKecamatan.getDaftarKecamatan().get(i).getNama());
                }
                spKecamatan.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,kecamatan));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getmInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private String getIdKecamatan(int position){
        String id="";
        id = metaKecamatan.getDaftarKecamatan().get(position).getId();
        return id;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fotoktp:
//                Intent photoKtpIntent = new Intent(Intent.ACTION_PICK);
//                photoKtpIntent.setType("image/*");
//                photoKtpIntent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(photoKtpIntent, IMG_KTP );
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"open gallery"), IMG_KTP);
                break;
            case R.id.fotobpjs:
                Intent photoBpjsIntent = new Intent(Intent.ACTION_PICK);
                photoBpjsIntent.setType("image/*");
                photoBpjsIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(photoBpjsIntent, IMG_BPJS );
                break;

            case R.id.btSave:
                savePasien();
                break;
        }
    }

    private void savePasien() {
        pd.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIConfig.ADD_PASIEN_API,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            pd.dismiss();
                            JSONObject getObject = new JSONObject(new String(response.data));
                            String code = getObject.getString("code");
                            String message = getObject.getString("message");
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String id = SharedPrefManager.getInstance(getActivity()).getRelawan().getId()+"";
                Map<String, String> params = new HashMap<>();
                params.put("nama", etNama.getText().toString());
                params.put("nik", etNik.getText().toString());
                params.put("jalan", etJalan.getText().toString());
                params.put("provinsi", spProvinsi.getSelectedItem().toString());
                params.put("kabupaten", spKabupaten.getSelectedItem().toString());
                params.put("kecamatan", spKecamatan.getSelectedItem().toString());
                params.put("desa", spDesa.getSelectedItem().toString());
                params.put("jeniskelamin", kelamin);
                params.put("ttl", etTtl.getText().toString());
                params.put("tanggaloperasi", spJadwal.getSelectedItem().toString());
                params.put("nomorhp", etNomorhp.getText().toString());
                params.put("id_relawan", id);
                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("fotoktp", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmapKtp)));
                params.put("fotobpjs", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmapBpjs)));
                return params;
            }

        };
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
    }

    private void displayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (code.equals("input error")) {

                } else if (code.equals("Success")) {

                } else if (code.equals("Fail")) {

                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_KTP && resultCode == RESULT_OK && data!=null)
        {
            ktpPath = data.getData();
            try {
                bitmapKtp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), ktpPath);
                Glide.with(getActivity()).load( ktpPath).into(ivKtp);
                ivKtp.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            try {
//                bitmapKtp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), ktpPath);
//                Glide.with(getActivity()).load( ktpPath).into(ivKtp);
//                //ivKtp.setImageBitmap(bitmapKtp);
//                ivKtp.setVisibility(View.VISIBLE);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        else  if(requestCode == IMG_BPJS && resultCode == RESULT_OK && data!=null)
        {
            bpjsPath = data.getData();
            try {
                bitmapBpjs = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), bpjsPath);
                Glide.with(getActivity()).load( bpjsPath).into(ivBpjs);
                ivBpjs.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            try {
//                bitmapBpjs = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), bpjsPath);
//                Glide.with(getActivity()).load( bpjsPath).into(ivBpjs);
//                //ivBpjs.setImageBitmap(bitmapBpjs);
//                ivBpjs.setVisibility(View.VISIBLE);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        Log.d("FOTO",Base64.encodeToString(imgBytes, Base64.DEFAULT));
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.rbPria:
                kelamin = rbPria.getText().toString();
                break;
            case R.id.rbWanita:
                kelamin = rbWanita.getText().toString();
                break;

        }
    }
}
