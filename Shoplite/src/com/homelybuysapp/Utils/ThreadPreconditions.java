/**
 * 
 */
package com.homelybuysapp.Utils;

import android.os.Looper;

import com.example.sholite.BuildConfig;

/**
 * @author I300291
 *
 */
public class ThreadPreconditions {

	    public static void checkOnMainThread() {
	        if (BuildConfig.DEBUG) {
	            if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
	                throw new IllegalStateException("This method should be called from the Main Thread");
	            }
	        }
	    }
	
}
