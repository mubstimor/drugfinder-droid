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
	
	FetchDrugTask fetchDrugsTask = new FetchDrugTask();
	ConnectionDetector cd; 		// Internet detector
	EditText search;

	 @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search_drug);
	    makeProgressBarDisappear();
	    
	    cd = new ConnectionDetector(getApplicationContext());

	    ActionBar actionBar = getSupportActionBar();
	    // add the custom view to the action bar
	    actionBar.setCustomView(R.layout.activity_search);
	    search = (EditText) actionBar.getCustomView().findViewById(R.id.searchfield);
	    search.setOnEditorActionListener(new OnEditorActionListener() {

	      @Override
	      public boolean onEditorAction(TextView v, int actionId,
	          KeyEvent event) {
	        Toast.makeText(SearchDrug.this, "Search triggered", Toast.LENGTH_LONG).show();
	        String drug = search.getText().toString();
	        showProgressBar();
	        downloadData(drug);
	        return false;
	      }
	    });
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
	        | ActionBar.DISPLAY_SHOW_HOME);
	  }

	 private void downloadData(String drugName){
		 if (!cd.isConnectingToInternet()){	            
	            Toast.makeText(getApplicationContext(), "Connect to Internet First",Toast.LENGTH_LONG).show();
	            return;
	        }else{	        	
	      		fetchDrugsTask.listScreen = this;
	      		fetchDrugsTask.execute("http://smsme.info/drugfinder/api/include/searchDrug.php?drug="+drugName.trim());
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
		public void displayList(final ArrayList<HashMap<String, String>> drugItems) {		
			DrugListAdapter adapter = new DrugListAdapter(this, drugItems);
	        ListView listView = (ListView)findViewById(android.R.id.list);
	        listView.setAdapter(adapter);
	        makeProgressBarDisappear();		
	        
	        listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {							
					Toast.makeText(getApplicationContext(), "clicked list item "+drugItems.get(position).get(FetchDrugTask.getKeyName()), Toast.LENGTH_SHORT).show();
					Intent i = new Intent(getApplicationContext(), StoresWithDrug.class);
			        i.putExtra(FetchDrugTask.getKeyDrugid(), drugItems.get(position).get(FetchDrugTask.getKeyDrugid()));
//			        i.putExtra(FetchDrugTask.getKeyDescription(), drugItems.get(position).get(FetchDrugTask.getKeyDescription()));
//			        i.putExtra(FetchDrugTask.getKeyPrescription(),drugItems.get(position).get(FetchDrugTask.getKeyPrescription()));
			        startActivity(i);
	 
				}
			});	
		}
		
}
