/**
 * 
 */
package com.shoplite.UI;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shoplite.fragments.SavedListsFragment;

import eu.livotov.zxscan.R;

/**
 * @author I300291
 *
 */
public class SaveListDialog extends DialogFragment{

	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	       	SavedListsFragment saveListFragment = new SavedListsFragment();
		 	View v = inflater.inflate(R.layout.generic_full_parent_container, container, false);
	         return v;
	    }
}
