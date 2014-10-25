package com.shoplite.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.client.android.CaptureActivity;

import eu.livotov.zxscan.R;

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
		CaptureActivity act = (CaptureActivity)getActivity();
		SurfaceView surfaceView = (SurfaceView)rootView.findViewById(R.id.preview_view);
		
		act.startQRScanner(surfaceView);
		
		super.onResume();
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
		CaptureActivity act = (CaptureActivity)getActivity();
		
		act.onPause();
	}
}
