package com.hanium.floty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.hanium.floty.adapter.ConnectedThread;
import com.hanium.floty.adapter.SettingAdapter;
import com.hanium.floty.fragment.CalendarFragment;
import com.hanium.floty.fragment.DictionaryFragment;
import com.hanium.floty.fragment.HomeFragment;
import com.hanium.floty.fragment.ProfileFragment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

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
    ConnectedThread connectedThread;
    private final static int REQUEST_ENABLE_BT = 1;
    public final static int MESSAGE_READ = 2;
    private final static int CONNECTING_STATUS = 3;
    Set<BluetoothDevice> pairedDevices;
    ArrayList<String> bluetoothArray;
    ArrayList<String> deviceAddressArray;
    private BluetoothSocket bluetoothSocket = null;
    private Handler handler;
    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

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

        String [] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);

        deviceAddressArray = new ArrayList<>();
        bluetoothArray = new ArrayList<>();
//        bluetoothOn();
//        discoverBluetooth();

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == MESSAGE_READ) {
                    String readMessage = null;

                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.d("day", readMessage);
                }

                if (msg.what == CONNECTING_STATUS) {
                    if (msg.arg1 == 1) {
                        Log.d("day", "connected");
                    } else {
                        Log.d("day", "fail to connect");
                    }
                }
            }
        };
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
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Log.d("day", "지원하지 않는 기기");
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                Log.d("day", "bluetooth on!");
            } else {
                Log.d("day", "already on!");
            }
        }
    }

    private void discoverBluetooth() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
            Toast.makeText(this, "연결된 기기가 없습니다.", Toast.LENGTH_SHORT).show();
            Log.d("day", "stop discover");
        } else {
            if (bluetoothAdapter.isEnabled()) {
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
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                bluetoothArray.add(device.getName());
                deviceAddressArray.add(device.getAddress());

                Log.d("day", bluetoothArray.size() + "");

                for (int i = 0; i < bluetoothArray.size(); i++) {
                    Log.d("day", bluetoothArray.toString());
                    if (bluetoothArray.get(i).equals("Galaxy Fit2 (85F6)")) {
                        String deviceName = bluetoothArray.get(i);
                        String deviceAddress = deviceAddressArray.get(i);

                        Log.d("day", "address " + deviceAddress + " name " + deviceName);

                        new Thread() {
                            @Override
                            public void run() {
                                boolean flag = false;

                                Log.d("day", "success");

                                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceName);

                                try {
                                    bluetoothSocket = createBluetoothSocket(device);
                                    bluetoothSocket.connect();
                                } catch (IOException e) {
                                    flag = true;
                                    Log.d("day", "socket fail");
                                }

                                if (flag) {
                                    connectedThread = new ConnectedThread(bluetoothSocket);
                                    connectedThread.start();
                                }

                                if (connectedThread != null) {
                                    connectedThread.write("1");
                                }

//                                try {
//                                    bluetoothSocket.connect();
//                                } catch (IOException e) {
//                                    try {
//                                        flag = true;
//                                        bluetoothSocket.close();
//                                        handler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
//                                    } catch (IOException e1) {
//                                        Log.d("day", "socket create fail");
//                                    }
//                                }
//                                if (!flag) {
//
//                                }
                            }
                        }.start();
                    } else {
                        Log.d("day", "no connection!");
                    }
                }
            } else {
                Log.d("day", "fail");
            }
        }
    };

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BT_MODULE_UUID);
        } catch (Exception e) {
            Log.d("day", "Could not create Insecure RFComm Connection",e);
        }
        return  device.createRfcommSocketToServiceRecord(BT_MODULE_UUID);
    }

}
