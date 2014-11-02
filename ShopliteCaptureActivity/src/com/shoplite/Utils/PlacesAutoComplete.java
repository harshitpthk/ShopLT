package com.shoplite.Utils;



import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.database.MatrixCursor;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.zxing.client.android.CaptureActivity;
import com.shoplite.connection.ServiceProvider;
import com.shoplite.models.PlacePrediction;

import eu.livotov.zxscan.R;

public class PlacesAutoComplete {

	

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";
	private static final String API_KEY = "AIzaSyDYYRgTGk777pOLVGQgqyYA3QtFKF9BMbw";
	public  static ServiceProvider serviceProvider = null;
	public static JsonArray placesPrediction = new JsonArray();
	public static ArrayList<PlacePrediction> placesList;
	private static String[] columnNames = {"_id","description"};
	public static MatrixCursor suggestionCursor  ;
	
	public void autocomplete(String input) {
		if(CaptureActivity.decorView != null && !CaptureActivity.isProgressBarAdded){
			CaptureActivity.isProgressBarAdded = true;
			CaptureActivity.decorView.addView(CaptureActivity.progressBar);
		}
		placesList = new ArrayList<PlacePrediction>();
		suggestionCursor  = new MatrixCursor(columnNames);
	    StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
	    sb.append("?key=" + API_KEY);
	    sb.append("&types=(regions)");
	    sb.append("&location="+Globals.current_location.getLatitude().toString()+
	    		","+Globals.current_location.getLongitude().toString());
        sb.append("&components=country:in");
        sb.append("&input=" + input);

        String url = sb.toString();
	    RestAdapter restAdapter = new RestAdapter.Builder()
		.setEndpoint(url)		
		.setLogLevel(RestAdapter.LogLevel.FULL)
		.build();
		
	    serviceProvider = restAdapter.create(ServiceProvider.class);
		
		serviceProvider.getPlacesSuggestion(new Callback<JsonObject>(){

			@Override
			public void failure(RetrofitError response) {
				// TODO Auto-generated method stub
				Log.e("Places API Error", response.toString());
			}

			@Override
			public void success(JsonObject result, Response response) {
				Log.e("Places API Success", response.toString());
				placesPrediction = result.getAsJsonArray("predictions");
				for (int i = 0; i < placesPrediction.size(); i++) {
					Gson gson = new Gson();
					PlacePrediction pl = gson.fromJson(placesPrediction.get(i), PlacePrediction.class);
					placesList.add(pl);
					suggestionCursor.addRow(new Object[]{i,pl.getDescription()});
		            
		            
		        }
				int[] to = {R.id.searchText};
				SimpleCursorAdapter  suggestionAdapter  = new SimpleCursorAdapter(Globals.ApplicationContext, R.layout.list_item,suggestionCursor,new String[]{columnNames[1]},to, 0);
				CaptureActivity.shopSearchView.setSuggestionsAdapter(suggestionAdapter);
				if(CaptureActivity.decorView != null){
					CaptureActivity.isProgressBarAdded = false;
					CaptureActivity.decorView.removeView(CaptureActivity.progressBar);
				}

			}
			
		});
		
	   
	   
	    

	   
	}

	
}
