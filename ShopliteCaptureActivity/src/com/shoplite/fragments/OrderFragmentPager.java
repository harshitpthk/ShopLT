package com.shoplite.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class OrderFragmentPager extends FragmentPagerAdapter {

	public OrderFragmentPager(FragmentManager fm) {
		super(fm);
		
	}

	@Override
	public Fragment getItem(int i) {
		
		Fragment fragment ;
		
		switch(i){
			case 0:
				return fragment = new PreviousOrderFragment();
	       case 1:
	    	   return fragment = new CurrentOrderFragment();
	       case 2:
	    	   return fragment = new PreviousOrderFragment();
	
	        
		}
        return null;
		
	}

	@Override
	public int getCount() {
		
		return 2;
	}

}
