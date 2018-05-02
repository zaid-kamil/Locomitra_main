package com.digipodium.derish.locomitra;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.os.ResultReceiver;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.EasyPermissions;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    String state;
    String city;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<FireModel> list;
    String uname;
    String phone;
    RecyclerView recycle;
    LocationManager locationManager;
    String lng, lat;
    public FirebaseDatabase taskdb;
    private String areacode;
    String state1;
    String city1;
    EditText search;
    OnlineContactAdapter adapter;
    TextView userId;
    TextView useremail;
    ImageView searchIcon;
    public String area;
    public String area1;
    ArrayList<FireModel> newlist;
    private String latt;
    private String lngg;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            EasyPermissions.requestPermissions(this, "Location_Permission", 4477, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildalert();
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
        userId=findViewById(R.id.userId);
        useremail=findViewById(R.id.useremail);
        searchIcon = findViewById(R.id.searchicon);
        search = findViewById(R.id.search);
        recycle = findViewById(R.id.rvonline);
        database = FirebaseDatabase.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef = database.getReference("User_Contact").child(uid);
        initUI();
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.SEND_SMS)) {
            EasyPermissions.requestPermissions(this, "Contact_Permission", 4477, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.SEND_SMS);
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            try {

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            String name = user.getDisplayName();
            String email = user.getEmail();


        }

    }

    private void buildalert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn on GPS").setCancelable(false).setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            EasyPermissions.requestPermissions(this, "Location_Permission", 4477, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
            @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                this.lat = String.valueOf(lat);
                this.lng = String.valueOf(lng);
                Toast.makeText(this, this.lat + " " + this.lng, Toast.LENGTH_SHORT).show();
                getAddressFromLocation(lat, lng, this, new GeocoderHandler());


            }


        }

    }

    public class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            Toast.makeText(Home.this, locationAddress, Toast.LENGTH_LONG).show();
        }
    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list = new ArrayList<FireModel>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                      String child= snapshot.getKey();
                     gotouser(child);


                }



                searchIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(Home.this, Map.class);
                        startActivity(it);
                    }
                });

            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               Intent intent=new Intent(Home.this,Map.class);
               intent.putExtra("abcdlist",list);
               intent.putExtra("area",area1);
                  startActivity(intent);

            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    filter(s.toString());
                } catch (Exception e) {
                }





            }
        });
    }

    private void gotouser(final String child ) {
        final int[] c = {0};
       DatabaseReference dbref = database.getReference("users");
       dbref.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   if (child.equals(snapshot.getKey().toString())) {
                       FireModel value = snapshot.getValue(FireModel.class);
                       FireModel fire = new FireModel();
                       uname = value.getName();
                       phone = value.getPhone();
                       state1 = value.getState();
                       city1 = value.getCity();
                       area1 = value.getArea();
                       latt=value.getLat();
                       lngg=value.getLng();
                       fire.setLat(latt);
                       fire.setLngg(lngg);
                       fire.setName(uname);
                       fire.setPhone(phone);
                       fire.setLocation(state1);
                       fire.setAddress(area1 + ", " + city1 + ".");
                       list.add(fire);
                       c[0] =1;
                       break;
                   }

               }
               if(c[0]==0)
                   Toast.makeText(Home.this, "No users", Toast.LENGTH_SHORT).show();
               adapter = new OnlineContactAdapter(list, Home.this);
               recycle.setLayoutManager(new LinearLayoutManager(Home.this));
               recycle.setItemAnimator(new DefaultItemAnimator());
               recycle.setAdapter(adapter);
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

    }

    public void filter(String s) {
        newlist = new ArrayList<>();
        try {
            for (FireModel item : list) {
                if ((item.getLocation() + item.getAddress()).toLowerCase().contains(s.toLowerCase())) {
                    newlist.add(item);
                }
            }
            adapter.filterList(newlist);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent it = new Intent(Home.this, profileactivity.class);
            startActivity(it);

        } else if (id == R.id.nav_accept_invite) {
            Intent it = new Intent(Home.this, InviteReceive.class);
            startActivity(it);
        } else if (id == R.id.nav_share) {
            Intent it = new Intent(Home.this, AllContacts.class);
            startActivity(it);
        } else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Home.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

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


            // Show a toast message if an address was found.
            if (resultCode == FetchAddressIntentService.Constants.SUCCESS_RESULT) {

            }

        }
    }

    private static final String TAG = "LocationAddress";

    public void getAddressFromLocation(final double latitude, final double longitude,
                                       final Context context, final Handler handler) {


        Thread thread = new Thread() {


            @Override
            public void run() {

                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");
                        }
                        sb.append(address.getLocality()).append("\n");
                        sb.append(address.getPostalCode()).append("\n");
                        sb.append(address.getCountryName()).append("\n");
                        sb.append(address.getAddressLine(0)).append("\n");
                        sb.append(address.getSubLocality()).append("\n");
                        area = address.getSubLocality();
                        state = address.getAdminArea();
                        city = address.getLocality();
                        areacode = address.getPostalCode();

                        result = sb.toString();
                        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        taskdb = FirebaseDatabase.getInstance();


                        taskdb.getReference().child("users").child(uid).child("lat").setValue(lat);
                        taskdb.getReference().child("users").child(uid).child("lng").setValue(lng);
                        taskdb.getReference().child("users").child(uid).child("state").setValue(state);
                        taskdb.getReference().child("users").child(uid).child("city").setValue(city);
                        taskdb.getReference().child("users").child(uid).child("areacode").setValue(areacode);
                        taskdb.getReference().child("users").child(uid).child("area").setValue(area);


                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Latitude: " + latitude + " Longitude: " + longitude +
                                "\n\nAddress:\n" + result;
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Latitude: " + latitude + " Longitude: " + longitude +
                                "\n Unable to get address for this lat-long.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                 message.sendToTarget();
                }

            }
        };
        thread.start();

    }
}
