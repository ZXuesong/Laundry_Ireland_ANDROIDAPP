package com.example.zxs.laundryinireland;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import androidx.appcompat.app.AppCompatActivity;


public class SettingActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.set_pre, new SettingPreferenceFragment());
        fragmentTransaction.commit();
    }
}
