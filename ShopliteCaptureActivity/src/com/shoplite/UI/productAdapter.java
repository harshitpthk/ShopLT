/**
 * 
 */
package com.shoplite.UI;

import java.util.ArrayList;
import java.util.Locale;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoplite.Utils.Globals;
import com.shoplite.interfaces.ProductAdapterCallback;
import com.shoplite.models.Product;
import com.squareup.picasso.Picasso;

import eu.livotov.zxscan.R;

/**
 * @author I300291
 *
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Filterable {
	private ArrayList<Product> allProductList = new ArrayList<Product>();
	private ArrayList<Product> visibleProductList;
	private ProductAdapterCallback callback;
	
	
	
	public static  interface ProductClick{
    	public void productClick(Product product);
    }
	
	public ProductAdapter(ArrayList<Product> productList,ProductAdapterCallback callback ){
		
		this.visibleProductList = productList;
		allProductList.addAll(allProductList);
		this.callback = callback;
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {

		/**
		 * @param itemView
		 * @param subCategoryClick 
		 */
		public TextView mNameView;
        public ImageView mImageView;
        ProductClick productOnClick = null;
        
		public ViewHolder(View itemView) {
			super(itemView);
			
			// TODO Auto-generated constructor stub
			mNameView =  (TextView) itemView.findViewById(R.id.productTitle);
            mImageView = (ImageView)  itemView.findViewById(R.id.productImage);
            
            
		}
		
		/**
		 * @param product
		 * @param subCategoryClick 
		 */
		public void onViewHolderClick(final Product product, final ProductClick productClick) {
			// TODO Auto-generated method stub
			itemView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					productClick.productClick(product);
				}
			});
		}
	}

	
	@Override
	public int getItemCount() {
		return visibleProductList.size();
	}

	
	@Override
	public void onBindViewHolder(ViewHolder holder, int index) {
		Product product = visibleProductList.get(index);
    	
		holder.mNameView.setText(product.getName());
    	
		Picasso.with(Globals.ApplicationContext) 
    	 .load("http://s3-ap-southeast-1.amazonaws.com/static.shoplite/product_image/"+product.getId()+".jpg")
    	 .placeholder(R.drawable.placeholder)
    	.into(holder.mImageView);
    	
    	ProductClick prodClick = new ProductClick() {
			@Override
			public void productClick(Product product) {
				Log.e("category clicked", product.getName());
				callback.onProductClicked(product);
			}
		};
    	    	
    	holder.onViewHolderClick(product,prodClick);
		
	}

	/* (non-Javadoc)
	 * @see android.support.v7.widget.RecyclerView.Adapter#onCreateViewHolder(android.view.ViewGroup, int)
	 */
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int index) {
		
		 View cardLayout = LayoutInflater.from(parent.getContext())
                 .inflate(R.layout.product_row, parent, false);
		 
	        
			ViewHolder vh = new ViewHolder(cardLayout);
			 
			return vh;
		
	}


	/* (non-Javadoc)
	 * @see android.widget.Filterable#getFilter()
	 */
	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}
	

	/* (non-Javadoc)
	 * @see android.widget.Filterable#getFilter()
	 */
	


	/* (non-Javadoc)
	 * @see android.support.v7.widget.RecyclerView.Adapter#onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder, int)
	 */
	
}
