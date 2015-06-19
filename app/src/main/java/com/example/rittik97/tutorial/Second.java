package com.example.rittik97.tutorial;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class Second extends SupportMapFragment{



    public Second() {
        // Required empty public constructor
    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_second, container, false);
    }
    */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       GoogleMap map= getMap();
        map.getUiSettings().setZoomControlsEnabled(true);
        MarkerOptions mo =new MarkerOptions();
        mo.title("Title");
        mo.position(new LatLng(40,-3));
        mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));

        map.setMyLocationEnabled(true);
        map.addMarker(mo);


    }



}
