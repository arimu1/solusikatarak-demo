package com.solusikatarak.solusikatarak.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.activities.LihatPasienActivity;
import com.solusikatarak.solusikatarak.activities.TambahPasienActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondSummaryFragment extends Fragment implements View.OnClickListener {
    private ImageView ivKtp, ivBpjs, ivMap;
    private Button btOk;
    TambahPasienActivity tambahPasienActivity;

    public SecondSummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tambahPasienActivity = (TambahPasienActivity) getActivity();
        View rootView = inflater.inflate(R.layout.fragment_second_summary, container, false);

        ivKtp = (ImageView) rootView.findViewById(R.id.ivKtp);
        ivBpjs = (ImageView) rootView.findViewById(R.id.ivBpjs);
        ivMap = (ImageView) rootView.findViewById(R.id.ivMap);

        btOk = (Button) rootView.findViewById(R.id.btOk);
        btOk.setOnClickListener(this);

        setImageView();

        return rootView;
    }

    private void setImageView() {
        ivKtp.setImageBitmap(tambahPasienActivity.getPasien().getBitmapKtp());
        ivBpjs.setImageBitmap(tambahPasienActivity.getPasien().getBitmapBpjs());
        Glide.with(getActivity().getApplicationContext())
                .load(tambahPasienActivity.getPasien().getMapurl())
                .into(ivMap);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btOk:
                Intent intent = new Intent(getActivity(), LihatPasienActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }
}
