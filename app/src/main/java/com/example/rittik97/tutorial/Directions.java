package com.example.rittik97.tutorial;


import android.app.AlertDialog;
import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;

import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.common.logger.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;


/**
 * A simple {@link Fragment} subclass.
 */
public class Directions extends android.support.v4.app.Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MyActivity";
    public Directions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LatLng fromPosition = new LatLng(40.798506, -73.964577);
        LatLng toPosition = new LatLng(40.8079639, -73.9630146);

       // String urltext = "http://maps.googleapis.com/maps/api/directions/xml?origin=13.687140112679154,100.53525868803263&destination=(13.683660045847258,100.53900808095932&sensor=false&units=metric&mode=driving";
        String urltext = "http://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + fromPosition.latitude + "," + fromPosition.longitude
                + "&destination=" + toPosition.latitude + "," + toPosition.longitude
                + "&sensor=false&units=metric&mode=" + "driving";
        // Replace JSON to XML to make above string return xml
        View vw=inflater.inflate(R.layout.fragment_directions, container, false);
        URL url;
        HttpURLConnection urlConnection;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        TextView tv23= (TextView) vw.findViewById(R.id.tr);

        try {


        } catch (Exception e) {
        }
        JSONObject objr;
        try {

            url = new URL(urltext);
            urlConnection = (HttpURLConnection) url.openConnection();
            //Toast.makeText(getActivity(),"Start",Toast.LENGTH_SHORT).show();
            urlConnection.connect();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            tv23.setText("rt");
            //Toast.makeText(getActivity(),"Point",Toast.LENGTH_SHORT).show();
            //readStream(in);
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            objr=new JSONObject(responseStrBuilder.toString());
            Log.i(TAG,objr.toString());

            tv23.setText(objr.toString());
            System.out.print("HI");



        }
        catch (MalformedURLException e) {
            // DEBUG
            Log.e("DEBUG: ", e.toString());
        } catch (IOException e) {
            // DEBUG
            Log.e("DEBUG: ", e.toString());
        }
        catch (Exception e) {
            Log.e(TAG,"ERRRRRRRRRRRRRRR", e);
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            // Show the stack trace on Logcat.
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            // Add the buttons
            builder.setMessage(errors.toString());
            // Set other dialog properties

            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        /*finally{
                urlConnection.disconnect();
            }
        */
        // Inflate the layout for this fragment
       ///TextView tv23= (TextView) vw.findViewById(R.id.tr);
        //tv23.setText(objr.toString());
        /*
        InputStream in = null;
        String queryResult = "";
        try {
            url = new URL(urltext);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int r=httpConn.getResponseCode();
            Toast.makeText(getActivity(),r,Toast.LENGTH_SHORT).show();
            in = httpConn.getInputStream();

            BufferedInputStream bis = new BufferedInputStream(in);
            ByteBuffer baf=ByteBuffer.allocate(50);
            int read = 0;
            int bufSize = 512;
            byte[] buffer = new byte[bufSize];
            while(true){
                read = bis.read(buffer);
                if(read==-1){
                    break;
                }
                baf.put(buffer, 0, read);
            }
            queryResult = baf.toString();
            Toast.makeText(getActivity(),"Start",Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            // DEBUG
            Log.e("DEBUG: ", e.toString());
        } catch (IOException e) {
            // DEBUG
            Log.e("DEBUG: ", e.toString());
        }
        */
        return vw;
    }


    @Override
    public void onConnected(Bundle bundle) {
        GoogleApiClient mGoogleApiClient=new GoogleApiClient.Builder(this.getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if(mLastLocation!=null){}
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
