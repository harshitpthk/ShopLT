/**
 * 
 */
package com.homelybuysapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import eu.livotov.zxscan.R;

/**
 * @author Harshit Pathak
 *
 */
public class BaseMapfragment extends Fragment{
	View rootView;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		rootView = inflater.inflate(R.layout.map_fragment_layout, container, false);
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
