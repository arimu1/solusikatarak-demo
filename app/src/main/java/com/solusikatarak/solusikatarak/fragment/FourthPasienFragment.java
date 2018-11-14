package com.solusikatarak.solusikatarak.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import com.solusikatarak.solusikatarak.activities.TambahPasienActivity;
import com.solusikatarak.solusikatarak.api.APIConfig;
import com.solusikatarak.solusikatarak.api.MySingleton;
import com.solusikatarak.solusikatarak.api.VolleyMultipartRequest;
import com.solusikatarak.solusikatarak.helper.SharedPrefManager;
import com.solusikatarak.solusikatarak.models.MetaJadwal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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
public class FourthPasienFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Spinner spJadwal;
    private ImageButton ibSave, ibBack;
    private TextView tvDate;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    MetaJadwal metaJadwal;
    int id_dokter;
    String dayOfWeek, tanggaloperasi, formattanggal;

    int year, month, day;

    TambahPasienActivity tambahPasienActivity;

    public FourthPasienFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fourth_pasien, container, false);
        tambahPasienActivity = (TambahPasienActivity) getActivity();
        spJadwal = (Spinner) rootView.findViewById(R.id.spJadwal);
        spJadwal.setOnItemSelectedListener(this);

        ibSave = (ImageButton) rootView.findViewById(R.id.ibSave);
        ibSave.setOnClickListener(this);
        ibBack = (ImageButton) rootView.findViewById(R.id.ibBack);
        ibBack.setOnClickListener(this);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        tvDate.setOnClickListener(this);

        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        getDataJadwal();

        builder = new AlertDialog.Builder(getActivity());

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading.........");
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibSave:
                tambahPasienActivity.getPasien().setJadwal(spJadwal.getSelectedItem().toString());
                id_dokter = metaJadwal.getJadwal().get(spJadwal.getSelectedItemPosition()).getIdDokter();
                tambahPasienActivity.getPasien().setId_dokter(id_dokter);
                savePasien();
                break;

            case R.id.ibBack:
                getFragmentManager().popBackStack();
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
            ibSave.setVisibility(View.VISIBLE);
        }
        else if(metaJadwal.getJadwal().get(spJadwal.getSelectedItemPosition()).getHari().equalsIgnoreCase("Selasa") && dayOfWeek.equalsIgnoreCase("Tuesday")){
            dayOfWeek = "Selasa";
            Toast.makeText(getContext(), dayOfWeek + ", " + day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
            ibSave.setVisibility(View.VISIBLE);
        }
        else if(metaJadwal.getJadwal().get(spJadwal.getSelectedItemPosition()).getHari().equalsIgnoreCase("Rabu") && dayOfWeek.equalsIgnoreCase("Wednesday")){
            dayOfWeek = "Rabu";
            Toast.makeText(getContext(), dayOfWeek + ", " + day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
            ibSave.setVisibility(View.VISIBLE);
        }
        else if(metaJadwal.getJadwal().get(spJadwal.getSelectedItemPosition()).getHari().equalsIgnoreCase("Kamis") && dayOfWeek.equalsIgnoreCase("Thursday")){
            dayOfWeek = "Kamis";
            Toast.makeText(getContext(), dayOfWeek + ", " + day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
            ibSave.setVisibility(View.VISIBLE);
        }
        else if(metaJadwal.getJadwal().get(spJadwal.getSelectedItemPosition()).getHari().equalsIgnoreCase("Jumat") && dayOfWeek.equalsIgnoreCase("Friday")){
            dayOfWeek = "Jumat";
            Toast.makeText(getContext(), dayOfWeek + ", " + day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
            ibSave.setVisibility(View.VISIBLE);
        }
        else if(metaJadwal.getJadwal().get(spJadwal.getSelectedItemPosition()).getHari().equalsIgnoreCase("Sabtu") && dayOfWeek.equalsIgnoreCase("Saturday")){
            dayOfWeek = "Sabtu";
            Toast.makeText(getContext(), dayOfWeek + ", " + day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
            ibSave.setVisibility(View.VISIBLE);
        }
        else if(metaJadwal.getJadwal().get(spJadwal.getSelectedItemPosition()).getHari().equalsIgnoreCase("Minggu") && dayOfWeek.equalsIgnoreCase("Sunday")){
            dayOfWeek = "Minggu";
            Toast.makeText(getContext(), dayOfWeek + ", " + day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
            ibSave.setVisibility(View.VISIBLE);
        }
        else{
            Toast.makeText(getContext(), "Jadwal Tidak Sesuai", Toast.LENGTH_LONG).show();
            ibSave.setVisibility(View.INVISIBLE);
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
                            Log.d("Response Save Pasien", message);
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
                String latitude = tambahPasienActivity.getPasien().getLatitude()+"";
                String longitude = tambahPasienActivity.getPasien().getLongitude()+"";
                Map<String, String> params = new HashMap<>();
                params.put("nama", tambahPasienActivity.getPasien().getNama());
                params.put("nik", tambahPasienActivity.getPasien().getNIK());
                params.put("jalan", tambahPasienActivity.getPasien().getJalan());
                params.put("provinsi", tambahPasienActivity.getPasien().getProvinsi());
                params.put("kabupaten", tambahPasienActivity.getPasien().getKabupaten());
                params.put("kecamatan", tambahPasienActivity.getPasien().getKecamatan());
                params.put("desa", tambahPasienActivity.getPasien().getDesa());
                params.put("jeniskelamin", tambahPasienActivity.getPasien().getGender());
                params.put("ttl", tambahPasienActivity.getPasien().getTTL());
                params.put("tanggaloperasi", tambahPasienActivity.getPasien().getJadwal()+", "+tanggaloperasi);
                params.put("nomorhp", tambahPasienActivity.getPasien().getNomorhp());
                params.put("id_relawan", id);
                params.put("id_dokter", tambahPasienActivity.getPasien().getId_dokter()+"");
                params.put("latitude",latitude);
                params.put("longitude",longitude);
                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("fotoktp", new DataPart(imagename + ".png", getFileDataFromDrawable(tambahPasienActivity.getPasien().getBitmapKtp())));
                params.put("fotobpjs", new DataPart(imagename + ".png", getFileDataFromDrawable(tambahPasienActivity.getPasien().getBitmapBpjs())));
                return params;
            }

        };
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
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
    private void displayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (code.equals("input error")) {

                } else if (code.equals("Success")) {
                    SummaryPasienFragment summaryPasienFragment = new SummaryPasienFragment();
                    FragmentTransaction fm = getFragmentManager().beginTransaction();
                    fm.add(R.id.content, summaryPasienFragment);
                    fm.commit();

                } else if (code.equals("Fail")) {

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.spJadwal:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
