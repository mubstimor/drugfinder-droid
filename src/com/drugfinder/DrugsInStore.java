package com.drugfinder;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.drugfinder.library.ConnectionDetector;
import com.drugfinder.library.DrugListAdapter;
import com.drugfinder.library.FetchDrugTask;
import com.drugfinder.library.FetchStoreTask;
import com.drugfinder.library.ItemListScreen;

public class DrugsInStore extends SherlockActivity implements ItemListScreen {
	
	FetchDrugTask fetchDrugsTask = new FetchDrugTask();
	ConnectionDetector cd; 		// Internet detector
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_layout);
		
		Intent intent = getIntent();
		String id= intent.getStringExtra(FetchStoreTask.getKeyStoreid());
		
		url = "http://smsme.info/drugfinder/api/include/getDrugsInStore.php?store="+id;;
		
		cd = new ConnectionDetector(getApplicationContext());
		
		 if (!cd.isConnectingToInternet()){	            
	            Toast.makeText(getApplicationContext(), "Connect to Internet First",Toast.LENGTH_LONG).show();
	            return;
	        }else{	        	
	        	fetchDrugsTask.listScreen = this;
	        	fetchDrugsTask.execute(url); 
	        }
	}

	  private void makeProgressBarDisappear() {
	        ProgressBar progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
	        progressBar.setVisibility(View.INVISIBLE);
	    }

	@Override
	public void displayList(final ArrayList<HashMap<String, String>> items) {
		DrugListAdapter adapter = new DrugListAdapter(this, items);
        ListView listView = (ListView)findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        makeProgressBarDisappear();		
        
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {							
				Toast.makeText(getApplicationContext(), "clicked list item "+ items.get(position).get(FetchDrugTask.getKeyName()), Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getApplicationContext(), DrugDetails.class);
//		        i.putExtra(FetchDrugTask.getKeyDrugid(), drugItems.get(position).get(FetchDrugTask.getKeyDrugid()));
//		        i.putExtra(FetchDrugTask.getKeyDescription(), drugItems.get(position).get(FetchDrugTask.getKeyDescription()));
//		        i.putExtra(FetchDrugTask.getKeyPrescription(),drugItems.get(position).get(FetchDrugTask.getKeyPrescription()));
		        startActivity(i);
 
			}
		});	
		
	}
}
