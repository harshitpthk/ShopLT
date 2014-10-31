package com.shoplite.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.shoplite.UI.SaveListAdapter;
import com.shoplite.Utils.Globals;
import com.shoplite.models.SaveList;

import eu.livotov.zxscan.R;

public class SavedListsFragment extends Fragment{

	private ListView savedListsView;
	private LinearLayout emptySavedListView;
	private static ArrayList<SaveList> savedLists ;
	private static SaveListAdapter saveListAdapter;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.saved_lists, container, false);
		savedListsView = (ListView) rootView.findViewById(R.id.savedList_ListView);
		emptySavedListView = (LinearLayout) rootView.findViewById(R.id.empty_saved_list_container);
		savedLists = Globals.dbhelper.getAllSavedShopList();
		saveListAdapter = new SaveListAdapter(getActivity(), savedLists);
		return rootView;
       }
	
	public static ArrayList<SaveList> getSavedLists() {
		return savedLists;
	}

	public static void setSavedLists(ArrayList<SaveList> savedLists) {
		SavedListsFragment.savedLists = savedLists;
	}

	public static SaveListAdapter getSaveListAdapter() {
		return saveListAdapter;
	}

	public static void setSaveListAdapter(SaveListAdapter saveListAdapter) {
		SavedListsFragment.saveListAdapter = saveListAdapter;
	}

	@Override
	public void onActivityCreated (Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

	    
	}
	@Override
	public void onResume()
	{		
		super.onResume();
		savedListsView.setAdapter(saveListAdapter);
		
	}
}
