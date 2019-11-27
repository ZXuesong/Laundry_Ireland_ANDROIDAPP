package com.example.zxs.laundryinireland;

import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link androidx.viewpager.widget.PagerAdapter;} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link androidx.fragment.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private FirebaseAuth mAuth;

    static TextView advice = null;
    static boolean isIndoor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        advice = findViewById(R.id.advice);



        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart(){
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this) ;
        isIndoor = prefs.getBoolean("indoor",false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_homepage) {
            // Handle the camera action
        } else if (id == R.id.nav_forecast) {
            Intent intent = new Intent(MainActivity.this,ForecastActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }  else if (id == R.id.nav_check) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private DatabaseReference mDatabase ;
        public PlaceholderFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;

            mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference[] weather = {mDatabase.child("devices").child("80:7D:3A:3C:D2:44").child("records")};

            if(getArguments().getInt(ARG_SECTION_NUMBER)==1){
                rootView = inflater.inflate(R.layout.fragment_1, container, false);
                final TextView weatherText = (TextView) rootView.findViewById(R.id.image_description);
                final ImageView weatherPic = (ImageView)rootView.findViewById(R.id.weather_image) ;
                Query last = weather[0].orderByKey().limitToLast(1);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            String wea = ds.child("weather").getValue().toString();
                            weatherText.setText(wea);
                            if(wea.equals("Rain") || wea.equals("Drizzle")){
                                weatherPic.setImageResource(R.drawable.rain);

                            }else if(wea.equals("Cloud")){
                                weatherPic.setImageResource(R.drawable.cloud);
                            }else if(wea.equals("Sunny")){
                                weatherPic.setImageResource((R.drawable.sun));
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                last.addValueEventListener(eventListener);
            }else if(getArguments().getInt(ARG_SECTION_NUMBER)==2){
                rootView = inflater.inflate(R.layout.fragment_2, container, false);
                final TextView dryScore = (TextView)rootView.findViewById(R.id.score_number);
                final TextView dryTime = (TextView)rootView.findViewById(R.id.score_time);
                final String[] outAdvice ={"No Laundry today, the weather is bad :(",
                        "It would be difficult to dry with this weather",
                        "It's not the best weather, but give it a try!",
                        "Hurry up, this weather won't last for long!",
                        "Take Advantage of this good weather to do your washing! ( and go Outside! )"};
                final String[] inAdvice = {"Switch on the heater or use a de-humidifier or a fan to improve drying",
                        "Did you know? heaters, de-humidifier, fan or wind can improve drying",
                        "Reduce humidity to avoid bad smelling clothes!",
                        "leave your clothes hanging at home and have a great day!",
                        "Your clothes will be dry in a few hours, enjoy the rest of your day!"};
                final String[] esDryTime ={"N.D",
                        ">4 h",
                        "4 h",
                        "2 h",
                        "1 h"
                };
                Query last = weather[0].orderByKey().limitToLast(1);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            if(isIndoor == false){
                               double rec = Double.parseDouble(ds.child("OutdoorDryScore").getValue().toString());
                               int i = (int) rec;
                               String score = String.format("%.1f",rec);
                               dryScore.setText(score+"/4");
                               dryTime.setText("estimated drying time: "+esDryTime[i]);
                               advice.setText(outAdvice[i]);
                            }else{
                                double rec = Double.parseDouble(ds.child("IndoorDryScore").getValue().toString());
                                int i = (int) rec;
                                String score = String.format("%.1f",rec);
                                dryScore.setText(score+"/4");
                                dryTime.setText("estimated drying time: "+esDryTime[i]);
                                advice.setText(inAdvice[i]);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                last.addValueEventListener(eventListener);
            }else if(getArguments().getInt(ARG_SECTION_NUMBER)==3){
                rootView = inflater.inflate(R.layout.fragment_3, container, false);
                final TextView temp = (TextView)rootView.findViewById(R.id.temperature);
                final TextView hum = (TextView)rootView.findViewById(R.id.humidity);
                final TextView wind = (TextView)rootView.findViewById(R.id.wind);

                Query last = weather[0].orderByKey().limitToLast(1);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            double rec = Double.parseDouble(ds.child("temperature").getValue().toString());
                            double rec2 = Double.parseDouble(ds.child("humidity").getValue().toString());
                            double rec3 = Double.parseDouble(ds.child("wind_speed").getValue().toString());
                            String score = String.format("%.1f",rec);
                            String score2 = String.format("%.1f",rec2);
                            String score3 = String.format("%.1f",rec3);
                            temp.setText("Temperature: "+score+" ℃");
                            hum.setText("Humidity: "+score2+" %");
                            wind.setText("Wind: "+score3+" km/h");
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                last.addValueEventListener(eventListener);
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
