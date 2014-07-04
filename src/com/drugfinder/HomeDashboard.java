package com.drugfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeDashboard extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        
        /**
         * Creating all buttons instances
         * */        
        Button btn_searchDrug = (Button) findViewById(R.id.btn_news_feed);        
        Button btn_searchStores = (Button) findViewById(R.id.btn_friends);
        Button btn_allDrugs = (Button) findViewById(R.id.btn_messages);      
        Button btn_nearestStores = (Button) findViewById(R.id.btn_places);
          
        /**
         * Handling all button click events
         * */
                
        btn_searchDrug.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching News Feed Screen
				Intent i = new Intent(getApplicationContext(), SearchDrug.class);
				startActivity(i);
			}
		});
        
        btn_searchStores.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching News Feed Screen
				Intent i = new Intent(getApplicationContext(), SearchStores.class);
				startActivity(i);
			}
		});
        
        btn_allDrugs.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching News Feed Screen
				Intent i = new Intent(getApplicationContext(), AllDrugs.class);
				startActivity(i);
			}
		});
         
        btn_nearestStores.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching News Feed Screen
				Intent i = new Intent(getApplicationContext(), NearestStores.class);
				startActivity(i);
			}
		});
        

    }
}