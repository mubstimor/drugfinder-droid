package com.drugfinder;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.drugfinder.library.ConnectionDetector;
import com.drugfinder.library.FetchDrugTask;
import com.drugfinder.library.FetchStoreTask;
import com.drugfinder.library.ItemListScreen;
import com.drugfinder.library.StoreListAdapter;

public class StoresWithDrug extends SherlockActivity implements ItemListScreen {
	
	FetchStoreTask fetchStoresTask = new FetchStoreTask();
	ConnectionDetector cd; 		// Internet detector
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_layout);
		
		Intent intent = getIntent();
		String id= intent.getStringExtra(FetchDrugTask.getKeyDrugid());
	
		url = "http://smsme.info/drugfinder/api/include/getStoreWithDrug.php?drug="+id.trim();
		Log.i("Logging from another intent", url);
				
		cd = new ConnectionDetector(getApplicationContext());
		
		 if (!cd.isConnectingToInternet()){	            
	            Toast.makeText(getApplicationContext(), "Connect to Internet First",Toast.LENGTH_LONG).show();
	            return;
	        }else{
	        	//to get buses along a route
	        	fetchStoresTask.listScreen = this;
	        	fetchStoresTask.execute(url); 
	        }
	}

	  private void makeProgressBarDisappear() {
	        ProgressBar progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
	        progressBar.setVisibility(View.INVISIBLE);
	    }

	@Override
	public void displayList(final ArrayList<HashMap<String, String>> items) {
		StoreListAdapter adapter = new StoreListAdapter(this, items);
        ListView listView = (ListView)findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        makeProgressBarDisappear();		
        
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {							
				Toast.makeText(getApplicationContext(), "clicked list item "+ items.get(position).get(FetchStoreTask.getKeyName()), Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getApplicationContext(), StoreDetails.class);
				i.putExtra(FetchStoreTask.getKeyAddress(), items.get(position).get(FetchStoreTask.getKeyAddress()));
		        i.putExtra(FetchStoreTask.getKeyLatitude(), items.get(position).get(FetchStoreTask.getKeyLatitude()));
		        i.putExtra(FetchStoreTask.getKeyLongitude(),items.get(position).get(FetchStoreTask.getKeyLongitude()));
		        i.putExtra(FetchStoreTask.getKeyName(),items.get(position).get(FetchStoreTask.getKeyName()));
		        i.putExtra(FetchStoreTask.getKeyPhone(),items.get(position).get(FetchStoreTask.getKeyPhone()));
		        i.putExtra(FetchStoreTask.getKeyStoreid(),items.get(position).get(FetchStoreTask.getKeyStoreid()));
		        startActivity(i);
 
			}
		});	
		
	}
}
