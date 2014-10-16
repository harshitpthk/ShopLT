package com.shoplite.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

public class AnimatedLinearLayout extends LinearLayout {

	public AnimatedLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ScaleAnimation anim = new ScaleAnimation(0,1,0,1);
        anim.setDuration(500);
        anim.setFillAfter(true);
        this.startAnimation(anim);
    }

}
