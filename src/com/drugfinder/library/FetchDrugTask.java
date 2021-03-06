package com.drugfinder.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class FetchDrugTask extends AsyncTask<String, Void, JSONArray> {

    public ItemListScreen listScreen = null;
    private static final String TAG_DRUGS = "drugs";
	private static final String KEY_DRUGID = "id";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_PRESCRIPTION= "prescription";
	static final String KEY_NAME= "name";
	JSONArray drugs = null;

    @Override
    protected JSONArray doInBackground(String... urls) {

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(urls[0]);
        String responseBody = "{}";
        try {
            HttpResponse response = client.execute(get);
            responseBody = getResponseBody(response);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
        	
           	JSONObject json = new JSONObject(responseBody);
           	drugs = json.getJSONArray(TAG_DRUGS);
            return drugs;           
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONArray todoJsonArray) {
        //List<BusItem> routeItems = new ArrayList<BusItem>();
        ArrayList<HashMap<String, String>> drugList = new ArrayList<HashMap<String, String>>();
       
        for (int i = 0; i < todoJsonArray.length(); i++) {
            try {
            	HashMap<String, String> map = new HashMap<String, String>();
                JSONObject jsonObject = todoJsonArray.getJSONObject(i);
                map.put(getKeyDrugid(), jsonObject.getString(getKeyDrugid()));
                map.put(getKeyDescription(), jsonObject.getString(getKeyDescription()));
                map.put(getKeyPrescription(), jsonObject.getString(getKeyPrescription()));
                map.put(getKeyName(), jsonObject.getString(getKeyName()));
                
             // adding HashList to ArrayList
    			drugList.add(map);
    			
          //      routeItems.add(new BusItem(jsonObject.getString("bus_id"),jsonObject.getString("latitude"),jsonObject.getString("longitude")));
                Log.i("FETCHING DATA", jsonObject.getString(getKeyDrugid()));
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        listScreen.displayList(drugList);
    }

    public static String getKeyName() {
		return KEY_NAME;
	}

	private String getResponseBody(HttpResponse response) {
        StringBuilder builder = new StringBuilder();
        try {
            InputStream body = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(body));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "{}";
        }
    }

	public static String getKeyDrugid() {
		return KEY_DRUGID;
	}

	public static String getKeyDescription() {
		return KEY_DESCRIPTION;
	}

	public static String getKeyPrescription() {
		return KEY_PRESCRIPTION;
	}
}