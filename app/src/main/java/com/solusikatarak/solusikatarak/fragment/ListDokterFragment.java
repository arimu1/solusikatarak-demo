package com.solusikatarak.solusikatarak.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.activities.CheckJadwalActivity;
import com.solusikatarak.solusikatarak.api.APIConfig;
import com.solusikatarak.solusikatarak.api.MySingleton;
import com.solusikatarak.solusikatarak.helper.DokterAdapter;
import com.solusikatarak.solusikatarak.helper.RecyclerTouchListener;
import com.solusikatarak.solusikatarak.models.Dokter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListDokterFragment extends Fragment {
    private List<Dokter> dokterList;
    private RecyclerView recyclerView;
    private DokterAdapter mAdapter;

    ProgressDialog pd;
    CheckJadwalActivity checkJadwalActivity;

    public ListDokterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list_dokter, container, false);
        dokterList = new ArrayList<>();
        checkJadwalActivity = (CheckJadwalActivity) getActivity();
        checkJadwalActivity.setTextTitle("List Dokter");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvDokter);

        mAdapter = new DokterAdapter(dokterList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading.........");
        pd.show();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                checkJadwalActivity.setDokter(dokterList.get(position));

//                JadwalDokterFragment jadwalDokterFragment = new JadwalDokterFragment();
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.content, jadwalDokterFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                FragmentManager fm = getFragmentManager();
                JadwalDokterFragment dialogFragment = new JadwalDokterFragment();
                dialogFragment.show(fm, "Sample Fragment");

            }

            @Override
            public void onLongClick(View view, final int position) {

            }
        }));

        prepareDokterData();

        return rootView;
    }

    public void prepareDokterData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConfig.VIEW_DOKTER_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPON LIHAT DOKTER", response);
                try {
                    pd.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("dokter");
                    for (int i =0; i<jsonArray.length();i++){
                        JSONObject jo = jsonArray.getJSONObject(i);
                        Dokter dokterlist = new Dokter(
                                jo.getInt("id"),
                                jo.getInt("role"),
                                jo.getString("name"),
                                jo.getString("email"),
                                jo.getString("fotouser"));

                        Log.d("IMAGE URL", jo.getString("fotouser"));
                        dokterList.add(dokterlist);
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
        });
        MySingleton.getmInstance(getActivity()).addToRequestQueue(stringRequest);
        mAdapter.notifyDataSetChanged();
    }

}
