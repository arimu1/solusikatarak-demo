package com.solusikatarak.solusikatarak.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.activities.TambahPasienActivity;
import com.solusikatarak.solusikatarak.api.APIConfig;
import com.solusikatarak.solusikatarak.api.MySingleton;
import com.solusikatarak.solusikatarak.models.MetaDesa;
import com.solusikatarak.solusikatarak.models.MetaKabupaten;
import com.solusikatarak.solusikatarak.models.MetaKecamatan;
import com.solusikatarak.solusikatarak.models.MetaProvinsi;
import com.solusikatarak.solusikatarak.models.Pasien;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondPasienFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText etJalan;
    private Spinner spProvinsi, spKabupaten, spKecamatan, spDesa;
    private RadioButton rbPria, rbWanita;
    private RadioGroup rbGender;
    private String Gender;
    private ImageButton ibBack, ibNext;
    MetaProvinsi metaProvinsi;
    MetaKabupaten metaKabupaten;
    MetaKecamatan metaKecamatan;
    MetaDesa metaDesa;
    ProgressDialog pd;
    private ArrayList<String> provinsi;

    TambahPasienActivity tambahPasienActivity;

    public SecondPasienFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_second_pasien, container, false);
        tambahPasienActivity = (TambahPasienActivity) getActivity();
        provinsi = new ArrayList<String>();

        etJalan = (EditText) rootView.findViewById(R.id.etJalan);

        spProvinsi = (Spinner) rootView.findViewById(R.id.spProvinsi);
        spProvinsi.setOnItemSelectedListener(this);
        spKabupaten = (Spinner) rootView.findViewById(R.id.spKabupaten);
        spKabupaten.setOnItemSelectedListener(this);
        spKecamatan = (Spinner) rootView.findViewById(R.id.spKecamatan);
        spKecamatan.setOnItemSelectedListener(this);
        spDesa = (Spinner) rootView.findViewById(R.id.spDesa);
        spDesa.setOnItemSelectedListener(this);

        rbGender = (RadioGroup) rootView.findViewById(R.id.rbGender);
        rbGender.setOnCheckedChangeListener(this);
        rbPria = (RadioButton) rootView.findViewById(R.id.rbPria);
        rbWanita = (RadioButton) rootView.findViewById(R.id.rbWanita);

        ibNext = (ImageButton) rootView.findViewById(R.id.ibNext);
        ibNext.setOnClickListener(this);
        ibBack = (ImageButton) rootView.findViewById(R.id.ibBack);
        ibBack.setOnClickListener(this);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading.........");
        pd.show();

        getDataProvinsi();
        return rootView;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rbPria:
                Gender = rbPria.getText().toString();
                break;
            case R.id.rbWanita:
                Gender = rbWanita.getText().toString();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibNext:
                if(isEmpty(etJalan)){
                    etJalan.setError("Jalan tidak boleh kosong");
                }
                else if(rbGender.getCheckedRadioButtonId()==-1){
                    Toast.makeText(getActivity(), "Jenis Kelamin harus diisi", Toast.LENGTH_LONG).show();
                }

                else {
                    tambahPasienActivity.getPasien().setJalan(etJalan.getText().toString());
                    tambahPasienActivity.getPasien().setProvinsi(spProvinsi.getSelectedItem().toString());
                    tambahPasienActivity.getPasien().setKabupaten(spKabupaten.getSelectedItem().toString());
                    tambahPasienActivity.getPasien().setKecamatan(spKecamatan.getSelectedItem().toString());
                    tambahPasienActivity.getPasien().setDesa(spDesa.getSelectedItem().toString());
                    tambahPasienActivity.getPasien().setGender(Gender);

                    MapFragment mapFragment = new MapFragment();
                    FragmentTransaction fm = getFragmentManager().beginTransaction();
                    fm.add(R.id.content, mapFragment);
                    fm.addToBackStack(null);
                    fm.commit();
                }
                break;
            case R.id.ibBack:
                getFragmentManager().popBackStack();
                break;
        }

    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        switch (adapterView.getId()) {
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

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getDataProvinsi() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConfig.DAERAH_INDONESIA_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                metaProvinsi = new Gson().fromJson(response, MetaProvinsi.class);
                Log.d("META PROVINSI", metaProvinsi.getSemuaprovinsi().get(1).getNama());
                for (int i = 0; i < metaProvinsi.getSemuaprovinsi().size(); i++) {
                    provinsi.add(metaProvinsi.getSemuaprovinsi().get(i).getNama());
                }
                spProvinsi.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, provinsi));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getmInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private String getIdProvinsi(int position) {
        String id = "";
        id = metaProvinsi.getSemuaprovinsi().get(position).getId();
        return id;
    }

    private void getDataKabupaten(String idProvinsi) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConfig.DAERAH_INDONESIA_API + "/" + idProvinsi + "/kabupaten", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<String> kabupaten = new ArrayList<String>();
                metaKabupaten = new Gson().fromJson(response, MetaKabupaten.class);
                Log.d("META KABUPATEN", metaKabupaten.getDaftarKabupaten().get(1).getNama());
                for (int i = 0; i < metaKabupaten.getDaftarKabupaten().size(); i++) {
                    kabupaten.add(metaKabupaten.getDaftarKabupaten().get(i).getNama());
                }
                spKabupaten.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, kabupaten));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getmInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private String getIdKabupaten(int position) {
        String id = "";
        id = metaKabupaten.getDaftarKabupaten().get(position).getId();
        return id;
    }

    private void getDataKecamatan(String idKabupaten) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConfig.DAERAH_INDONESIA_API + "/kabupaten/" + idKabupaten + "/kecamatan", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<String> kecamatan = new ArrayList<String>();
                metaKecamatan = new Gson().fromJson(response, MetaKecamatan.class);
                Log.d("META KECAMATAN", metaKecamatan.getDaftarKecamatan().get(1).getNama());
                for (int i = 0; i < metaKecamatan.getDaftarKecamatan().size(); i++) {
                    kecamatan.add(metaKecamatan.getDaftarKecamatan().get(i).getNama());
                }
                spKecamatan.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, kecamatan));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getmInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private String getIdKecamatan(int position) {
        String id = "";
        id = metaKecamatan.getDaftarKecamatan().get(position).getId();
        return id;
    }

    private void getDataDesa(String idKecamatan) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConfig.DAERAH_INDONESIA_API + "/kabupaten/kecamatan/" + idKecamatan + "/desa",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        ArrayList<String> desa = new ArrayList<String>();
                        metaDesa = new Gson().fromJson(response, MetaDesa.class);
                        Log.d("META DESA", metaDesa.getDaftarDesa().get(1).getNama());
                        for (int i = 0; i < metaDesa.getDaftarDesa().size(); i++) {
                            desa.add(metaDesa.getDaftarDesa().get(i).getNama());
                        }
                        spDesa.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, desa));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getmInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private String getIdDesa(int position) {
        String id = "";
        id = metaDesa.getDaftarDesa().get(position).getId();
        return id;
    }

}
