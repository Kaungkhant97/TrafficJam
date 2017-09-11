package com.unihackchallenge.mmtrafficreport;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unihackchallenge.mmtrafficreport.api.RetrofitClient;
import com.yayandroid.locationmanager.LocationBaseActivity;
import com.yayandroid.locationmanager.LocationConfiguration;
import com.yayandroid.locationmanager.LocationManager;
import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.LogType;
import com.yayandroid.locationmanager.constants.ProviderType;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends LocationBaseActivity implements OnMapReadyCallback, Callback<LocationResponse> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog progressDialog;

    private Location mlocation;
    private CoordinatorLayout mainLayout;
    private GoogleMap mMap;
    private boolean isGpsStatusOk = true;
    private Button btn_report;
    private RadioGroup rdgroup_status;
    private Toolbar toolbar;
    private RadioGroup rdgroup_interval;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();

        checkToshowButtonOrNot();

        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getLocation();
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlocation != null) {

                    String address = getAddress(mlocation.getLatitude(), mlocation.getLongitude());
                    if (isGpsStatusOk) {
                        int radioButtonID = rdgroup_status.getCheckedRadioButtonId();
                        RadioButton checkedRadioButton = (RadioButton) rdgroup_status.findViewById(radioButtonID);

                        if (checkedRadioButton != null) {
                            String status = getcheckButtonData(radioButtonID);

                            location trafficJamLocation = new
                                    location(address
                                    , mlocation.getLatitude() + ""
                                    , mlocation.getLongitude() + ""
                                    , "public"
                                    , status
                                    , ""
                                    , "public"
                            );
                            btn_report.setVisibility(View.GONE);
                            rdgroup_status.setVisibility(View.GONE);
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Alert")
                                    .setMessage("Please wait 3 min until you can make a new report")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            postDataToServer(trafficJamLocation);

                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(mlocation.getLatitude(), mlocation.getLongitude()))
                                    .icon(BitmapDescriptorFactory.fromResource(stausTodrawableid(status))));


                        } else {
                            Toast.makeText(MainActivity.this, "Please select one emotion to report", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Cannot find gps Connection", Toast.LENGTH_SHORT).show();
                        getLocation();
                    }


                }

            }
        });
        LocationManager.setLogType(LogType.GENERAL);


    }

    private String getcheckButtonData(int radioButtonID) {
        String status = getString(R.string.statusveryBad);
        switch (radioButtonID) {
            case R.id.rbtn_verybad:
                status = getString(R.string.statusveryBad);
                break;
            case R.id.rbtn_ok:
                status = getString(R.string.statusOk);
                break;
            case R.id.rbtn_bad:
                status = getString(R.string.statusBad);
                break;
        }

        return status;
    }

    private void checkToshowButtonOrNot() {
        long lastsubmittedTime = Long.parseLong(SPrefHelper.getString(this, Constants.lASTSUBMITTEDTIME, "0"));
        long expectedTime = lastsubmittedTime + Constants.REPORT_BUTTON_HIDE_INTERVAL;

        long currentTime = Calendar.getInstance().getTimeInMillis();
        Log.e(TAG, "onCreate: " + lastsubmittedTime + " " + expectedTime + " " + " " + currentTime);
        if (currentTime > expectedTime) {
            btn_report.setVisibility(View.VISIBLE);
        } else {
            btn_report.setVisibility(View.GONE);
            rdgroup_status.setVisibility(View.GONE);

        }
    }

    private void init() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btn_report = (Button) findViewById(R.id.btn_report);
        mainLayout = (CoordinatorLayout) findViewById(R.id.content_main);
        rdgroup_status = (RadioGroup) findViewById(R.id.rdgroup_status);
        rdgroup_interval = (RadioGroup) findViewById(R.id.rdgroup_interval);
        RadioButton rbtn_15min = (RadioButton) findViewById(R.id.rbtn_fifteenmins);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        int checkIdofInterval = SPrefHelper.getInteger(this, Constants.REQUEST_TRAFFICREPORT_INTERVAL, R.id.rbtn_onehr);

        rdgroup_interval.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.e(TAG, "onCheckedChanged: " + checkedId);
                String interval = getInterval(checkedId);

                getNetworkData(interval);

                SPrefHelper.putInteger(MainActivity.this, Constants.REQUEST_TRAFFICREPORT_INTERVAL, checkedId);
            }
        });
        ((RadioButton) findViewById(checkIdofInterval)).setChecked(true);
    }

    private String getInterval(int checkedId) {
        String num = "4";

        switch (checkedId) {

            case R.id.rbtn_fifteenmins:
                num = "1";
                break;
            case R.id.rbtn_thirtymins:
                num = "2";
                break;

            case R.id.rbtn_onehr:
                num = "4";
                break;
            case R.id.rbtn_twohr:
                num = "8";
                break;
            default:
                num = "4";
                break;
        }
        return num;
    }

    private void postDataToServer(location trafficJamLocation) {
        RetrofitClient.getInstance().getService().postLocation(trafficJamLocation).enqueue(new Callback<location>() {
            @Override
            public void onResponse(Call<location> call, Response<location> response) {
                if (response.isSuccessful()) {
                    String currentTime = Calendar.getInstance().getTimeInMillis() + "";
                    SPrefHelper.putString(MainActivity.this, Constants.lASTSUBMITTEDTIME, currentTime);
                }
            }

            @Override
            public void onFailure(Call<location> call, Throwable t) {

            }
        });

    }

    private void getNetworkData(String interval) {
        //onehr ago recent traffic
        RetrofitClient.getInstance().getService().getRecentTraffic(interval).enqueue(this);
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
                isGpsStatusOk = false;
                break;
            }
            case FailType.GP_SERVICES_NOT_AVAILABLE:
            case FailType.GP_SERVICES_CONNECTION_FAIL: {

                isGpsStatusOk = false;
                break;
            }
            case FailType.NETWORK_NOT_AVAILABLE: {
                isGpsStatusOk = false;
                break;
            }
            case FailType.TIMEOUT: {
                isGpsStatusOk = false;
                break;
            }
            case FailType.GP_SERVICES_SETTINGS_DENIED: {
                isGpsStatusOk = false;
                break;
            }
            case FailType.GP_SERVICES_SETTINGS_DIALOG: {
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
        String address = "";
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);

            Address obj = addresses.get(0);
            address = obj.getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return address;


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

    }

    @Override
    public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
        mMap.clear();
        if (response.body().getData().size() > 0) {
            List<location> locationList = response.body().getData();

            if (mMap != null) {


                for (location l : locationList) {
                    Double lat = Double.parseDouble(l.getLatitude());
                    Double lag = Double.parseDouble(l.getLongitude());


                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lag))
                            .icon(BitmapDescriptorFactory.fromResource(stausTodrawableid(l.getStatus()))));

                }
            }

        }
    }

    @Override
    public void onFailure(Call<LocationResponse> call, Throwable t) {
        Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            int checkedId = SPrefHelper.getInteger(this, Constants.REQUEST_TRAFFICREPORT_INTERVAL, R.id.rbtn_onehr);
            String interval = getInterval(checkedId);
            getNetworkData(interval);
        }
        return super.onOptionsItemSelected(item);
    }

    private int stausTodrawableid(String status) {
        int drawableresource = R.drawable.badmarker;
        String vbad = getString(R.string.statusveryBad);
        String bad = getString(R.string.statusBad);
        String ok = getString(R.string.statusOk);

        if(vbad.equals(status)){
            drawableresource =R.drawable.badmarker;

        }
        if(bad.equals(status)){
            drawableresource =R.drawable.mehmarker;
        }

        if(ok.equals(status)){
            drawableresource =R.drawable.goodmarker;
        }


        return drawableresource;
    }
}


