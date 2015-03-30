/**
 * 
 */
package com.homelybuysapp.UI;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.homelybuys.homelybuysApp.R;
import com.homelybuysapp.models.SaveList;

/**
 * @author I300291
 *
 */
public class SaveListAdapter  extends BaseAdapter{

	private ArrayList<SaveList> savedLists;
	private Context context;
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	
	public SaveListAdapter(Context context,ArrayList<SaveList> savedLists)
	{
		this.savedLists = savedLists;
		this.context = context;
	}
	
	public  void updateSavedLists(ArrayList<SaveList> savedLists)
	{
		this.savedLists = savedLists;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return !savedLists.isEmpty() ? savedLists.size(): 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public SaveList getItem(int position) {
		// TODO Auto-generated method stub
		return savedLists.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup root) {
		// TODO Auto-generated method stub
		SaveList savedList = savedLists.get(position);
		SavedListCard saveListCard = new SavedListCard(savedList);
		if(convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.save_list_container, root,false);
			saveListCard.setParentView(context, (ViewGroup) convertView, savedLists, this);
			
		}
		return convertView;
	}

}
