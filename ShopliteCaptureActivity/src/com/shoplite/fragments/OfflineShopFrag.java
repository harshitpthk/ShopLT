package com.shoplite.fragments;


import com.google.zxing.client.android.CaptureActivity;

import eu.livotov.zxscan.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

public class OfflineShopFrag extends Fragment {
		View rootView;
	   
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
			rootView = inflater.inflate(R.layout.offline_shop, container, false);
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
