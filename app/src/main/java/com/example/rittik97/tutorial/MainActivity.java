package com.example.rittik97.tutorial;

import android.app.Activity;
import android.net.Uri;
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

import com.google.android.gms.maps.SupportMapFragment;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager vp = (ViewPager) findViewById(R.id.pager);
        FragmentManager fm=getSupportFragmentManager();
        vp.setAdapter(new adapt((fm)));
    }



    public static class adapt extends FragmentPagerAdapter{

            public adapt(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                if(position == 0)
                    return new NewFragment();
                else
                    return new Second();



            }

            @Override
            public int getCount() {
                return 2;
            }

        @Override
        public CharSequence getPageTitle(int position) {
          if(position == 0)
            return "List";
         else
           return "Map";
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
