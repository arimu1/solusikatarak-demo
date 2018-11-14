package com.solusikatarak.solusikatarak.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.activities.TambahPasienActivity;
import com.solusikatarak.solusikatarak.models.Pasien;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdPasienFragment extends Fragment implements View.OnClickListener {
    private EditText etTtl;
    private ImageView fotoktp, fotobpjs;
    private ImageView ivKtp, ivBpjs;
    private Bitmap bitmapKtp, bitmapBpjs;
    Uri ktpPath, bpjsPath;
    private ImageButton ibNext, ibBack;
    private final int IMG_KTP = 0, IMG_BPJS = 1;
    TambahPasienActivity tambahPasienActivity;

    public ThirdPasienFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_third_pasien, container, false);
        tambahPasienActivity = (TambahPasienActivity) getActivity();
        etTtl = (EditText) rootView.findViewById(R.id.etTtl);

        fotoktp = (ImageView) rootView.findViewById(R.id.fotoktp);
        fotoktp.setOnClickListener(this);
        fotobpjs = (ImageView) rootView.findViewById(R.id.fotobpjs);
        fotobpjs.setOnClickListener(this);

        ivKtp = (ImageView) rootView.findViewById(R.id.ivKtp);
        ivBpjs = (ImageView) rootView.findViewById(R.id.ivBpjs);

        ibNext = (ImageButton) rootView.findViewById(R.id.ibNext);
        ibNext.setOnClickListener(this);
        ibBack = (ImageButton) rootView.findViewById(R.id.ibBack);
        ibBack.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fotoktp:
                Intent photoKtpIntent = new Intent();
                photoKtpIntent.setType("image/*");
                photoKtpIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(photoKtpIntent, IMG_KTP);
                break;
            case R.id.fotobpjs:
                Intent photoBpjsIntent = new Intent(Intent.ACTION_PICK);
                photoBpjsIntent.setType("image/*");
                photoBpjsIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(photoBpjsIntent, IMG_BPJS );
                break;
            case R.id.ibNext:
                if(isEmpty(etTtl)){
                    etTtl.setError("Tempat tanggal lahir harus diisi");
                }
                else if(ivKtp.getDrawable() == null){
                    Toast.makeText(getActivity(), "Foto KTP harus disertakan", Toast.LENGTH_LONG).show();
                }
                else if(ivBpjs.getDrawable() == null){
                    Toast.makeText(getActivity(), "Foto BPJS harus disertakan", Toast.LENGTH_LONG).show();
                }
                else {
                    tambahPasienActivity.getPasien().setTTL(etTtl.getText().toString());
                    FourthPasienFragment fourthPasienFragment = new FourthPasienFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.content, fourthPasienFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.ibBack:
                getFragmentManager().popBackStack();
                break;

        }
    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_KTP && resultCode == RESULT_OK && data!=null)
        {
            ktpPath = data.getData();
//            try {
//                bitmapKtp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), ktpPath);
                bitmapKtp = decodeBitmap(getActivity().getApplicationContext(), ktpPath, 4);
                tambahPasienActivity.getPasien().setBitmapKtp(bitmapKtp);

                Glide.with(getActivity()).load( ktpPath).into(ivKtp);
                ivKtp.setVisibility(View.VISIBLE);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
        else  if(requestCode == IMG_BPJS && resultCode == RESULT_OK && data!=null)
        {
            bpjsPath = data.getData();
//            try {
//                bitmapBpjs = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), bpjsPath);
                bitmapBpjs = decodeBitmap(getActivity().getApplicationContext(), bpjsPath, 4);
                tambahPasienActivity.getPasien().setBitmapBpjs(bitmapBpjs);

                Glide.with(getActivity()).load( bpjsPath).into(ivBpjs);
                ivBpjs.setVisibility(View.VISIBLE);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    private static Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                fileDescriptor.getFileDescriptor(), null, options);

        Log.d(TAG, options.inSampleSize + " sample method bitmap ... "
                + actuallyUsableBitmap.getWidth() + " " + actuallyUsableBitmap.getHeight());

        return actuallyUsableBitmap;
    }
}
