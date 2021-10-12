package com.hanium.floty.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hanium.floty.fragment.LightSettingFragment;
import com.hanium.floty.fragment.SettingsFragment;
import com.hanium.floty.fragment.TemperatureSettingFragment;
import com.hanium.floty.fragment.WaterSettingFragment;

public class SettingAdapter extends FragmentPagerAdapter {

    public SettingAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0 :
                return new SettingsFragment();
            case 1 :
                return new WaterSettingFragment();
            case 2 :
                return new LightSettingFragment();
            case 3:
                return new TemperatureSettingFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
