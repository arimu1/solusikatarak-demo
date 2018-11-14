package com.solusikatarak.solusikatarak.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.api.APIConfig;
import com.solusikatarak.solusikatarak.api.MySingleton;
import com.solusikatarak.solusikatarak.helper.PasienAdapter;
import com.solusikatarak.solusikatarak.helper.RecyclerTouchListener;
import com.solusikatarak.solusikatarak.helper.SharedPrefManager;
import com.solusikatarak.solusikatarak.models.Pasien;

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
public class CheckupFragment extends Fragment {
    private List<Pasien> pasienList;
    private RecyclerView recyclerView;
    private PasienAdapter mAdapter;

    ProgressDialog pd;


    public CheckupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_checkup, container, false);
        pasienList = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvCheckup);

        mAdapter = new PasienAdapter(pasienList);
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
//                Bundle bundle = new Bundle();
//                DetailPasienFragment detailPasienFragment = new DetailPasienFragment();
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.content, detailPasienFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        preparePasienData();

        return rootView;
    }

    private void preparePasienData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConfig.VIEW_PASIEN_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPON LIHAT PASIEN", response);
                try {
                    pd.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("pasien");
                    for (int i =0; i<jsonArray.length();i++){
                        JSONObject jo = jsonArray.getJSONObject(i);
                        if(jo.getInt("status")==1) {
                            Pasien pasienlist = new Pasien(jo.getInt("id"),
                                    jo.getString("nama"),
                                    jo.getString("nik"),
                                    jo.getString("jalan"),
                                    jo.getString("provinsi"),
                                    jo.getString("kabupaten"),
                                    jo.getString("kecamatan"),
                                    jo.getString("desa"),
                                    jo.getString("jeniskelamin"),
                                    jo.getString("ttl"),
                                    jo.getString("tanggaloperasi"),
                                    jo.getString("nomorhp"),
                                    jo.getString("fotoktp"),
                                    jo.getString("fotobpjs"),
                                    jo.getDouble("latitude"),
                                    jo.getDouble("longitude"),
                                    jo.getInt("status"));

                            Log.d("STATUS", String.valueOf(jo.getInt("status")));

                            Log.d("IMAGE URL", jo.getString("fotoktp"));
                            pasienList.add(pasienlist);
                        }
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
                String id = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getRelawan().getId()+"";
                Map<String, String> params = new HashMap<String, String>();
                params.put("idrelawan", id);
                return params;
            }
        };
        MySingleton.getmInstance(getActivity()).addToRequestQueue(stringRequest);
        mAdapter.notifyDataSetChanged();
    }

}
