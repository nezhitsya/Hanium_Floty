package com.hanium.floty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hanium.floty.adapter.SettingAdapter;
import com.hanium.floty.fragment.CalendarFragment;
import com.hanium.floty.fragment.DictionaryFragment;
import com.hanium.floty.fragment.HomeFragment;
import com.hanium.floty.fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;
    ViewPager viewPager;
    SettingAdapter mAdapter;
    FrameLayout frameLayout;
    TextView[] dots;
    LinearLayout dotsLayout;
    ConstraintLayout constraintLayout;

    BluetoothAdapter bluetoothAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    public final static int MESSAGE_READ = 2;
    private final static int CONNECTING_STATUS = 3;
    Set<BluetoothDevice> pairedDevices;
    ArrayAdapter<String> bluetoothArrayAdapter;
    ArrayList<String> deviceAddressArray;
    private BluetoothSocket bluetoothSocket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(navigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        onFragmentChanged(new HomeFragment());
        Bundle intent = getIntent().getExtras();

        constraintLayout = findViewById(R.id.viewPager_container);
        viewPager = (ViewPager) findViewById(R.id.slider);
        frameLayout = (FrameLayout) findViewById(R.id.fragment_container);
        dotsLayout = findViewById(R.id.dots);

        constraintLayout.setVisibility(View.GONE);
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);

    }

    private BottomNavigationView.OnNavigationItemReselectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_dictionary:
                    selectedFragment = new DictionaryFragment();
                    break;
                case R.id.nav_settings:
                    selectedFragment = null;
                    constraintLayout.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.GONE);
                    mAdapter = new SettingAdapter(getSupportFragmentManager());
                    viewPager.setAdapter(mAdapter);
                    break;
                case R.id.nav_diary :
                    selectedFragment = new CalendarFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                constraintLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    public void onFragmentChanged(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
    }

    private void addDots(int position) {
        dots = new TextView[5];
        dotsLayout.removeAllViews();

        for(int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorLightGray));

            dotsLayout.addView(dots[i]);
        }

        if(dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.colorGreen));
        }
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void bluetoothOn() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            Log.d("day", "bluetooth on!");
        } else {
            Log.d("day", "already on!")
        }
    }

    private void discoverBluetooth() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
            Log.d("day", "stop discover");
        } else {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothArrayAdapter.clear();
                bluetoothAdapter.startDiscovery();
                registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
                Log.d("day", "start discover");
            }
        }
    }

    final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_DEVICE);
                bluetoothArrayAdapter.add(device.getName() + " " + device.getAddress());
                bluetoothArrayAdapter.notifyDataSetChanged();
            }
        }
    }

}
