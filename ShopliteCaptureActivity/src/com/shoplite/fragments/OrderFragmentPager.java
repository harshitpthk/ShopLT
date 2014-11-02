package com.shoplite.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class OrderFragmentPager extends FragmentPagerAdapter {

	private static final String[] titles = { " Saved Lists","Previous Orders" };
	
	public OrderFragmentPager(FragmentManager fm) {
		super(fm);
		
	}

	@Override
	public Fragment getItem(int i) {
		
			
		switch(i){
			case 0:
				 return  new SavedListsFragment();
		   	case 1:
	    	   
	    	    return new OrderHistoryFragment();
	    	   
	        
		}
        return null;
		
	}
	@Override
	public CharSequence getPageTitle(int position) {
	    return titles[position];
	}

	@Override
	public int getCount() {
		
		return 2;
	}

}
