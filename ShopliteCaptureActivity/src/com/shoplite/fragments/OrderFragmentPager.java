package com.shoplite.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class OrderFragmentPager extends FragmentPagerAdapter {

	public OrderFragmentPager(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int i) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return 2;
	}

}
