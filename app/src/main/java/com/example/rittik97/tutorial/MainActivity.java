package com.example.rittik97.tutorial;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
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
import android.widget.EditText;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

import static android.location.Location.distanceBetween;


public class MainActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener,OnMapReadyCallback,TextToSpeech.OnInitListener ,Executor

{

    private GoogleApiClient mGoogleApiClient;

    private static final String TAG = "Log";
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    Location mLastLocation;
    private TextView tv23;
    private TextView tv2;
    private EditText destination;
    private PolylineOptions polylineOptions=null;
    private GoogleMap map;
    protected LatLng fromPosition;
    private LatLng toPosition;
    private JSONObject objr;
    TextToSpeech tts;
    ArrayList instructions = null;
    private int flagforcoordinates=0;
    private int flagforinstructions=0;
    private boolean navigating;
    ArrayList<LatLng> points;
    ArrayList <LatLng> endlocations=null;
    private Marker marker;
    private int totalturns;
    private int currentturn;
    private ParserTask parserTask;
    private getinstructions gi;
    private endpoints endloc;
    private int docalcs=1;
    private boolean isnavigating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        mCurrentLocation=null;


        setContentView(R.layout.activity_main);
        //ViewPager vp = (ViewPager) findViewById(R.id.pager);
        //FragmentManager fm=getSupportFragmentManager();
        //vp.setAdapter(new adapt((fm)));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();//getMapAsync(this)
        setupmap();
        tts=new TextToSpeech(this,this);


    }
    private void setupmap(){
        map.setMyLocationEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);
        }

    }

    @Override
    public void execute(Runnable r) {
        new Thread(r).start();

    }

    private class Direct extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }
    }
    public void startclick(View v){
        map.clear();
        navigating=false;
        //LatLng fromPosition = new LatLng(40.798506, -73.964577);
        //LatLng toPosition = new LatLng(40.798204, -73.952304);//40.8079639, -73.9630146);
        try {
            destination= (EditText) findViewById(R.id.destination);
            String dest = destination.getText().toString();



            //talkthis(dest);

            toPosition= geocoder(dest);
            if(toPosition.latitude==0 && toPosition.longitude==0) return;
            Double la=mLastLocation.getLatitude();
            Double ln=mLastLocation.getLongitude();
            fromPosition = new LatLng(la,ln);

        }
        catch (Exception e) {
            Log.e(TAG,"ERRRRRRRRRRRRRRR", e);
            alertexception(e);
        }
        //geocoder(dest);
        // String urltext = "http://maps.googleapis.com/maps/api/directions/json?origin=40.798506,-73.964577&destination=(40.8079639,-73.9630146&sensor=false&units=metric&mode=walking";
        String urltext = "http://maps.googleapis.com/maps/api/directions/json?"
              + "origin=" + fromPosition.latitude + "," + fromPosition.longitude
              + "&destination=" + toPosition.latitude + "," + toPosition.longitude
              + "&sensor=false&units=metric&mode=" + "walking";
        // Replace JSON to XML to make above string return xml



        // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        //StrictMode.setThreadPolicy(policy);
        //tv23= (TextView) findViewById(R.id.tv1);
        //tv2= (TextView) findViewById(R.id.textView2);



        try {


            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(urltext);
            /*
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
            Toast.makeText(this,list.toString(),Toast.LENGTH_LONG).show();
            ArrayList<LatLng> points = new ArrayList<LatLng>();
            for(int i=0;i<list.size();i++)
            {
                try{
                    double lat = (list.get(i).latitude);
                    double lng = (list.get(i).longitude);
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);}
                catch(Exception e){Toast.makeText(this,"Chut",Toast.LENGTH_LONG).show();}
            }
            polylineOptions.addAll(points);
            map.addPolyline(polylineOptions);
          */
        }

        /*
        catch (MalformedURLException e) {
            // DEBUG
            Log.e("DEBUG: ", e.toString());
        } catch (IOException e) {
            // DEBUG
            Log.e("DEBUG: ", e.toString());
        }
        */

        catch (Exception e) {
            Log.e(TAG,"ERRRRRRRRRRRRRRR", e);
            alertexception(e);
        }
        //while (parserTask.getStatus().equals(AsyncTask.Status.FINISHED) ==false && gi.getStatus().equals(AsyncTask.Status.FINISHED)==false)

    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url1) {

            // For storing data from web service

            String data = "";
            URL url;

            HttpURLConnection urlConnection;
            try{
                // Fetching the data from web service
                url = new URL(url1[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                //Toast.makeText(getActivity(),"Start",Toast.LENGTH_SHORT).show();
                urlConnection.connect();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                objr=new JSONObject(responseStrBuilder.toString());
                //Log.i(TAG, objr.toString());
                data=objr.toString();
            }catch(Exception e){
                Log.d("Background Task",e.toString());

            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            parserTask = new ParserTask();
            gi=new getinstructions();
            endloc=new endpoints();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
            gi.execute(result);
            endloc.execute(result);

        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            flagforcoordinates=1;

            points=null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(4);
                lineOptions.color(Color.GRAY);
            }

            // Drawing polyline in the Google Map for the i-th route
            //Toast.makeText(getApplicationContext(),points.toString(), Toast.LENGTH_SHORT).show();
            map.addPolyline(lineOptions);
        }
    }

    private class getinstructions extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;


            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                instructions = parser.parsehtml(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return instructions;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            flagforinstructions=1;












        }
    }
    private class endpoints extends AsyncTask<String, Integer, ArrayList >{

        // Parsing the data in non-ui thread
        @Override
        protected ArrayList doInBackground(String... jsonData) {

            JSONObject jObject;


            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                endlocations = parser.parseendpoints(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return endlocations;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(ArrayList result) {

            totalturns=endlocations.size();
            currentturn=0;
            navigating=true;
            for(int i=0;i<totalturns;i++)
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(endlocations.get(i).latitude,
                            endlocations.get(i).longitude))
                    .title("Turn "+i));

            talkthis(instructions.get(currentturn).toString());


        }
    }

    void startnavigation(){
        try {
            while (parserTask.getStatus().equals(AsyncTask.Status.FINISHED) == false && gi.getStatus().equals(AsyncTask.Status.FINISHED) == false) {
            }
            //Toast.makeText(this,points.size(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(this,instructions.size(), Toast.LENGTH_SHORT).show();
            Log.i(TAG, String.valueOf(instructions.size()));
            int n = points.size();
            int i = 0;
            double mylat;
            double mylong;
            boolean looper=true;
            while (i < n) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(instructions.get(i).toString(), TextToSpeech.QUEUE_ADD, null, null);
                } else {
                    HashMap<String, String> param = new HashMap<String, String>();
                    tts.speak(instructions.get(i).toString(), TextToSpeech.QUEUE_ADD, param);
                }
                while(looper) {
                    if (mCurrentLocation != null) {
                        mylat = mCurrentLocation.getLatitude();
                        mylong = mCurrentLocation.getLongitude();
                    } else {
                        mylat = mLastLocation.getLatitude();
                        mylong = mLastLocation.getLongitude();
                    }
                    float results[] = new float[3];

                    Location.distanceBetween(mylat, mylong
                            , points.get(i).latitude
                            , points.get(i).longitude,
                            results
                    );
                    Log.i(TAG, String.valueOf(results[0]));
                    if (results[0] > 3) {
                        continue;
                    }
                    else
                    {

                        looper=false;


                    }
                }
                //execute(new navigation());
                i++;
            }
        }
        catch (Exception e) {
            Log.e(TAG,"ERRRRRRRRRRRRRRR", e);
            alertexception(e);

        }



    }

    public void alertexception(Exception e){
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

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);


        if(mLastLocation!=null) {
           // Toast.makeText(this, "Point :" + mLastLocation.getLatitude(), Toast.LENGTH_SHORT).show();
            makecameragotocurrentlocation(    mLastLocation
            );
        }
        else
            Toast.makeText(this,"Can't retrieve Location", Toast.LENGTH_SHORT).show();


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
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    @Override
    public void onLocationChanged(Location location) {
        Location mCurrentLocation = location;
        mLastLocation=mCurrentLocation;
        //Toast.makeText(this,"Point2 :"+mCurrentLocation.getLatitude(), Toast.LENGTH_SHORT).show();
        makecameragotocurrentlocation(mCurrentLocation);

        //updateUI();
        //Date mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        if(navigating==true && parserTask.getStatus().equals(AsyncTask.Status.FINISHED) == true
                && gi.getStatus().equals(AsyncTask.Status.FINISHED) == true
                && endloc.getStatus().equals(AsyncTask.Status.FINISHED) == true){
            double mylat;
            double mylong;
            isnavigating=true;
            /*
            int turns[]=new int[endlocations.size()];
            if(docalcs==1){
                docalcs=0;
                int whereisloop=0;
                float min=10000;
                float results[]=new float[3];
                for(int i=0;i<endlocations.size();i++){
                    for(int j=whereisloop;j<points.size();j++){
                        Location.distanceBetween(points.get(j).latitude,points.get(j).longitude,
                        endlocations.get(i).latitude,endlocations.get(i).longitude,results
                                );
                        if(results[0]<min){
                            min=results[0];
                        }
                        if(min<10){
                            turns[i]=j;
                            whereisloop=j;
                            break;
                        }

                    }
                }

                Toast.makeText(this,turns.toString(), Toast.LENGTH_SHORT).show();


            }
            */

             if (mCurrentLocation != null) {
             mylat = mCurrentLocation.getLatitude();
             mylong = mCurrentLocation.getLongitude();
                    } else {
                        mylat = mLastLocation.getLatitude();
                        mylong = mLastLocation.getLongitude();
                    }
                    float results[] = new float[3];

                    Location.distanceBetween(mylat, mylong
                            , endlocations.get(currentturn).latitude
                            , endlocations.get(currentturn).longitude,
                            results
                    );

             if(results[0]<10){currentturn++;
             talkthis(instructions.get(currentturn).toString());
             }
             if(currentturn==totalturns){//You have reached destination
             navigating=false;
              }

        }

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
            alertexception(e);
        }

    }
    public void makecameragotocurrentlocation(Location loc){
        //map.clear();
        Location sydney = loc;
        double lat=sydney.getLatitude();
        double lon = sydney.getLongitude();
        LatLng lng=new LatLng(lat,lon);

        map.addMarker(new MarkerOptions().position(lng));
        // map.moveCamera(CameraUpdateFactory.newLatLng(sydney), 10);
        map.moveCamera(CameraUpdateFactory.newLatLng(lng));
        if(isnavigating){
            Location target=new Location("");
            target.setLatitude(endlocations.get(currentturn + 1).latitude);
            target.setLongitude(endlocations.get(currentturn + 1).longitude);
            CameraPosition cameraPosition =
                    new CameraPosition.Builder()
                            .target(endlocations.get(currentturn + 1))
                            .bearing(mCurrentLocation.bearingTo(target))
                            .tilt(90)
                            .zoom(map.getCameraPosition().zoom)
                            .build();
        }
            else{
            map.animateCamera(CameraUpdateFactory.zoomTo(16));
        }
    }
    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.

        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        //Toast.makeText(this,"Point",Toast.LENGTH_SHORT).show();


    }


    public void reversegeocoder(){
        Geocoder geocoder= new Geocoder(this, Locale.ENGLISH);

        try {

            //Place your latitude and longitude
            List<Address> addresses = geocoder.getFromLocation(40.798204, -73.952304, 1);

            if(addresses != null) {

                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();

                for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                }

                Toast.makeText(this,strAddress.toString(), Toast.LENGTH_SHORT).show();

            }

            else
                Toast.makeText(this,"No address", Toast.LENGTH_SHORT).show();

        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Could not get address..!", Toast.LENGTH_LONG).show();
        }
    }
    public LatLng geocoder(String destination){
        Geocoder geocoder= new Geocoder(this, Locale.ENGLISH);

        try {

            //talkthis(String.valueOf(mLastLocation.getLatitude()));
            //talkthis(String.valueOf(mLastLocation.getLatitude()-50/70));
            //Place your latitude and longitude
            List<Address> addresses = geocoder.getFromLocationName(destination, 6
                    , mLastLocation.getLatitude() - 0.015, mLastLocation.getLongitude() -0.009 , mLastLocation.getLatitude() + 0.015, mLastLocation.getLongitude() + 0.009
            );
            if(!addresses.isEmpty() && addresses!=null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // Add the buttons
                builder.setMessage(addresses.toString());
                // Set other dialog properties

                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();      
                int distances[] = new int[addresses.size()];
                for (int i = 0; i < addresses.size(); i++) {
                    float result[] = new float[3];
                    Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude()
                            , addresses.get(i).getLatitude(), addresses.get(i).getLongitude(), result
                    );
                    distances[i] = (int) result[0];
                }
                talkthis("I found " + String.valueOf(addresses.size()) + "options");
                //if(distances[0]null)
                int temp1 = 0;
                for (int i = 0; i < distances.length; i++) {
                    if (distances[i] < temp1) {
                        temp1 = i;
                    }


                }
                talkthis("The closest one is" + distances[temp1] + "meters away, I'll take you there");


                if (addresses != null) {

                    Address address = addresses.get(temp1);

                    double lat = address.getLatitude();
                    double lng = address.getLongitude();

                    Toast.makeText(this, Double.toString(lat) + "  " + Double.toString(lng), Toast.LENGTH_SHORT).show();
                    LatLng returnlatlng = new LatLng(lat, lng);
                    return returnlatlng;
                }

            }else {Toast.makeText(this, "No address", Toast.LENGTH_SHORT).show();
                talkthis("Couldn't find what you were looking for");}

        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Could not get address..!", Toast.LENGTH_LONG).show();
        }
        return new LatLng(0,0);


    }
    private class navigation implements Runnable{



        @Override
        public void run() {
            Looper.prepare();
            Handler hr=new Handler();
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            try {
               /* while(flagforcoordinates!=1 && flagforinstructions!=1)
                {Thread.sleep(1000,0);
                    Toast.makeText(MainActivity.this,"No address", Toast.LENGTH_SHORT).show();

                }*/

            int n=points.size();
            int i=0;
            double mylat;
            double mylong;
            while (i<n) {

                if(mCurrentLocation!=null)
                { mylat = mCurrentLocation.getLatitude();
                    mylong = mCurrentLocation.getLongitude();}
                else
                { mylat = mLastLocation.getLatitude();
                    mylong = mLastLocation.getLongitude();}
                float results[]=new float[3];

                Location.distanceBetween(mylat,mylong
                        ,points.get(i).latitude
                        ,points.get(i).longitude,
                        results
                );
                // if(results[0]>3)
                {



                }

            }
            Looper.loop();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }


    }
    public void talkthis(String s){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(s,TextToSpeech.QUEUE_ADD,null,null );
        }
        else
        {
            HashMap<String, String> param=new HashMap<String,String>();
            tts.speak(s,TextToSpeech.QUEUE_ADD,param );
        }

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