/**
 * 
 */
package com.shoplite.UI;

import eu.livotov.zxscan.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author I300291
 *
 */
public class BaseCardView extends LinearLayout {

	/**
	 * @param context
	 */
	public BaseCardView(Context context) {
		super(context);
		View.inflate(context, R.layout.drawer_item_container, this);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public BaseCardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
			View.inflate(context, R.layout.drawer_item_container, this);
	}
	

}
