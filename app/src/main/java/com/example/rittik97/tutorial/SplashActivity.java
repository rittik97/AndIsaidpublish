package com.example.rittik97.tutorial;

//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.os.Handler;

import com.parse.Parse;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Parse.enableLocalDatastore(SplashActivity.this);

        Parse.initialize(SplashActivity.this, "kIldtIpiPS8tH9wpQrkHUTGXybuTCn6NeUYzAmKU", "dyXD1Kl19DCl2gfuhvVeUPm9mm8AE51PdKczCfm4");

        Runnable execute= new Runnable() {
            @Override
            public void run() {
                nextact();
                finish();
            }
        };
        Handler hr= new Handler();
        hr.postDelayed(execute,1000);
    }
    //new Handler().postDelayed(new Runnable(){ public void run() {

    //}},3000 );

    public void nextact(){
        Intent i=new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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
