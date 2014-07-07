package com.drugfinder;

import java.util.ArrayList;
import java.util.HashMap;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.drugfinder.library.ConnectionDetector;
import com.drugfinder.library.FetchStoreTask;
import com.drugfinder.library.ItemListScreen;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearestStores extends SherlockFragmentActivity implements ItemListScreen, LocationSource, android.location.LocationListener {
	
	FetchStoreTask fetchStoresTask = new FetchStoreTask();
	ConnectionDetector cd; 		// Internet detector
	String url;
	private GoogleMap mMap;
	private OnLocationChangedListener mListener;
	private LocationManager locationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		url = "http://smsme.info/drugfinder/api/include/getAllStores.php";		
		cd = new ConnectionDetector(getApplicationContext());
		
	
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
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
		
		 if (!cd.isConnectingToInternet()){	            
	            Toast.makeText(getApplicationContext(), "Connect to Internet First",Toast.LENGTH_LONG).show();
	            return;
	        }else{
	        	//to get buses along a route
	        	fetchStoresTask.listScreen = this;
	        	fetchStoresTask.execute(url); 
	        }
	}

	 @Override
	    protected void onResume() {
	        super.onResume();
	        setUpMapIfNeeded();
	        if(locationManager != null)
	        {
	            mMap.setMyLocationEnabled(true);
	        }
	    }

	 @Override
	    public void onPause()
	    {
	        if(locationManager != null)
	        {
	            locationManager.removeUpdates(this);
	        }
	         
	        super.onPause();
	    }
	 
	   private void setUpMapIfNeeded() {
	        // Do a null check to confirm that we have not already instantiated the map.
	        if (mMap == null) {
	            // Try to obtain the map from the SupportMapFragment.
	            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	            
	            // Check if we were successful in obtaining the map.
	            if (mMap != null) {
	                setUpMapIfNeeded();
	            }
	            
	            //This is how you register the LocationSource
	            mMap.setLocationSource(this);
	        }
	    }
	   
	@Override
	public void displayList(final ArrayList<HashMap<String, String>> items) {
	        
        for(HashMap<String, String> hashMap:items){        	
        	  Marker colored = mMap.addMarker(new MarkerOptions()
              .position(new LatLng(Double.parseDouble(hashMap.get(FetchStoreTask.getKeyLatitude())) ,Double.parseDouble(hashMap.get(FetchStoreTask.getKeyLongitude()))))
              .title(hashMap.get(FetchStoreTask.getKeyName()))
              .snippet(hashMap.get(FetchStoreTask.getKeyAddress()))
              .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        	  
        	  colored.showInfoWindow();
        	
        }
	}

	@Override
	public void activate(OnLocationChangedListener arg0) {}

	@Override
	public void deactivate() {}

	@Override
	public void onLocationChanged(Location arg0) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}
