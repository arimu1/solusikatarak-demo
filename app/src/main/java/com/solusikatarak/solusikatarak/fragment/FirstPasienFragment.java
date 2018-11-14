package com.solusikatarak.solusikatarak.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;

import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.activities.TambahPasienActivity;
import com.solusikatarak.solusikatarak.models.Pasien;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstPasienFragment extends Fragment implements View.OnClickListener {
    private EditText etNama, etNik, etNomorhp;
    private ImageButton ibNext;
    TambahPasienActivity tambahPasienActivity;

    public FirstPasienFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_first_pasien, container, false);

        tambahPasienActivity = (TambahPasienActivity) getActivity();

        etNama = (EditText) rootView.findViewById(R.id.etNama);
        etNama.setSingleLine();
        etNama.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        etNik = (EditText) rootView.findViewById(R.id.etNik);
        etNik.setSingleLine();
        etNik.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        etNomorhp = (EditText) rootView.findViewById(R.id.etNomorhp);
        ibNext = (ImageButton) rootView.findViewById(R.id.ibNext);
        ibNext.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibNext:
                if(isEmpty(etNama)){
                    etNama.setError("Nama tidak boleh kosong");
                }
                else if(isEmpty(etNik)){
                    etNik.setError("NIK tidak boleh kosong");
                }
                else if(isEmpty(etNomorhp)){
                    etNomorhp.setError("Nomor HP tidak boleh kosong");
                }
                else if(etNomorhp.getText().toString().trim().length() < 10){
                    etNomorhp.setError("Nomor HP tidak kurang dari 10 angka");
                }
                else {
                    tambahPasienActivity.getPasien().setNama(etNama.getText().toString());
                    tambahPasienActivity.getPasien().setNIK(etNik.getText().toString());
                    tambahPasienActivity.getPasien().setNomorhp(etNomorhp.getText().toString());
                    SecondPasienFragment secondPasienFragment = new SecondPasienFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.content, secondPasienFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                break;
        }
    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }
}
