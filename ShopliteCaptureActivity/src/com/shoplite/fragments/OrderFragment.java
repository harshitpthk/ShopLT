package com.shoplite.fragments;

import eu.livotov.zxscan.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OrderFragment extends Fragment {

	public OrderFragmentPager mOrderPagerAdapter;
	
	public ViewPager mViewPager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		
		View rootView = inflater.inflate(R.layout.order_layout, container, false);
		mOrderPagerAdapter = new OrderFragmentPager(getChildFragmentManager());
		mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
    	mViewPager.setAdapter(mOrderPagerAdapter);
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
