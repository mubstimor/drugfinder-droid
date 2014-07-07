package com.drugfinder;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.drugfinder.library.ConnectionDetector;
import com.drugfinder.library.FetchStoreTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class StoreDetails extends FragmentActivity implements LocationListener, LocationSource{

    GoogleMap mGoogleMap;
    double mLatitude=0;
    double mLongitude=0;

    
    LocationManager locationManager;
    ConnectionDetector cd; 		// Internet detector
    String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_details);

        Intent in = getIntent();

        String address = in.getStringExtra(FetchStoreTask.getKeyAddress());        
        String latitude = in.getStringExtra(FetchStoreTask.getKeyLatitude());
        String longitude = in.getStringExtra(FetchStoreTask.getKeyLongitude());
        String name = in.getStringExtra(FetchStoreTask.getKeyName());
        String phone = in.getStringExtra(FetchStoreTask.getKeyPhone());
        

        cd = new ConnectionDetector(getApplicationContext());

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();

        // Getting LocationManager object from System Service LOCATION_SERVICE
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else { // Google Play Services are available

            // Getting reference to the SupportMapFragment
            SupportMapFragment fragment = ( SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting Google Map
            mGoogleMap = fragment.getMap();

            // Enabling MyLocation in Google Map
            mGoogleMap.setMyLocationEnabled(true);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            if(provider != null){

                // Getting Current Location From GPS
                Location location = locationManager.getLastKnownLocation(provider);

                if(location!=null){
                    onLocationChanged(location);
                }

                locationManager.requestLocationUpdates(provider, 20000, 0, this);

            }

        }//end else

        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latitude) ,Double.parseDouble(longitude))).title(name).snippet("Address: "+address));

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(locationManager != null)
        {
            boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(gpsIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this);
            }
            else if(networkIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
            }
            else
            {
                //Show an error dialog that GPS is disabled...               
            }
        }
		  
		  /* CHECK FOR CONNECTION */
        // Check if Internet present
        if (!cd.isConnectingToInternet()){
            //alert.showNoConnectionDialog(CrimeDetails.this);
            Toast.makeText(getApplicationContext(), "Connect to Internet First",Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13));

        // mLatitude, mLongitude

    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void activate(OnLocationChangedListener arg0) {}

    @Override
    public void deactivate() {}

}

