package com.shoplite.fragments;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class ContainerFragmentPager extends FragmentPagerAdapter {
	private Fragment mCurrentFragment;

	public ContainerFragmentPager(FragmentManager fm) {
		super(fm);
		
	}
	
    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }
//...    
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
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
