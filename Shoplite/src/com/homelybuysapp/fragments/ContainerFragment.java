package com.homelybuysapp.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.example.sholite.R;

public class ContainerFragment extends Fragment {
	public ViewPager mViewPager;
	public ContainerFragmentPager mContainerFragPager;
	public SurfaceView sfcview;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		
		View rootView = inflater.inflate(R.layout.container_fragment, container, false);
		mContainerFragPager = new ContainerFragmentPager(getFragmentManager());
		mViewPager = (ViewPager) rootView.findViewById(R.id.container_pager);
    	mViewPager.setAdapter(mContainerFragPager);
    	mViewPager.setOffscreenPageLimit(2);
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
}
