/**
 * 
 */
package com.shoplite.UI;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shoplite.activities.ItemsDisplayActivity;
import com.shoplite.models.SaveList;

import eu.livotov.zxscan.R;

/**
 * @author I300291
 *
 */
public class SavedListCard {
	
	protected int innerView;
	protected TextView listNameView;
	protected TextView dateView;
	protected TextView totalItemsView;
	protected SaveList savedlist;
	protected ArrayList<SaveList> savedLists;
	protected SaveListAdapter savedListAdapter;
	protected Context context;
	
	public SavedListCard( SaveList savedList){
		this.savedlist = savedList;
	}
	
	public void setParentView(Context context, ViewGroup container, ArrayList<SaveList> savedLists
			, SaveListAdapter savedListAdapter)
	{
		this.context = context;
		this.savedLists = savedLists;
		this.savedListAdapter = savedListAdapter;
		setUpView(context,container);
	}
	
	/**
	 * @param context2
	 * @param container
	 */
	private void setUpView(final Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		LayoutInflater lp = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = lp.inflate(R.layout.save_list_card, root);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "onclick saved list", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(context,ItemsDisplayActivity.class);
				i.putExtra("ListName", savedlist.getSaveListName());
				i.putExtra("ItemList", savedlist.getListEntries());
				context.startActivity(i);
			}

			
		});
		setListNameView((TextView)view.findViewById(R.id.saved_list_name));
		setDateView((TextView)view.findViewById(R.id.saved_list_date));
		setTotalItemsView((TextView)view.findViewById(R.id.total_items));
		updateView();
	}
	
	private void updateView()
	{
		getListNameView().setText(savedlist.getSaveListName());
		getDateView().setText(savedlist.getSavedDate());
		getTotalItemsView().setText(Integer.toString(savedlist.getTotalItems()));
	}

	public int getInnerView() {
		return innerView;
	}
	public void setInnerView(int innerView) {
		this.innerView = innerView;
	}
	public TextView getListNameView() {
		return listNameView;
	}
	public void setListNameView(TextView listNameView) {
		this.listNameView = listNameView;
	}
	public TextView getDateView() {
		return dateView;
	}
	public void setDateView(TextView dateView) {
		this.dateView = dateView;
	}
	public TextView getTotalItemsView() {
		return totalItemsView;
	}
	public void setTotalItemsView(TextView numberItemsView) {
		this.totalItemsView = numberItemsView;
	}
}
