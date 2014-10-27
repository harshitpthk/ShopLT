/**
 * 
 */
package com.shoplite.Utils;

import eu.livotov.zxscan.BuildConfig;
import android.os.Looper;

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
