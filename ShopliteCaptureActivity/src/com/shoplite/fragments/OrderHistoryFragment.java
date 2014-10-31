package com.shoplite.fragments;

import eu.livotov.zxscan.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OrderHistoryFragment extends Fragment{

	   
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
	        return inflater.inflate(R.layout.previous_order, container, false);
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
}
