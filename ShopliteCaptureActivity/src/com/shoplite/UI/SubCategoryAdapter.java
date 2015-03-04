/**
 * 
 */
package com.shoplite.UI;

import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoplite.Utils.Globals;
import com.shoplite.fragments.OfflineShopFrag;
import com.shoplite.interfaces.CategoryAdapterCallback;
import com.shoplite.models.Category;
import com.squareup.picasso.Picasso;

import eu.livotov.zxscan.R;

/**
 * @author I300291
 *
 */
public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>  {
	private ArrayList<Category> subCategories;
	private CategoryAdapterCallback callback;
	
	
	
	public static  interface SubCategoryClick{
    	public void onSubCategoryClick(Category subCat);
    }
	
	public SubCategoryAdapter(ArrayList<Category> subCategories,CategoryAdapterCallback callback ){
		
		this.subCategories = subCategories;
		this.callback = callback;
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {

		/**
		 * @param itemView
		 * @param subCategoryClick 
		 */
		public TextView mNameView;
        public ImageView mImageView;
        SubCategoryClick subCatOnclick = null;
        
		public ViewHolder(View itemView) {
			super(itemView);
			
			// TODO Auto-generated constructor stub
			mNameView =  (TextView) itemView.findViewById(R.id.catTitle);
            mImageView = (ImageView)  itemView.findViewById(R.id.catImage);
            
            
		}
		
		/**
		 * @param subCat
		 * @param subCategoryClick 
		 */
		public void onViewHolderClick(final Category subCat, final SubCategoryClick subCategoryClick) {
			// TODO Auto-generated method stub
			itemView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subCategoryClick.onSubCategoryClick(subCat);
				}
			});
		}
	}

	
	@Override
	public int getItemCount() {
		return subCategories.size();
	}

	
	@Override
	public void onBindViewHolder(ViewHolder holder, int index) {
		Category subCat = subCategories.get(index);
    	holder.mNameView.setText(subCat.getName());
    	 Picasso.with(Globals.ApplicationContext) 
    	 .load("http://www.muscleandfitness.com/sites/muscleandfitness.com/files/images/bread.jpg")
    	 .placeholder(R.drawable.placeholder)
    	.into(holder.mImageView);
    	
    	SubCategoryClick subCategoryClick = new SubCategoryClick() {
			@Override
			public void onSubCategoryClick(Category subCat) {
				Log.e("category clicked", subCat.getName());
				callback.onCategoryClicked(subCat);
			}
		};
    	    	
    	holder.onViewHolderClick(subCat,subCategoryClick);
		
	}

	/* (non-Javadoc)
	 * @see android.support.v7.widget.RecyclerView.Adapter#onCreateViewHolder(android.view.ViewGroup, int)
	 */
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int index) {
		
		 View cardLayout = LayoutInflater.from(parent.getContext())
                 .inflate(R.layout.subcategorycard, parent, false);
		 
	        
			ViewHolder vh = new ViewHolder(cardLayout);
			 
			return vh;
		
	}
}
