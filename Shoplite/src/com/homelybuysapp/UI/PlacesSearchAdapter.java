/**
 * 
 */
package com.homelybuysapp.UI;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.homelybuys.homelybuysApp.R;

/**
 * @author I300291
 *
 */
public class PlacesSearchAdapter extends CursorAdapter implements Filterable {

    private ArrayList<String> items;
    private ArrayList<String> orig;

    public PlacesSearchAdapter(Context context, Cursor cursor,
            ArrayList<String> items) {
        super(context, cursor, false);
        this.items = new ArrayList<String>();
        this.items = items;
        orig = new ArrayList<String>();
        orig.addAll(items);
    }

    @Override
    public int getCount() {
    	if(items != null)
    		return items.size();
    	else
    		return 0;
    }
    @Override
    public Object getItem(int position) {
       if (position < items.size()) {            
           return items.get(position);
       } else {
           return null;
       }
   }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();

                if (constraint != null
                        && constraint.toString().length() > 0) {
                    List<String> founded = new ArrayList<String>();
                    for (int i = 0, l = orig.size(); i < l; i++) {
                        if (orig.get(i)
                                .toString()
                                .toLowerCase()
                                .startsWith(
                                        constraint.toString().toLowerCase()))
                            founded.add(orig.get(i));
                    }

                    result.values = founded;
                    result.count = founded.size();
                } else {
                    synchronized (this) {
                        result.values = orig;
                        result.count = orig.size();
                    }

                }
                return result;

            }

            @Override
            protected void publishResults(CharSequence constraint,
                    FilterResults results) {

            	if(items != null){
	                items.clear();
	                items = new ArrayList<String>();
	                items = (ArrayList<String>) results.values;
	                notifyDataSetChanged();
            	}

            }

        };
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.textView.setText(items.get(cursor.getPosition()));
    }

    public class ViewHolder {
        public TextView textView;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View v = null;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        v = inflater.inflate(R.layout.list_item, parent, false);
        holder.textView = (TextView) v.findViewById(R.id.searchText);

        v.setTag(holder);
        return v;

    }

	/**
	 * @param suggestionCursor
	 * @param placesNameList
	 */
	public void updateAdapter(MatrixCursor suggestionCursor,
			ArrayList<String> placesNameList) {
		// TODO Auto-generated method stub
		
	}
}
