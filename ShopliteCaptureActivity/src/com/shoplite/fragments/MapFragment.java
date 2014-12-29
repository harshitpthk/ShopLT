/**
 * 
 */
package com.shoplite.fragments;

import com.google.zxing.client.android.CaptureActivity;

import eu.livotov.zxscan.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author I300291
 *
 */
public class MapFragment extends Fragment {
	View rootView;
	private TextView shopDetailHeading;
	private LinearLayout shopDetailsView;
	private TextView shopDetailDescription;
    private Button startShop;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		rootView = inflater.inflate(R.layout.map_fragment_layout, container, false);
		shopDetailsView = (LinearLayout)rootView.findViewById(R.id.shop_details_view);
    	shopDetailHeading = (TextView)rootView.findViewById(R.id.shop_details_heading);
    	shopDetailDescription = (TextView)rootView.findViewById(R.id.shop_details_description);
    	startShop = (Button)rootView.findViewById(R.id.startShop);
        return rootView;
    }
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

	    
	}
	@Override
	public void onResume()
	{
		
		super.onResume();
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
	
	}
}
