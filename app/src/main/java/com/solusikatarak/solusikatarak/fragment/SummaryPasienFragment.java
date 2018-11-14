package com.solusikatarak.solusikatarak.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.activities.TambahPasienActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryPasienFragment extends Fragment implements View.OnClickListener {
    private TextView tvNama, tvNik, tvNomorhp, tvAlamat, tvJenisKelamin, tvTtl;
    private Button btNext;
    TambahPasienActivity tambahPasienActivity;

    public SummaryPasienFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_summary_pasien, container, false);
        tambahPasienActivity = (TambahPasienActivity) getActivity();
        tvNama = (TextView) rootView.findViewById(R.id.tvNama);
        tvNik = (TextView) rootView.findViewById(R.id.tvNik);
        tvNomorhp = (TextView) rootView.findViewById(R.id.tvNomorhp);
        tvAlamat = (TextView) rootView.findViewById(R.id.tvAlamat);
        tvJenisKelamin = (TextView) rootView.findViewById(R.id.tvJeniskelamin);
        tvTtl = (TextView) rootView.findViewById(R.id.tvTtl);

        btNext = (Button) rootView.findViewById(R.id.btNext);

        btNext.setOnClickListener(this);

        setTextView();

        return rootView;
    }

    private void setTextView() {
        String alamat = tambahPasienActivity.getPasien().getJalan() + ", "
                + tambahPasienActivity.getPasien().getProvinsi() + ", "
                + tambahPasienActivity.getPasien().getKabupaten() + ", "
                + tambahPasienActivity.getPasien().getKecamatan() + ", "
                + tambahPasienActivity.getPasien().getDesa() + ", ";
        tvNama.setText(tambahPasienActivity.getPasien().getNama());
        tvNik.setText(tambahPasienActivity.getPasien().getNIK());
        tvNomorhp.setText(tambahPasienActivity.getPasien().getNomorhp());
        tvAlamat.setText(alamat);
        tvJenisKelamin.setText(tambahPasienActivity.getPasien().getGender());
        tvTtl.setText(tambahPasienActivity.getPasien().getTTL());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btNext:
                SecondSummaryFragment secondSummaryFragment = new SecondSummaryFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.content, secondSummaryFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

        }
    }
}
