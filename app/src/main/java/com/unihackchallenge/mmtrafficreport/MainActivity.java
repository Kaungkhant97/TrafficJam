package com.unihackchallenge.mmtrafficreport;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yayandroid.locationmanager.LocationBaseActivity;
import com.yayandroid.locationmanager.LocationConfiguration;
import com.yayandroid.locationmanager.LocationManager;
import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.LogType;
import com.yayandroid.locationmanager.constants.ProviderType;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends LocationBaseActivity implements OnMapReadyCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog progressDialog;
    private TextView locationText;
    private Location mlocation;
    private GoogleMap mMap;
    private boolean isGpsStatusOk = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationText = (TextView) findViewById(R.id.locationText);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        getLocation();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlocation != null) {

                    String s = mlocation.getLatitude() + " " + mlocation.getLongitude();
                    locationText.setText(s);
                    String address = getAddress(mlocation.getLatitude(), mlocation.getLongitude());
                    if(isGpsStatusOk){

                        ReportDialog dialog =  ReportDialog.newInstance(address);
                        Bundle args = new Bundle();
                        args.putString( ReportDialog.ADDRESS,address);
                        dialog.setArguments(args);

                        dialog.show(getSupportFragmentManager(),"reportfragment");
                    }
                }

            }
        });
        LocationManager.setLogType(LogType.GENERAL);


    }

    @Override
    public LocationConfiguration getLocationConfiguration() {
        return new LocationConfiguration()
                .keepTracking(true)
                .askForGooglePlayServices(true)
                .setMinAccuracy(200.0f)
                .setWaitPeriod(ProviderType.GOOGLE_PLAY_SERVICES, 5 * 1000)
                .setWaitPeriod(ProviderType.GPS, 10 * 1000)
                .setWaitPeriod(ProviderType.NETWORK, 5 * 1000)
                .setGPSMessage("Would you mind to turn GPS on?")
                .setRationalMessage("Gimme the permission!");
    }

    @Override
    public void onLocationChanged(Location location) {
        dismissProgress();
        mlocation = location;
        LatLng myLocation = null;
        if (mlocation != null) {
            myLocation = new LatLng(mlocation.getLatitude(),
                    mlocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                    16));
        }


    }

    @Override
    public void onLocationFailed(int failType) {
        dismissProgress();

        switch (failType) {
            case FailType.PERMISSION_DENIED: {
                locationText.setText("Couldn't get location, because user didn't give permission!");
                isGpsStatusOk = false;
                break;
            }
            case FailType.GP_SERVICES_NOT_AVAILABLE:
            case FailType.GP_SERVICES_CONNECTION_FAIL: {
                locationText.setText("Couldn't get location, because Google Play Services not available!");
                isGpsStatusOk = false;
                break;
            }
            case FailType.NETWORK_NOT_AVAILABLE: {
                locationText.setText("Couldn't get location, because network is not accessible!");
                isGpsStatusOk = false;
                break;
            }
            case FailType.TIMEOUT: {
                locationText.setText("Couldn't get location, and timeout!");
                isGpsStatusOk = false;
                break;
            }
            case FailType.GP_SERVICES_SETTINGS_DENIED: {
                locationText.setText("Couldn't get location, because user didn't activate providers via settingsApi!");
                isGpsStatusOk = false;
                break;
            }
            case FailType.GP_SERVICES_SETTINGS_DIALOG: {
                locationText.setText("Couldn't display settingsApi dialog!");
                isGpsStatusOk = false;
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getLocationManager().isWaitingForLocation()
                && !getLocationManager().isAnyDialogShowing()) {
            displayProgress();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        dismissProgress();
    }

    private void displayProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
            progressDialog.setMessage("Getting location...");
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address obj = addresses.get(0);
        String address = obj.getAddressLine(0);



        return address;


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);




    }
}


