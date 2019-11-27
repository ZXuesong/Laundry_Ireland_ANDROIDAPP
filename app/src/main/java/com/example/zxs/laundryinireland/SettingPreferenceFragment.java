package com.example.zxs.laundryinireland;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import androidx.fragment.app.Fragment;

public class SettingPreferenceFragment extends  PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_pre);
    }
}
