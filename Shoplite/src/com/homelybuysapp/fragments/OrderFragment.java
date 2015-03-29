package com.homelybuysapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sholite.R;

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
      	View rootView = inflater.inflate(R.layout.order_layout, container, false);
		mOrderPagerAdapter = new OrderFragmentPager(getChildFragmentManager());
		mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
		final PagerTabStrip strip = PagerTabStrip.class.cast(rootView.findViewById(R.id.pager_title_strip));
		strip.setDrawFullUnderline(true);
		strip.setNonPrimaryAlpha(0.5f);
		strip.setTextSpacing(25);
		strip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
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
