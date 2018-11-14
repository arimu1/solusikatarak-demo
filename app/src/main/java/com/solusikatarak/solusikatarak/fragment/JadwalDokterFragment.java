package com.solusikatarak.solusikatarak.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.activities.CheckJadwalActivity;
import com.solusikatarak.solusikatarak.api.APIConfig;
import com.solusikatarak.solusikatarak.api.MySingleton;
import com.solusikatarak.solusikatarak.helper.JadwalAdapter;
import com.solusikatarak.solusikatarak.helper.RecyclerTouchListener;
import com.solusikatarak.solusikatarak.models.Jadwal;
import com.solusikatarak.solusikatarak.models.MetaJadwal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class JadwalDokterFragment extends DialogFragment {
    private List<Jadwal> jadwalList;
    private RecyclerView recyclerView;
    private JadwalAdapter mAdapter;
    MetaJadwal metaJadwal;

    ProgressDialog pd;
    CheckJadwalActivity checkJadwalActivity;

    public JadwalDokterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_jadwal_dokter, container, false);

        jadwalList = new ArrayList<>();
        checkJadwalActivity = (CheckJadwalActivity) getActivity();
        checkJadwalActivity.setTextTitle("List Jadwal");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvJadwal);

        mAdapter = new JadwalAdapter(jadwalList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

//        pd = new ProgressDialog(getActivity());
//        pd.setMessage("Loading.........");
//        pd.show();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                checkJadwalActivity.setJadwal(jadwalList.get(position));
                

            }

            @Override
            public void onLongClick(View view, final int position) {

            }
        }));

        prepareJadwalData();

        return rootView;
    }

    public void prepareJadwalData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConfig.JADWAL_DOKTER_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                pd.dismiss();
                ArrayList<String> jadwal = new ArrayList<>();
                metaJadwal = new Gson().fromJson(response, MetaJadwal.class);
                if(metaJadwal.getStatus().equalsIgnoreCase("fail")){
                    Toast.makeText(getContext(), "Jadwal Belum Tersedia", Toast.LENGTH_LONG).show();
                }
                Log.d("RESPON STATUS", metaJadwal.getStatus());
                Log.d("RESPON LIHAT JADWAL", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("jadwal");
                    for (int i =0; i<jsonArray.length();i++){
                        JSONObject jo = jsonArray.getJSONObject(i);
                        Jadwal jadwallist = new Jadwal(
                                jo.getInt("id"),
                                jo.getString("hari"),
                                jo.getString("mulai"),
                                jo.getString("selesai"),
                                jo.getInt("id_dokter"));

                        jadwalList.add(jadwallist);
                    }
                    recyclerView.setAdapter(mAdapter);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_dokter", String.valueOf(checkJadwalActivity.getDokter().getId()));
                return params;
            }
        };
        MySingleton.getmInstance(getActivity()).addToRequestQueue(stringRequest);
        mAdapter.notifyDataSetChanged();
    }

}
