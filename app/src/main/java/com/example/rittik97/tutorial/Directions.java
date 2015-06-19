package com.example.rittik97.tutorial;


import android.os.Bundle;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class Directions extends android.support.v4.app.Fragment {


    public Directions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LatLng fromPosition = new LatLng(13.687140112679154, 100.53525868803263);
        LatLng toPosition = new LatLng(13.683660045847258, 100.53900808095932);

        String urltext = "http://maps.googleapis.com/maps/api/directions/xml?"
                + "origin=" + fromPosition.latitude + "," + fromPosition.longitude
                + "&destination=" + toPosition.latitude + "," + toPosition.longitude
                + "&sensor=false&units=metric&mode=" + "driving";
        URL url;
        HttpURLConnection urlConnection;
        try {


        } catch (Exception e) {
        }

        try {
            url = new URL(urltext);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //readStream(in);
        }
        catch (Exception e) {
        }
        /*finally{
                urlConnection.disconnect();
            }
        */// Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_directions, container, false);
    }


}
