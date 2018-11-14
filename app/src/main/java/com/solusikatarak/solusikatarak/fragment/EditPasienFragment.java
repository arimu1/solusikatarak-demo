package com.solusikatarak.solusikatarak.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.activities.LihatPasienActivity;
import com.solusikatarak.solusikatarak.activities.RegisterActivity;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPasienFragment extends Fragment implements AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    LihatPasienActivity lihatPasienActivity;
    private TextView tvJadwal, tvDate;;
    private EditText etNama, etNik, etNomorhp,etJalan, etTtl;
    private Spinner spProvinsi, spKabupaten, spKecamatan, spDesa, spJadwal;
    private CardView viewJadwal,viewDate;
    private RadioButton rbPria, rbWanita;
    private RadioGroup rbGender;
    private SwitchCompat swWilayah, swJadwal;
    private String kelamin;
    private LinearLayout viewWilayah;
    private FloatingActionButton floatSave;
    String dayOfWeek, tanggaloperasi, formattanggal;
    int year, month, day;
    MetaProvinsi metaProvinsi;
    MetaKabupaten metaKabupaten;
    MetaKecamatan metaKecamatan;
    MetaDesa metaDesa;
    MetaJadwal metaJadwal;
    HashMap<Integer,String> idJadwal = new HashMap<Integer, String>();
    ProgressDialog pd;
    private ArrayList<String> provinsi;

    private boolean load = false;
    public EditPasienFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_pasien, container, false);

        lihatPasienActivity = (LihatPasienActivity) getActivity();

        lihatPasienActivity.setTextTitle("Edit Pasien");
        provinsi = new ArrayList<String>();

        etNama = (EditText) rootView.findViewById(R.id.etNama);
        etNama.setText(lihatPasienActivity.getPasien().getNama());
        etNik = (EditText) rootView.findViewById(R.id.etNik);
        etNik.setText(lihatPasienActivity.getPasien().getNIK());
        etNomorhp = (EditText) rootView.findViewById(R.id.etNomorhp);
        etNomorhp.setText(lihatPasienActivity.getPasien().getNomorhp());
        etJalan = (EditText) rootView.findViewById(R.id.etJalan);
        etJalan.setText(lihatPasienActivity.getPasien().getJalan());
        etTtl = (EditText) rootView.findViewById(R.id.etTtl);
        etTtl.setText(lihatPasienActivity.getPasien().getTTL());

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
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        tvDate.setOnClickListener(this);

        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        swWilayah = (SwitchCompat) rootView.findViewById(R.id.swWilayah);
        swWilayah.setOnCheckedChangeListener(this);

        floatSave = (FloatingActionButton) rootView.findViewById(R.id.floatSave);
        floatSave.setOnClickListener(this);

        viewWilayah = (LinearLayout) rootView.findViewById(R.id.viewWilayah);

        viewJadwal = (CardView) rootView.findViewById(R.id.viewJadwal);
        viewDate = (CardView) rootView.findViewById(R.id.viewDate);
        tvJadwal = (TextView) rootView.findViewById(R.id.tvJadwal);
        tvJadwal.setText(lihatPasienActivity.getPasien().getJadwal());
        swJadwal = (SwitchCompat) rootView.findViewById(R.id.swJadwal);
        swJadwal.setOnCheckedChangeListener(this);

        rbGender = (RadioGroup) rootView.findViewById(R.id.rbGender);
        rbGender.setOnCheckedChangeListener(this);
        rbPria = (RadioButton) rootView.findViewById(R.id.rbPria);
        rbWanita = (RadioButton) rootView.findViewById(R.id.rbWanita);

        if(lihatPasienActivity.getPasien().getGender().equalsIgnoreCase("pria")){
            rbGender.check(R.id.rbPria);
        }
        else if(lihatPasienActivity.getPasien().getGender().equalsIgnoreCase("wanita")){ rbGender.check(R.id.rbWanita);}

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading.........");

        getDataProvinsi();
        getDataJadwal();

        return rootView;
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
                        load = true;
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

    private void getDataJadwal() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConfig.JADWAL_API , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                load=true;
                ArrayList<String> jadwal = new ArrayList<>();
                metaJadwal = new Gson().fromJson(response, MetaJadwal.class);
                Log.d("META JADWAL", metaJadwal.getJadwal().get(0).getHari());
                for (int i=0;i<metaJadwal.getJadwal().size();i++){
                    idJadwal.put(i, String.valueOf(metaJadwal.getJadwal().get(i).getId()));
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

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rbPria:
                kelamin = rbPria.getText().toString();
                break;
            case R.id.rbWanita:
                kelamin = rbWanita.getText().toString();
                break;
        }
    }

    private void savePasien() {
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConfig.UPDATE_PASIEN_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pd.dismiss();
                            JSONObject getObject = new JSONObject(response);
                            Log.d("Response", response);
                            getActivity().finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Log.d("Response Error", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String id = SharedPrefManager.getInstance(getActivity()).getRelawan().getId()+"";
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(lihatPasienActivity.getPasien().getId()));
                params.put("nama", etNama.getText().toString());
                params.put("nik", etNik.getText().toString());
                params.put("nomorhp", etNomorhp.getText().toString());
                params.put("jalan", etJalan.getText().toString());
                if(swWilayah.isChecked()) {
                    params.put("provinsi", spProvinsi.getSelectedItem().toString());
                    params.put("kabupaten", spKabupaten.getSelectedItem().toString());
                    params.put("kecamatan", spKecamatan.getSelectedItem().toString());
                    params.put("desa", spDesa.getSelectedItem().toString());
                }
                params.put("jeniskelamin", kelamin);
                params.put("ttl", etTtl.getText().toString());
                if(swJadwal.isChecked()){
                params.put("tanggaloperasi", spJadwal.getSelectedItem().toString()+", "+tanggaloperasi);
                }
                params.put("id_dokter", String.valueOf(metaJadwal.getJadwal().get(spJadwal.getSelectedItemPosition()).getIdDokter()));
                params.put("id_relawan", id);
                return params;
            }

        };
        MySingleton.getmInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.floatSave:
                savePasien();


//                Intent intent = new Intent(getActivity(),LihatPasienActivity.class);
//                startActivity(intent);
//                lihatPasienActivity.getPasien().setJadwal(spJadwal.getSelectedItem().toString()+", "+tanggaloperasi);
//                getFragmentManager().popBackStack();

                break;

            case R.id.tvDate:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String dateString = String.format("%d-%d-%d", year, (month + 1), day);
                        Date date = null;
                        try {
                            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
                        formattanggal = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        verifyDate();


                        month = month + 1;

                        tvDate.setText(dayOfWeek + ", " + day + "-" + month + "-" + year);
                        tanggaloperasi = formattanggal;
                        Log.d("tanggal operasi", spJadwal.getSelectedItem().toString()+", "+tanggaloperasi);

                    }
                }, year, month, day);
                datePickerDialog.show();


                break;
        }

    }
    public void verifyDate(){
        if(metaJadwal.getJadwal().get(spJadwal.getSelectedItemPosition()).getHari().equalsIgnoreCase("Senin") && dayOfWeek.equalsIgnoreCase("Monday")){
            dayOfWeek = "Senin";
            Toast.makeText(getContext(), dayOfWeek + ", " + day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
            floatSave.setVisibility(View.VISIBLE);
        }
        else if(metaJadwal.getJadwal().get(spJadwal.getSelectedItemPosition()).getHari().equalsIgnoreCase("Selasa") && dayOfWeek.equalsIgnoreCase("Tuesday")){
            dayOfWeek = "Selasa";
            Toast.makeText(getContext(), dayOfWeek + ", " + day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
            floatSave.setVisibility(View.VISIBLE);
        }
        else if(metaJadwal.getJadwal().get(spJadwal.getSelectedItemPosition()).getHari().equalsIgnoreCase("Rabu") && dayOfWeek.equalsIgnoreCase("Wednesday")){
            dayOfWeek = "Rabu";
            Toast.makeText(getContext(), dayOfWeek + ", " + day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
            floatSave.setVisibility(View.VISIBLE);
        }
        else if(metaJadwal.getJadwal().get(spJadwal.getSelectedItemPosition()).getHari().equalsIgnoreCase("Kamis") && dayOfWeek.equalsIgnoreCase("Thursday")){
            dayOfWeek = "Kamis";
            Toast.makeText(getContext(), dayOfWeek + ", " + day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
            floatSave.setVisibility(View.VISIBLE);
        }
        else if(metaJadwal.getJadwal().get(spJadwal.getSelectedItemPosition()).getHari().equalsIgnoreCase("Jumat") && dayOfWeek.equalsIgnoreCase("Friday")){
            dayOfWeek = "Jumat";
            Toast.makeText(getContext(), dayOfWeek + ", " + day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
            floatSave.setVisibility(View.VISIBLE);
        }
        else if(metaJadwal.getJadwal().get(spJadwal.getSelectedItemPosition()).getHari().equalsIgnoreCase("Sabtu") && dayOfWeek.equalsIgnoreCase("Saturday")){
            dayOfWeek = "Sabtu";
            Toast.makeText(getContext(), dayOfWeek + ", " + day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
            floatSave.setVisibility(View.VISIBLE);
        }
        else if(metaJadwal.getJadwal().get(spJadwal.getSelectedItemPosition()).getHari().equalsIgnoreCase("Minggu") && dayOfWeek.equalsIgnoreCase("Sunday")){
            dayOfWeek = "Minggu";
            Toast.makeText(getContext(), dayOfWeek + ", " + day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
            floatSave.setVisibility(View.VISIBLE);
        }
        else{
            Toast.makeText(getContext(), "Jadwal Tidak Sesuai", Toast.LENGTH_LONG).show();
            floatSave.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()){
            case R.id.swWilayah:
                if (!isChecked){
                viewWilayah.setVisibility(View.GONE);
                }
                else{
                    if(!load) {
                        pd.show();
                    }
                    viewWilayah.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.swJadwal:
                if (!isChecked){
                    viewJadwal.setVisibility(View.GONE);
                    viewDate.setVisibility(View.VISIBLE);
                }
                else{
                    if(!load) {
                        pd.show();
                    }
                    viewJadwal.setVisibility(View.VISIBLE);
                    viewDate.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
