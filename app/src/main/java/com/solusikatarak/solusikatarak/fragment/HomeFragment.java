package com.solusikatarak.solusikatarak.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.activities.CheckJadwalActivity;
import com.solusikatarak.solusikatarak.activities.LihatPasienActivity;
import com.solusikatarak.solusikatarak.activities.StatusOperasiActivity;
import com.solusikatarak.solusikatarak.activities.TambahPasienActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private CardView cvAddpasien, cvViewpasien, cvStatus, cvJadwal;
    Intent intent;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        cvAddpasien = (CardView)rootView.findViewById(R.id.cvAddpasien);
        cvAddpasien.setOnClickListener(this);
        cvJadwal = (CardView)rootView.findViewById(R.id.cvJadwal);
        cvJadwal.setOnClickListener(this);
        cvViewpasien = (CardView)rootView.findViewById(R.id.cvViewpasien);
        cvViewpasien.setOnClickListener(this);
        cvStatus = (CardView)rootView.findViewById(R.id.cvStatus);
        cvStatus.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cvAddpasien:
                intent = new Intent(getActivity(), TambahPasienActivity.class);
                startActivity(intent);
                break;

            case R.id.cvJadwal:
                intent = new Intent(getActivity(), CheckJadwalActivity.class);
                startActivity(intent);
                break;

            case R.id.cvViewpasien:
                intent = new Intent(getActivity(), LihatPasienActivity.class);
                startActivity(intent);
                break;

            case R.id.cvStatus:
                intent = new Intent(getActivity(), StatusOperasiActivity.class);
                startActivity(intent);
                break;
        }
    }
}
