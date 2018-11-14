package com.solusikatarak.solusikatarak.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.activities.LihatPasienActivity;
import com.solusikatarak.solusikatarak.api.APIConfig;
import com.solusikatarak.solusikatarak.api.VolleyMultipartRequest;
import com.solusikatarak.solusikatarak.helper.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailPasienFragment extends Fragment implements View.OnClickListener, RequestListener<Drawable> {
private TextView tvNama, tvNik, tvNomorhp, tvAlamat, tvJeniskelamin, tvTtl, tvJadwal;
private FloatingActionButton floatedit,floatmap;
private ImageView ivKtp, ivBpjs;
Uri ktpPath, bpjsPath;
private Bitmap bitmapKtp, bitmapBpjs;
private final int IMG_KTP = 0, IMG_BPJS = 1;
ProgressDialog pd;
LihatPasienActivity lihatPasienActivity;

    public DetailPasienFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail_pasien, container, false);
        lihatPasienActivity = (LihatPasienActivity) getActivity();

        lihatPasienActivity.setTextTitle("Detail Pasien");

         tvNama = (TextView) rootView.findViewById(R.id.tvNama);
         tvNik = (TextView) rootView.findViewById(R.id.tvNik);
         tvNomorhp = (TextView) rootView.findViewById(R.id.tvNomorhp);
         tvAlamat = (TextView) rootView.findViewById(R.id.tvAlamat);
         tvJeniskelamin = (TextView) rootView.findViewById(R.id.tvJeniskelamin);
         tvTtl = (TextView) rootView.findViewById(R.id.tvTtl);
         tvJadwal = (TextView) rootView.findViewById(R.id.tvJadwal);

         ivKtp = (ImageView) rootView.findViewById(R.id.ivKtp);
         ivKtp.setOnClickListener(this);
         ivBpjs = (ImageView) rootView.findViewById(R.id.ivBpjs);
         ivBpjs.setOnClickListener(this);

         floatedit = (FloatingActionButton) rootView.findViewById(R.id.floatedit);
         floatedit.setOnClickListener(this);
         floatmap = (FloatingActionButton) rootView.findViewById(R.id.floatmap);
         floatmap.setOnClickListener(this);

         if(lihatPasienActivity.getPasien().getStatus() !=0){
             floatedit.setVisibility(View.GONE);
         }

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading.........");

        setDetailValue();
        return rootView;
    }

    private void setDetailValue() {
        tvNama.setText(lihatPasienActivity.getPasien().getNama());
        tvNik.setText(lihatPasienActivity.getPasien().getNIK());
        tvNomorhp.setText(lihatPasienActivity.getPasien().getNomorhp());
        tvAlamat.setText(lihatPasienActivity.getPasien().getAlamat());
        tvJeniskelamin.setText(lihatPasienActivity.getPasien().getGender());
        tvTtl.setText(lihatPasienActivity.getPasien().getTTL());
        tvJadwal.setText(lihatPasienActivity.getPasien().getJadwal());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);

        pd.show();
        Glide.with(getContext())
                .load(lihatPasienActivity.getPasien().getKtpurl())
//                .apply(requestOptions)
                .listener(this)
                .into(ivKtp);
        Glide.with(getContext())
                .load(lihatPasienActivity.getPasien().getBpjsurl())
//                .apply(requestOptions)
                .listener(this)
                .into(ivBpjs);
//        Glide.with(context)
//                .load(pasien.getMapurl())
//                .into(holder.ivFotomap);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.floatedit:
                EditPasienFragment editPasienFragment = new EditPasienFragment();
                FragmentTransaction fm = getFragmentManager().beginTransaction();
                fm.replace(R.id.content, editPasienFragment);
                fm.addToBackStack(null);
                fm.commit();
                break;
            case R.id.floatmap:
                new MapDialogFragment().show(getFragmentManager(), null);
                break;
            case R.id.ivKtp:
                Intent photoKtpIntent = new Intent();
                photoKtpIntent.setType("image/*");
                photoKtpIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(photoKtpIntent, IMG_KTP);
                break;
            case R.id.ivBpjs:
                Intent photoBpjsIntent = new Intent(Intent.ACTION_PICK);
                photoBpjsIntent.setType("image/*");
                photoBpjsIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(photoBpjsIntent, IMG_BPJS );
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_KTP && resultCode == RESULT_OK && data!=null)
        {
            ktpPath = data.getData();
            bitmapKtp = decodeBitmap(getActivity().getApplicationContext(), ktpPath, 4);
            lihatPasienActivity.getPasien().setBitmapKtp(bitmapKtp);
            //Glide.with(getActivity()).load(ktpPath).into(fotoktp);
            pd.show();
            saveFoto(requestCode);
            Glide.with(getActivity()).load( ktpPath).into(ivKtp);

        }
        else  if(requestCode == IMG_BPJS && resultCode == RESULT_OK && data!=null)
        {
            bpjsPath = data.getData();
            bitmapBpjs = decodeBitmap(getActivity().getApplicationContext(), bpjsPath, 4);
            lihatPasienActivity.getPasien().setBitmapBpjs(bitmapBpjs);
            //Glide.with(getActivity()).load(bpjsPath).into(fotobpjs);
            pd.show();
            saveFoto(requestCode);
            Glide.with(getActivity()).load( bpjsPath).into(ivBpjs);

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

    private void saveFoto(final int requestCode) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIConfig.UPDATE_FOTO_PASIEN_API,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            pd.dismiss();
                            JSONObject getObject = new JSONObject(new String(response.data));

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
                String id = SharedPrefManager.getInstance(getActivity()).getRelawan().getId()+"";
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(lihatPasienActivity.getPasien().getId()));
                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                if(requestCode == IMG_KTP){
                params.put("fotoktp", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmapKtp)));}
                else if(requestCode == IMG_BPJS){
                params.put("fotobpjs", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmapBpjs)));}
                return params;
            }

        };
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        pd.dismiss();
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
        pd.dismiss();
        return false;
    }
}
