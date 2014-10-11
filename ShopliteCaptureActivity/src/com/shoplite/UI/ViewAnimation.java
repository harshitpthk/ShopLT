package com.shoplite.UI;

import android.view.View;
import android.view.animation.TranslateAnimation;


public class ViewAnimation {

	// To animate view slide out from left to right
	public static void slideToRight(View view){
	TranslateAnimation animate = new TranslateAnimation(0,view.getWidth(),0,0);
	animate.setDuration(500);
	animate.setFillAfter(true);
	view.startAnimation(animate);
	view.setVisibility(View.GONE);
	}
	// To animate view slide out from right to left
	public static void slideToLeft(View view){
	TranslateAnimation animate = new TranslateAnimation(0,-view.getWidth(),0,0);
	animate.setDuration(500);
	animate.setFillAfter(true);
	view.startAnimation(animate);
	view.setVisibility(View.GONE);
	}

	// To animate view slide out from top to bottom
	public  static void slideToBottom(View view){
	TranslateAnimation animate = new TranslateAnimation(0,0,0,view.getHeight());
	animate.setDuration(500);
	animate.setFillAfter(true);
	view.startAnimation(animate);
	view.setVisibility(View.GONE);
	}

	// To animate view slide out from bottom to top
	public static void slideToTop(View view){
	TranslateAnimation animate = new TranslateAnimation(0,0,0,-view.getHeight());
	animate.setDuration(500);
	animate.setFillAfter(true);
	view.startAnimation(animate);
	view.setVisibility(View.GONE);
	}
}
