package com.shoplite.fragments;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ContainerFragmentPager extends FragmentPagerAdapter {

	public ContainerFragmentPager(FragmentManager fm) {
		super(fm);
		
	}

	@Override
	public Fragment getItem(int i) {
		switch(i){
			case 0:
				 return  new OfflineShopFrag();
	        case 1:
	    	     return   new CameraFragment();
	        case 2:
	        	 return   new OrderFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		
		return 3;
	}

}
