package com.example.rittik97.tutorial;

import android.app.Activity;
import android.app.AlertDialog;
import android.location.Location;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.common.logger.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener,OnMapReadyCallback {

    private GoogleApiClient mGoogleApiClient;

    private static final String TAG = "Logging this";
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private TextView tv23;
    private TextView tv2;
    private PolylineOptions polylineOptions=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();


        setContentView(R.layout.activity_main);
        //ViewPager vp = (ViewPager) findViewById(R.id.pager);
        //FragmentManager fm=getSupportFragmentManager();
        //vp.setAdapter(new adapt((fm)));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        GoogleMap map = mapFragment.getMap();//getMapAsync(this)

    }
    public void startclick(){
        LatLng fromPosition = new LatLng(40.798506, -73.964577);
        LatLng toPosition = new LatLng(40.8079639, -73.9630146);

        // String urltext = "http://maps.googleapis.com/maps/api/directions/xml?origin=40.798506,-73.964577&destination=(40.8079639,-73.9630146&sensor=false&units=metric&mode=driving";
        String urltext = "http://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + fromPosition.latitude + "," + fromPosition.longitude
                + "&destination=" + toPosition.latitude + "," + toPosition.longitude
                + "&sensor=false&units=metric&mode=" + "driving";
        // Replace JSON to XML to make above string return xml

        URL url;
        HttpURLConnection urlConnection;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        tv23= (TextView) findViewById(R.id.tv1);
        //tv2= (TextView) findViewById(R.id.textView2);


        JSONObject objr;
        try {

            url = new URL(urltext);
            urlConnection = (HttpURLConnection) url.openConnection();
            //Toast.makeText(getActivity(),"Start",Toast.LENGTH_SHORT).show();
            urlConnection.connect();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //Toast.makeText(getActivity(),"Point",Toast.LENGTH_SHORT).show();
            //readStream(in);
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            objr=new JSONObject(responseStrBuilder.toString());
            Log.i(TAG, objr.toString());
            JSONArray routeObject = objr.getJSONArray("routes");
            JSONObject routes = routeObject.getJSONObject(0);
            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            // JSONObject jsonArray=objr.getJSONObject("overview_polyline");
            //String points=jsonArray.getJSONObject(0.getString("points");
            Toast.makeText(this,encodedString,Toast.LENGTH_LONG).show();

            //tv23.setText(objr.toString());
            List<LatLng> list = decodePoly(encodedString);

            ArrayList<LatLng> points = null;
            for(int i=0;i<list.size();i++)
            {

                try{points.add(list.get(i));}
                catch(Exception e){Toast.makeText(this,encodedString,Toast.LENGTH_LONG).show();}

            }
            polylineOptions.addAll(points);



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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Add the buttons
            builder.setMessage(errors.toString());
            // Set other dialog properties

            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    public void onConnected(Bundle bundle) {

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if(mLastLocation!=null)
            Toast.makeText(this,"Point :"+mLastLocation.getLatitude(), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"Nah", Toast.LENGTH_SHORT).show();


        createLocationRequest();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"Connection Fail",Toast.LENGTH_SHORT).show();
    }
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    @Override
    public void onLocationChanged(Location location) {
        Location mCurrentLocation = location;
        Toast.makeText(this,"Point2 :"+mCurrentLocation.getLatitude(), Toast.LENGTH_SHORT).show();

        //updateUI();
        //Date mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

    }
    private void updateUI() {

        //mLongitudeTextView.setTxext(String.valueOf(mCurrentLocation.getLongitude()));
        // tv23.setText(mLastUpdateTime);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //{tv2.setText("here");}
        //tv23.setText("Started");
        try {
            mGoogleApiClient.connect();
            //if(mGoogleApiClient.isConnected()) {
            //createLocationRequest();
           // startLocationUpdates();

            //}else
            //  Toast.makeText(this,"Point", Toast.LENGTH_SHORT).show();

        }catch (Exception e)
        {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            // Show the stack trace on Logcat.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Add the buttons
            builder.setMessage(errors.toString());
            // Set other dialog properties

            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // map.moveCamera(CameraUpdateFactory.newLatLng(sydney), 10);
        map.setMyLocationEnabled(true);


    }
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public static class adapt extends FragmentPagerAdapter{

            public adapt(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                if(position == 0)
                    return new Directions(); //NewFragment();
                else if (position==1)
                    return new Second();
                else
                    return new Directions();//



            }

            @Override
            public int getCount() {
                return 2;
            }

        @Override
        public CharSequence getPageTitle(int position) {
          if(position == 0)
            return "Directions";
         else if(position == 1)
           return "Map";
         else
              return "Directions";
        }

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
