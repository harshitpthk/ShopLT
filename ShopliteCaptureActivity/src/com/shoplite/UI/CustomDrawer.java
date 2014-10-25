package com.shoplite.UI;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;

public class CustomDrawer extends DrawerLayout {

	private static final int DEFAULT_SIZE_WIDTH = 200;
	private static final int DEFAULT_SIZE_HEIGHT = 300;
    public CustomDrawer(Context context) {
        super(context);
    }

    public CustomDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       widthMeasureSpec = MeasureSpec.makeMeasureSpec(
               MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY);
       
       heightMeasureSpec = MeasureSpec.makeMeasureSpec(
    		   MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY);
       super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

	private int getSize(int widthMeasureSpec ,char dimension) {
		// TODO Auto-generated method stub
		int result = 0;
		if(dimension =='W'){
			 result = (int) (DEFAULT_SIZE_WIDTH *
				 getResources().getDisplayMetrics().density);
		}
		else if(dimension =='H'){
			 result = (int) (DEFAULT_SIZE_HEIGHT *
					 getResources().getDisplayMetrics().density);
		}
		return result;
	}

}