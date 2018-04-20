package com.digipodium.derish.locomitra;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.os.ResultReceiver;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Toast;

import com.digipodium.derish.locomitra.Models.Location_Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    private GoogleMap mMap;

    private static final String REQUESTING_LOCATION_UPDATES_KEY = "location";
    private boolean isPermitted = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private String mAddressOutput;
    private AddressResultReceiver mResultReceiver;
    private double lat;
    private double lng;
    private GoogleMap googleMap;
    public DatabaseReference taskdb;
    FirebaseDatabase fbase = FirebaseDatabase.getInstance();
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
         uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (EasyPermissions.hasPermissions(this, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            isPermitted = true;
            mResultReceiver = new MapActivity.AddressResultReceiver(new Handler());
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            //fetch address
                            if (Geocoder.isPresent()) {
                                startIntentService(location);
                            }
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                            FloatingActionButton fbb = findViewById(R.id.fbb);
                            fbb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onMapReady();
                                }
                            });


                        }

                    }
                }

                ;
            };
            createLocationRequest();
        } else {
            EasyPermissions.requestPermissions(this, "location permission", 78, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    public void onMapReady() {
        mMap = googleMap;
        Toast.makeText(MapActivity.this, "address found", Toast.LENGTH_SHORT).show();
        Toast.makeText(MapActivity.this, String.valueOf(lat) + "" + String.valueOf(lng), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, mAddressOutput, Toast.LENGTH_SHORT).show();

        Location_Task lcntsk=new Location_Task(String.valueOf(lat),String.valueOf(lng),"shubham",true);
        taskdb = fbase.getReference("Curr_Location").child(uid);
        taskdb.push().setValue(lcntsk).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MapActivity.this, "Success", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MapActivity.this, "Location Not Fetched", Toast.LENGTH_SHORT).show();
                }
            }
        });
        LatLng myLocation = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(myLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        isPermitted = true;
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        isPermitted = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPermitted) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isPermitted) {
            if (mFusedLocationClient != null) {
                stopLocationUpdates();
            }
        }
    }

    private void startLocationUpdates() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(30000);
        mLocationRequest.setFastestInterval(15000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startIntentService(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
    }

    class AddressResultReceiver extends ResultReceiver {
        @SuppressLint("RestrictedApi")
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultData == null) {
                return;
            }

            // Display the address string
            // or an error message sent from the intent service.
            mAddressOutput = resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY);
            if (mAddressOutput == null) {
                mAddressOutput = "";
            }
            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == FetchAddressIntentService.Constants.SUCCESS_RESULT) {

            }

        }
    }

    private void displayAddressOutput() {
        //TEXTVIEW

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

}
