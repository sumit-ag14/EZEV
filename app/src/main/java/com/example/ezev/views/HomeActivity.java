package com.example.ezev.views;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ezev.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import kotlin.experimental.BitwiseOperationsKt;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private ActionBarDrawerToggle drawerToggle;


    /* Location Access*/
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    SensorManager sensorManager;
    Sensor magFieldSensor, accelerometer;
    SensorEventListener sensorListener;
    LocationListener locationListener;
    LocationManager locationManager;
    TextView orientationView, locationView;
    private float[] gravityValues = new float[3];
    private float[] geoMagnetValues = new float[3];
    private float[] orientation = new float[3];
    private float[] rotationMatrix = new float[9];
    private final static int ALL_PERMISSIONS_RESULT = 101;
    /* Location Access*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // This will display an Up icon (<-), we will replace it with hamburger later
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_dehaze_24 );
        Class fragmentClass=HomeFragment.class;
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        //Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent,
                new HomeFragment()).commit();
/* Location*/
        sensorManager = (SensorManager)
                getSystemService(Context.SENSOR_SERVICE);
//        magFieldSensor = sensorManager
//                .getDefaultSensor(TYPE_MAGNETIC_FIELD);
//        accelerometer = sensorManager
//                .getDefaultSensor(TYPE_ACCELEROMETER);
//        sensorListener = new MySensorEventListener();
        locationListener = new MyLocationListener();
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
       //orientationView =(TextView) findViewById(R.id.locationView);
        /* Location*/

    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass=HomeFragment.class;
        switch(menuItem.getItemId()) {
            case R.id.nav_home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_transactions:
                fragmentClass = TransactionList.class;
                break;
            case R.id.nav_cs:
                Intent intent1=new Intent(this,VendorList.class);
                startActivity(intent1);
//                fragmentClass = About_us.class;
                break;
            case R.id.nav_joinUs:
                fragmentClass = About_us.class;
                break;
            case R.id.logout: {
//                BookNowFragment.userId = null;
//                VendorHomeFragment.userId = null;
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
            }
                break;
            default:
                fragmentClass = HomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // Bottom navigation
    private NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.transaction:
                            selectedFragment = new TransactionList();// replace with Transation fragment
                            break;
                        case R.id.profile:
                            selectedFragment = new MyProfile();// replace with Profile fragment
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent,
                            selectedFragment).commit();
                    return true;
                }
            };
    @Override
    public void onBackPressed() {

    }


    // Location
    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            permissions.add(ACCESS_FINE_LOCATION);
            permissions.add(ACCESS_COARSE_LOCATION);
            permissionsToRequest = findUnAskedPermissions(permissions);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0)
                    requestPermissions((String[]) permissionsToRequest.toArray(new

                            String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);

            }
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                10, 1, locationListener);

    }
    @Override
    protected void onPause() {
        super.onPause();
//        sensorManager.unregisterListener(sensorListener);
        locationManager.removeUpdates(locationListener);
    }
//    class MySensorEventListener implements SensorEventListener {
//        @Override
//        public void onSensorChanged(SensorEvent event) {
//            int sensorEventType = event.sensor.getType();
//            if (sensorEventType == Sensor.TYPE_ACCELEROMETER) {
//                System.arraycopy
//                        (event.values, 0, gravityValues, 0, 3);
//
//            } else if (sensorEventType ==
//                    Sensor.TYPE_MAGNETIC_FIELD) {
//                System.arraycopy
//                        (event.values, 0, geoMagnetValues, 0, 3);
//
//            } else {
//                return;
//            }
//            if (SensorManager.getRotationMatrix(rotationMatrix,
//                    null, gravityValues, geoMagnetValues)) {
//                SensorManager.getOrientation(rotationMatrix,
//                        orientation);
//
////                orientationView.setText("Yaw: " + orientation[0] + "\n"+ "Pitch: " + orientation[1] + "\n"+ "Roll: " + orientation[2]);
//
//            }
//        }
//        @Override
//        public void onAccuracyChanged(Sensor sensor,
//                                      int accuracy) {
//
//            if (accuracy <= 1) {
//                Toast.makeText(HomeActivity.this, "Please shake the " +
//                                "device in a figure eight pattern to " +
//                                "improve sensor accuracy!", Toast.LENGTH_LONG)
//                        .show();
//
//            }
//        }
//    }
    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            String locationString=
                    "Latitude: " + location.getLatitude() +",Longitude: " + location.getLongitude();
                   //locationView.setText(locationString);
                      System.out.println(locationString);

        }
        @Override
        public void onProviderDisabled(String provider) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onStatusChanged(String provider,

                                    int status, Bundle extras) {

        }
    }
    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();
        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }
        return result;
    }
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) ==

                        PackageManager.PERMISSION_GRANTED);

            }
        }
        return true;
    }
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    @TargetApi(Build.VERSION_CODES.M)

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String)

                                permissionsRejected.get(0))) {

                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,

                                                            int which) {

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                                requestPermissions((String[])
                                                                permissionsRejected.toArray(new String[permissionsRejected.size()]),
                                                        ALL_PERMISSIONS_RESULT);

                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener
            okListener) {
        new AlertDialog.Builder(HomeActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }
}