package com.solusikatarak.solusikatarak.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.activities.LihatPasienActivity;
import com.solusikatarak.solusikatarak.activities.LoginActivity;
import com.solusikatarak.solusikatarak.activities.VerifTokenActivity;
import com.solusikatarak.solusikatarak.api.APIConfig;
import com.solusikatarak.solusikatarak.api.MySingleton;
import com.solusikatarak.solusikatarak.helper.PasienAdapter;
import com.solusikatarak.solusikatarak.helper.RecyclerTouchListener;
import com.solusikatarak.solusikatarak.helper.SharedPrefManager;
import com.solusikatarak.solusikatarak.models.Meta;
import com.solusikatarak.solusikatarak.models.MetaJadwal;
import com.solusikatarak.solusikatarak.models.MetaPasien;
import com.solusikatarak.solusikatarak.models.Pasien;
import com.solusikatarak.solusikatarak.models.ResponseAPI;

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
public class ListPasienFragment extends Fragment {
    private List<Pasien> pasienList;
    private RecyclerView recyclerView;
    private PasienAdapter mAdapter;
    ResponseAPI responseAPI;
    android.support.v7.app.AlertDialog.Builder builder;

    ProgressDialog pd;
    LihatPasienActivity lihatPasienActivity;

    public ListPasienFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list_pasien, container, false);
        pasienList = new ArrayList<>();
        lihatPasienActivity = (LihatPasienActivity) getActivity();
        lihatPasienActivity.setTextTitle("List Pasien");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvPasien);
        builder = new android.support.v7.app.AlertDialog.Builder(getActivity());

        mAdapter = new PasienAdapter(pasienList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading.........");
        pd.show();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                lihatPasienActivity.setPasien(pasienList.get(position));

                Bundle bundle = new Bundle();
                Log.d("ID ADALAH", String.valueOf(lihatPasienActivity.getPasien().getId()));
                DetailPasienFragment detailPasienFragment = new DetailPasienFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, detailPasienFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

            @Override
            public void onLongClick(View view, final int position) {
                    detail(position);
        }
        }));
        preparePasienData();

        return rootView;
    }

    public void detail(final int position){
        if (pasienList.get(position).getStatus() == 0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Delete");
            alert.setMessage("Ingin Menghapus Pasien?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    pd.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConfig.DELETE_PASIEN_API , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.dismiss();
                            responseAPI = new Gson().fromJson(response, ResponseAPI.class);
                            String code = responseAPI.getCode();
                            String message = responseAPI.getMessage();
                            builder.setTitle("Server Response....");
                            builder.setMessage(message);
                            displayAlert(code, position);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id", String.valueOf(pasienList.get(position).getId()));
                            return params;
                        }
                    };
                    MySingleton.getmInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
                    dialog.dismiss();
                }
            });

            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alert.show();
        }
        else if(pasienList.get(position).getStatus()==3){
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Konfirmasi Pembayaran");
            alert.setMessage("Apakah anda sudah mendapatkan pembayaran dari dokter dan ingin konfirmasi?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    pd.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConfig.KONFIRMASI_PEMBAYARAN_API , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.dismiss();
                            responseAPI = new Gson().fromJson(response, ResponseAPI.class);
                            String code = responseAPI.getCode();
                            String message = responseAPI.getMessage();
                            Log.d("Konfirm Pembayaran", message);
                            builder.setTitle("Server Response....");
                            builder.setMessage(message);

                            mAdapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            String id = SharedPrefManager.getInstance(getActivity()).getRelawan().getId()+"";
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id", String.valueOf(pasienList.get(position).getId()));
                            params.put("inputstatus", 4+"");
                            params.put("id_dokter", String.valueOf(pasienList.get(position).getId_dokter()));
                            params.put("id_relawan", id);
                            return params;
                        }
                    };
                    MySingleton.getmInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
                    dialogInterface.dismiss();

                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
        else{
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Alert");
            alert.setMessage("Pasien yang sudah melalukan checkup atau operasi tidak dapat dihapus");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {


                    dialog.dismiss();
                }
            });
            alert.show();
        }

    }


    private void displayAlert(final String code, final int position) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (code.equals("input error")) {

                }else if (code.equalsIgnoreCase("Success")) {
                    pasienList.remove(position);
                    mAdapter.notifyItemRemoved(position);
                }
                else if (code.equalsIgnoreCase("Fail")) {
                }
            }
        });
        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void preparePasienData() {

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

                        Log.d("IMAGE URL", jo.getString("fotoktp"));
                        pasienList.add(pasienlist);
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
