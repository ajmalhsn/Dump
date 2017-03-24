package com.example.ajmal.bus;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 1;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private GoogleApiClient mgoogleApiClient;
    TextView lat, longi, text;
    String mLastUpdateTime;
    private static final String TAG = "mainactivity";
    protected String mlat_label,mlong_label,mtime;
    Double latitude,longitude;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lat = (TextView) findViewById(R.id.textView);
        longi = (TextView) findViewById(R.id.textView2);
        text = (TextView) findViewById(R.id.text);
        mlat_label="latitude";
        mlong_label="longitude";
        mtime="time";
        // Write a message to the database
         database = FirebaseDatabase.getInstance();
         myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        myRef.child("user1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap location=new HashMap<String, Object>();
                location.put("longitude",dataSnapshot.getValue(Boolean.parseBoolean("longitude")));
                location.put("latitude",dataSnapshot.getValue(Boolean.parseBoolean("latitude")));
                location.put("timestamp",dataSnapshot.getValue(Boolean.parseBoolean("timestamp")));

                Log.d(TAG, "Value is: " + location.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        buildApiClient();

    }


    protected synchronized void buildApiClient() {
        if (mgoogleApiClient == null) {
            mgoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            createLocationRequest();
        }

    }

    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10* 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

    }

    @Override
    protected void onResume() {
        super.onResume();
        mgoogleApiClient.connect();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mgoogleApiClient.isConnected()) {
            mgoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnected( Bundle ConnectionHint) {
        if (mLastLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            try {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mgoogleApiClient);
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateUI();
            }catch (Exception e){
                Log.d(TAG,e.toString());
            }
            if(mgoogleApiClient.isConnected())
                startLocationUpdates();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
            mLastLocation = location;
            latitude=location.getLatitude();
            longitude=location.getLongitude();
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateUI();
        Toast.makeText(this, "location changed", Toast.LENGTH_SHORT).show();

    }

    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mgoogleApiClient, mLocationRequest, this);
    }



    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG,"ConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }
    protected void updateUI() {
        if(latitude!=null&&longitude!=null&&mLastUpdateTime!=null) {

            myRef.child("user1").child("longitude").setValue(longitude);
            myRef.child("user1").child("latitude").setValue(latitude);
            myRef.child("user1").child("timestamp").setValue(mLastUpdateTime);
            lat.setText(String.format("%s: %f", mlat_label,longitude));
            longi.setText(String.format("%s: %f", mlong_label,latitude));
        }
    }



}
