package com.shoplite.fragments;


import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shoplite.UI.AddItemCard;
import com.shoplite.UI.BaseCardView;
import com.shoplite.UI.BaseItemCard.OnClickActionButtonListener;
import com.shoplite.UI.Controls;
import com.shoplite.UI.SubCategoryAdapter;
import com.shoplite.UI.productAdapter;
import com.shoplite.Utils.Globals;
import com.shoplite.interfaces.CategoryAdapterCallback;
import com.shoplite.interfaces.ControlsInterface;
import com.shoplite.interfaces.ItemInterface;
import com.shoplite.interfaces.ProductAdapterCallback;
import com.shoplite.models.Category;
import com.shoplite.models.Input;
import com.shoplite.models.Product;

import eu.livotov.zxscan.R;

public class OfflineShopFrag extends Fragment implements ItemInterface, CategoryAdapterCallback,ProductAdapterCallback,ControlsInterface {
		View rootView;
		private RecyclerView mRecyclerView;
	    private RecyclerView.Adapter mAdapter;
	    private RecyclerView.LayoutManager mLayoutManager;
	    Resources resources = null ;
	    private Toolbar searchToolbar;
		private MenuItem searchMenuItem;
		private SearchView itemSearchView;
		private LinearLayout mItemSearchView;
		private static LinearLayout catLevelView;
		private ArrayList<Category> currentChildLists;
		private AlertDialog alertDialog;
		private AlertDialog addDialog;
		private AddItemCard addToItem;
		private ProgressBar prgBar;
	    
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
			rootView = inflater.inflate(R.layout.offline_shop, container, false);
			mItemSearchView = (LinearLayout) rootView.findViewById(R.id.item_search_container);
			mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
			catLevelView = (LinearLayout)rootView.findViewById(R.id.cat_level_container);
			searchToolbar = (Toolbar) rootView.findViewById(R.id.search_toolbar);
			searchToolbar.inflateMenu(R.menu.dashboard);
			searchToolbar.setTitle("Search Products");
			searchToolbar.setTitleTextColor(getResources().getColor(android.R.color.darker_gray));
			resources = getResources();
			searchMenuItem = searchToolbar.getMenu().findItem( R.id.action_search ); // get my MenuItem with placeholder submenu
			itemSearchView = (SearchView) searchMenuItem.getActionView();
			itemSearchView.setQueryHint("Search Products");
			//itemSearchView.onActionViewExpanded();
			mRecyclerView.setHasFixedSize(true);
			//searchMenuItem.expandActionView();
			
			 searchToolbar.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.e("click",(String) searchMenuItem.getTitle());
					Log.e("click","toolbar clicked");
					itemSearchView.onActionViewExpanded();
					
					searchMenuItem.expandActionView(); // Expand the search menu item in order to show by default the query
				}
			});
			float minheight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, resources.getDisplayMetrics());
			//CaptureActivity.toolbar.setMinimumHeight(Float.floatToIntBits(minheight));
	        
	       

	        // specify an adapter (see also next example)
	        
	        return rootView;
	    }
		
		@Override
		public void onActivityCreated (Bundle savedInstanceState) {
		    super.onActivityCreated(savedInstanceState);

		    
		}
		@Override
		public void onResume()
		{
			
			super.onResume();
			
		}
		
		@Override
		public void onPause(){
			super.onPause();
			
		}

		/**
		 * 
		 */
		public void loadChildCategories(ArrayList<Category> childLists,final String parentCat) {
			// TODO Auto-generated method stub
			Log.e("childList",String.valueOf(childLists.size()));
			
	        mLayoutManager = new GridLayoutManager(getActivity(),2);
	        mRecyclerView.setLayoutManager(mLayoutManager);
	        
			mRecyclerView.setVisibility(View.VISIBLE);
			mItemSearchView.setVisibility(View.GONE);
			mAdapter = new SubCategoryAdapter(childLists,this);
			currentChildLists = childLists;
			catLevelView.removeAllViews();
			TextView parentCatView = new TextView(getActivity());
			parentCatView.setText(parentCat+ "  >");
			parentCatView.setTextColor(getResources().getColor(R.color.white));
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.weight = 1.0f;
			params.gravity = Gravity.CENTER_VERTICAL;
			
			
			parentCatView.setLayoutParams(params);
			catLevelView.addView(parentCatView);
			
			parentCatView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			parentCatView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					loadChildCategories(currentChildLists,parentCat);
				}
			});
	    	mRecyclerView.setAdapter(mAdapter);
		}

		/**
		 * 
		 */
		public void showSearchItems() {
			// TODO Auto-generated method stub
			catLevelView.removeAllViews();
			mRecyclerView.setVisibility(View.GONE);
			mItemSearchView.setVisibility(View.VISIBLE);
		}

		/**
		 * @param name
		 */
		

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ItemInterface#getItemList(com.shoplite.models.Product)
		 */
		@Override
		public void getItemList(Product item) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ItemInterface#ItemGetSuccess(com.shoplite.models.Product)
		 */
		@Override
		public void ItemGetSuccess(Product item) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ItemInterface#ItemGetFailure()
		 */
		@Override
		public void ItemGetFailure() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ItemInterface#ItemListGetSuccess(java.util.ArrayList)
		 */
		@Override
		public void ItemListGetSuccess(ArrayList<Product> itemFamily) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ItemInterface#getItem()
		 */
		@Override
		public void getItem() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ItemInterface#updateItemSuccess(com.shoplite.models.Product)
		 */
		@Override
		public void updateItemSuccess(Product product) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ItemInterface#updateItemFailure()
		 */
		@Override
		public void updateItemFailure() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ItemInterface#productsGetFailure()
		 */
		@Override
		public void productsGetFailure() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ItemInterface#productsGetSuccess()
		 */
		@Override
		public void productsGetSuccess(ArrayList<Product> productList) {
			// TODO Auto-generated method stub
			mLayoutManager = new LinearLayoutManager(getActivity());
	        mRecyclerView.setLayoutManager(mLayoutManager);
	        mAdapter = new productAdapter(productList,this);
	        mRecyclerView.setAdapter(mAdapter);
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ItemInterface#getProducts()
		 */
		@Override
		public void getProducts(Input input) {
			// TODO Auto-generated method stub
			Product product = new Product(0,null);
			product.getproducts(this, input);
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.CategoryAdapterCallback#onCategoryClicked(com.shoplite.models.Category)
		 */
		@Override
		public void onCategoryClicked(Category cat) {
			// TODO Auto-generated method stub
			TextView parentCatView = new TextView(Globals.ApplicationContext);
			parentCatView.setText(cat.getName());
			parentCatView.setTextColor(Globals.ApplicationContext.getResources().getColor(R.color.white));
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.weight = 1.0f;
			params.gravity = Gravity.CENTER_VERTICAL;

		
			parentCatView.setLayoutParams(params);
			catLevelView.addView(parentCatView);
			parentCatView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			Input input = new Input(cat.getId(),"category");
			getProducts(input);
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ProductAdapterCallback#onProductClicked(com.shoplite.models.Product)
		 */
		@Override
		public void onProductClicked( Product product) {
			// TODO Auto-generated method stub

			((ItemInterface) getActivity()).ItemGetSuccess(product);
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ControlsInterface#positive_button_alert_method()
		 */
		@Override
		public void positive_button_alert_method() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ControlsInterface#negative_button_alert_method()
		 */
		@Override
		public void negative_button_alert_method() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ControlsInterface#save_alert_dialog(android.app.AlertDialog)
		 */
		@Override
		public void save_alert_dialog(AlertDialog alertDialog) {
			// TODO Auto-generated method stub
			this.addDialog = alertDialog;
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ControlsInterface#neutral_button_alert_method()
		 */
		@Override
		public void neutral_button_alert_method() {
			// TODO Auto-generated method stub
			
		}
}
