package com.solusikatarak.solusikatarak.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.api.APIConfig;
import com.solusikatarak.solusikatarak.api.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
PieChart pieChart;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        pieChart = (PieChart) rootView.findViewById(R.id.pieChart);

        setPieData();

        return rootView;
    }

    private void setPieData() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);



        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConfig.DASHBOARD_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    final ArrayList<PieEntry> yValues = new ArrayList<>();
                    int terdaftar = jsonObject.getInt("terdaftar");
                    int checkup = jsonObject.getInt("checkup");
                    int operasi = jsonObject.getInt("operasi");

                    yValues.add(new PieEntry(terdaftar, "Terdaftar"));
                    yValues.add(new PieEntry(checkup, "Checkup"));
                    yValues.add(new PieEntry(operasi, "Operasi"));

                    PieDataSet dataSet = new PieDataSet(yValues, "Data");
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                    PieData data = new PieData((dataSet));
                    data.setValueTextSize(10f);
                    data.setValueTextColor(Color.YELLOW);

                    pieChart.setData(data);
                    pieChart.invalidate();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getmInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
