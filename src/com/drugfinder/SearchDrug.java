package com.drugfinder;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.drugfinder.library.DrugListAdapter;
import com.drugfinder.library.ItemListScreen;
import com.drugfinder.library.ConnectionDetector;
import com.drugfinder.library.FetchDrugTask;

public class SearchDrug extends SherlockActivity implements ItemListScreen {
	
	FetchDrugTask fetchBusesTask = new FetchDrugTask();
	ConnectionDetector cd; 		// Internet detector

	 @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search_drug);
	    makeProgressBarDisappear();
	    
	    cd = new ConnectionDetector(getApplicationContext());

	    ActionBar actionBar = getSupportActionBar();
	    // add the custom view to the action bar
	    actionBar.setCustomView(R.layout.activity_search);
	    EditText search = (EditText) actionBar.getCustomView().findViewById(R.id.searchfield);
	    search.setOnEditorActionListener(new OnEditorActionListener() {

	      @Override
	      public boolean onEditorAction(TextView v, int actionId,
	          KeyEvent event) {
	        Toast.makeText(SearchDrug.this, "Search triggered", Toast.LENGTH_LONG).show();
	        showProgressBar();
	        downloadData();
	        return false;
	      }
	    });
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
	        | ActionBar.DISPLAY_SHOW_HOME);
	  }

	 private void downloadData(){
		 if (!cd.isConnectingToInternet()){	            
	            Toast.makeText(getApplicationContext(), "Connect to Internet First",Toast.LENGTH_LONG).show();
	            return;
	        }else{
	        	//to get buses along a route
	      		fetchBusesTask.listScreen = this;
	      		fetchBusesTask.execute("http://ptts.herokuapp.com/getbuslocations/"+"1"+"/?format=json");
	        }
	 }
	  private void showProgressBar(){
	        ProgressBar progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
	        progressBar.setVisibility(View.VISIBLE);
	    }
	  
	  private void makeProgressBarDisappear() {
	        ProgressBar progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
	        progressBar.setVisibility(View.INVISIBLE);
	    }
	  
	  @Override
		public void displayList(final ArrayList<HashMap<String, String>> busItems) {		
			DrugListAdapter adapter = new DrugListAdapter(this, busItems);
	        ListView listView = (ListView)findViewById(android.R.id.list);
	        listView.setAdapter(adapter);
	        makeProgressBarDisappear();		
	        
	        listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {							
					Toast.makeText(getApplicationContext(), "clicked list item "+busItems.get(position).get(FetchDrugTask.getKeyBusid()), Toast.LENGTH_SHORT).show();
					Intent i = new Intent(getApplicationContext(), DrugDetails.class);
			        i.putExtra("bus_id", busItems.get(position).get(FetchDrugTask.getKeyBusid()));
			        i.putExtra("latitude", busItems.get(position).get(FetchDrugTask.getKeyLatitude()));
			        i.putExtra("longitude",busItems.get(position).get(FetchDrugTask.getKeyLongitude()));
			        startActivity(i);
	 
				}
			});	
		}
		
}
