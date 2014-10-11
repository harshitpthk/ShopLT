package com.shoplite.fragments;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ContainerFragmentPager extends FragmentPagerAdapter {

	public ContainerFragmentPager(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int i) {
		// TODO Auto-generated method stub
		Fragment fragment ;
		switch(i){
			case 0:
				 return fragment = new OfflineShopFrag();
	        case 1:
	    	     return fragment = new CameraFragment();
	        case 2:
	        	 return fragment = new OrderFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

}
