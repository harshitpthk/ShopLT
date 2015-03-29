package com.homelybuysapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.example.sholite.R;
import com.homelybuysapp.activities.HomeActivity;

public class CameraFragment extends Fragment {
	View rootView;
	   
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		rootView = inflater.inflate(R.layout.camera_layout, container, false);
        return rootView;
    }
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

	    
	}
	@Override
	public void onResume()
	{
		// CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
        
//		HomeActivity act = (HomeActivity)getActivity();
//		SurfaceView surfaceView = (SurfaceView)rootView.findViewById(R.id.preview_view);
//		
//		//act.startQRScanner(surfaceView);
//		
		super.onResume();
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
		HomeActivity act = (HomeActivity)getActivity();
		
		act.onPause();
	}
}
