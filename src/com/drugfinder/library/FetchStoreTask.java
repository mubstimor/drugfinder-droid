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

public class FetchStoreTask extends AsyncTask<String, Void, JSONArray> {

    public ItemListScreen listScreen = null;
    private static final String TAG_STORES = "stores";
	private static final String KEY_STOREID = "id";
	private static final String KEY_LATITUDE = "latitude";
	private static final String KEY_LONGITUDE= "longitude";
	private static final String KEY_ADDRESS= "address";
	private static final String KEY_PHONE= "phone";
	private static final String TAG_SUCCESS= "success";
	static final String KEY_NAME= "name";
	JSONArray stores = null;

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
           	if(json.getString(TAG_SUCCESS) == "0"){
           		throw new JSONException("Data not found");
           	}
           	stores = json.getJSONArray(TAG_STORES);
                    
        } catch (JSONException e) {
        	throw new RuntimeException(e);
        }
//        return null;
        return stores;  
    }

    @Override
    protected void onPostExecute(JSONArray todoJsonArray) {
        //List<BusItem> routeItems = new ArrayList<BusItem>();
        ArrayList<HashMap<String, String>> drugList = new ArrayList<HashMap<String, String>>();
       
        for (int i = 0; i < todoJsonArray.length(); i++) {
            try {
            	HashMap<String, String> map = new HashMap<String, String>();
                JSONObject jsonObject = todoJsonArray.getJSONObject(i);
                map.put(getKeyStoreid(), jsonObject.getString(getKeyStoreid()));
                map.put(getKeyLatitude(), jsonObject.getString(getKeyLatitude()));
                map.put(getKeyLongitude(), jsonObject.getString(getKeyLongitude()));
                map.put(getKeyName(), jsonObject.getString(getKeyName()));
                
             // adding HashList to ArrayList
    			drugList.add(map);
    
                Log.i("FETCHING DATA", jsonObject.getString(getKeyStoreid()));
                
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

	public static String getKeyStoreid() {
		return KEY_STOREID;
	}

	public static String getKeyLatitude() {
		return KEY_LATITUDE;
	}

	public static String getKeyLongitude() {
		return KEY_LONGITUDE;
	}

	public static String getKeyAddress() {
		return KEY_ADDRESS;
	}

	public static String getKeyPhone() {
		return KEY_PHONE;
	}
}