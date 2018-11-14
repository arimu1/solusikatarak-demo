package com.solusikatarak.solusikatarak.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.solusikatarak.solusikatarak.R;
import com.solusikatarak.solusikatarak.activities.LihatPasienActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapDialogFragment extends DialogFragment implements OnMapReadyCallback {
    GoogleMap mGoogleMap;
    SupportMapFragment mapFragment;
    LihatPasienActivity lihatPasienActivity;
    ProgressDialog pd;

    public MapDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map_dialog, container, false);

        lihatPasienActivity = (LihatPasienActivity) getActivity();


        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading.........");
        pd.show();

        mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SupportMapFragment f = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        pd.dismiss();
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng latLng = new LatLng(lihatPasienActivity.getPasien().getLatitude(),lihatPasienActivity.getPasien().getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
        mGoogleMap.addMarker(markerOptions);
    }

    @Override
    public void onPause() {
        dismiss();
        super.onPause();
    }

}
