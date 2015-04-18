package com.homelybuysapp.fragments;


import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.homelybuys.homelybuysApp.R;
import com.homelybuysapp.UI.AddItemCard;
import com.homelybuysapp.UI.Controls;
import com.homelybuysapp.UI.DividerItemDecoration;
import com.homelybuysapp.UI.ProductAdapter;
import com.homelybuysapp.UI.SubCategoryAdapter;
import com.homelybuysapp.UI.UIUtil;
import com.homelybuysapp.interfaces.CategoryAdapterCallback;
import com.homelybuysapp.interfaces.ControlsInterface;
import com.homelybuysapp.interfaces.ItemInterface;
import com.homelybuysapp.interfaces.ProductAdapterCallback;
import com.homelybuysapp.models.Category;
import com.homelybuysapp.models.Input;
import com.homelybuysapp.models.Product;

public class OfflineShopFrag extends Fragment implements ItemInterface, CategoryAdapterCallback,ProductAdapterCallback,ControlsInterface {
		View rootView;
		
		
		private RecyclerView mSearchRecyclerView;
		private RecyclerView.Adapter mSearchAdapter;
		private RecyclerView.LayoutManager mSearchLayoutManager;
		
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
		private ArrayList<Product> searchedProductList = new ArrayList<Product>();
		private ArrayList<Product> currentProductlist = new ArrayList<Product>();
		private DividerItemDecoration itemDecor;
		private TextView subCatView;


		private TextView parentCatView;
		
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
			rootView = inflater.inflate(R.layout.offline_shop, container, false);
			mItemSearchView = (LinearLayout) rootView.findViewById(R.id.item_search_container);
			mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
			mRecyclerView.setHasFixedSize(true);
			
			
			mSearchRecyclerView = (RecyclerView) rootView.findViewById(R.id.search_recycler_view);
			mSearchRecyclerView.setHasFixedSize(true);
			mSearchLayoutManager = new LinearLayoutManager(getActivity());
			mSearchRecyclerView.setLayoutManager(mSearchLayoutManager);
		   
			mSearchRecyclerView.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					//Toast.makeText(getActivity(), "touched", Toast.LENGTH_SHORT).show();
					mItemSearchView.clearFocus();
					UIUtil.hideSoftKeyboard(getActivity());

					return false;
				}
			});
			
			itemDecor =  new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
			catLevelView = (LinearLayout)rootView.findViewById(R.id.cat_level_container);
			searchToolbar = (Toolbar) rootView.findViewById(R.id.search_toolbar);
			searchToolbar.inflateMenu(R.menu.dashboard);
			searchToolbar.setTitle("Search Products");
			searchToolbar.setTitleTextColor(getResources().getColor(android.R.color.darker_gray));
			resources = getResources();
			searchMenuItem = searchToolbar.getMenu().findItem( R.id.action_search ); // get my MenuItem with placeholder submenu
			itemSearchView = (SearchView) searchMenuItem.getActionView();
			itemSearchView.setQueryHint("Search Products");
			mSearchAdapter = new ProductAdapter(searchedProductList,this);
			
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
			 itemSearchView.setOnQueryTextListener(new OnQueryTextListener() {
				
				@Override
				public boolean onQueryTextSubmit(String query) {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public boolean onQueryTextChange(String query) {
					// TODO Auto-generated method stub
					if(query.length() ==3){
						searchProducts(query);
						// Turn it on
						
										    // And when you want to turn it off
					   
					   
					}
					else if(query.length() > 3){
						filter(query);
					}
					else{
						mSearchRecyclerView.setAdapter(null);
					}
					
					return false;
				}
			});
			 mSearchRecyclerView.setAdapter(mSearchAdapter);
			
			
			  subCatView = new TextView(getActivity());

				 parentCatView = new TextView(getActivity());
				
	        
	        return rootView;
	    }
		
		/**
		 * @param query
		 */
		protected void searchProducts(String query) {
			Product pd = new Product(0,null);
			pd.searchProducts(this, query);
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
			parentCatView.setText(parentCat.toUpperCase());
			parentCatView.setTextColor(getResources().getColor(R.color.app_yellow));

			parentCatView.setPaintFlags(parentCatView.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.weight = 1.0f;
			params.gravity = Gravity.CENTER_VERTICAL;
			
			
			parentCatView.setLayoutParams(params);
			catLevelView.addView(parentCatView);
			parentCatView.setEllipsize(TruncateAt.END);
			parentCatView.setHorizontallyScrolling(false);
			parentCatView.setSingleLine();
			parentCatView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			
			parentCatView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					loadChildCategories(currentChildLists,parentCat);
					parentCatView.setTextColor(getResources().getColor(R.color.app_yellow));
					parentCatView.setPaintFlags(parentCatView.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
			        mRecyclerView.removeItemDecoration(itemDecor);

					
					//parentCatView.setBackgroundColor(Globals.ApplicationContext.getResources().getColor(R.color.dark_app_color));
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
		 * @see com.homelybuysapp.interfaces.ItemInterface#getItemList(com.homelybuysapp.models.Product)
		 */
		@Override
		public void getItemList(Product item) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.ItemInterface#ItemGetSuccess(com.homelybuysapp.models.Product)
		 */
		@Override
		public void ItemGetSuccess(Product item) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.ItemInterface#ItemGetFailure()
		 */
		@Override
		public void ItemGetFailure() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.ItemInterface#ItemListGetSuccess(java.util.ArrayList)
		 */
		@Override
		public void ItemListGetSuccess(ArrayList<Product> itemFamily) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.ItemInterface#getItem()
		 */
		@Override
		public void getItem() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.ItemInterface#updateItemSuccess(com.homelybuysapp.models.Product)
		 */
		@Override
		public void updateItemSuccess(Product product) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.ItemInterface#updateItemFailure()
		 */
		@Override
		public void updateItemFailure() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.ItemInterface#productsGetFailure()
		 */
		@Override
		public void productsGetFailure() {
			// TODO Auto-generated method stub
			if(catLevelView.findViewById(9999) != null)
				catLevelView.removeView(catLevelView.findViewById(9999));
			android.widget.Toast.makeText(getActivity(),getString(R.string.products_fetch_failure),android.widget.Toast.LENGTH_LONG).show();
			parentCatView.setTextColor(getResources().getColor(R.color.app_yellow));

		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.ItemInterface#productsGetSuccess()
		 */
		@Override
		public void productsGetSuccess(ArrayList<Product> productList) {
			// TODO Auto-generated method stub
			Controls.show_loading_dialog(getActivity(), "Building Product List");

			mLayoutManager = new LinearLayoutManager(getActivity());
	        mRecyclerView.setLayoutManager(mLayoutManager);
	        mRecyclerView.addItemDecoration(itemDecor);

	        mAdapter = new ProductAdapter(productList,this);
	        
	        mRecyclerView.setAdapter(mAdapter);
			Controls.dismiss_progress_dialog();

		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.ItemInterface#getProducts()
		 */
		@Override
		public void getProducts(Input input) {
			// TODO Auto-generated method stub
			Product product = new Product(0,null);
			product.getproducts(this, input);
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.CategoryAdapterCallback#onCategoryClicked(com.homelybuysapp.models.Category)
		 */
		@Override
		public void onCategoryClicked(Category cat) {
			// TODO Auto-generated method stub
			//subCatView.setPaintFlags(subCatView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			parentCatView.setPaintFlags(parentCatView.getPaintFlags() & ~Paint.FAKE_BOLD_TEXT_FLAG);
			parentCatView.setTextColor(getResources().getColor(R.color.white));
			
			subCatView.setPaintFlags(parentCatView.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
			subCatView.setId(9999);
			subCatView.setText(cat.getName().toUpperCase());
			subCatView.setTextColor(getResources().getColor(R.color.app_yellow));
			
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.weight = 1.0f;
			params.leftMargin=20;
			params.gravity = Gravity.CENTER_VERTICAL;

			//parentCatView.setBackgroundColor(Globals.ApplicationContext.getResources().getColor(R.color.status_bar_app_color));
			subCatView.setLayoutParams(params);
			if(catLevelView.findViewById(9999) == null){
			catLevelView.addView(subCatView);
			subCatView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			subCatView.setEllipsize(TruncateAt.END);
			subCatView.setHorizontallyScrolling(false);
			subCatView.setSingleLine();
			Input input = new Input(cat.getId(),"category");
			getProducts(input);
			Controls.show_loading_dialog(getActivity(), "Fetching Products...");

			}
			
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.ProductAdapterCallback#onProductClicked(com.homelybuysapp.models.Product)
		 */
		@Override
		public void onProductClicked( Product product) {
			// TODO Auto-generated method stub

			((ItemInterface) getActivity()).ItemGetSuccess(product);
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.ControlsInterface#positive_button_alert_method()
		 */
		@Override
		public void positive_button_alert_method() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.ControlsInterface#negative_button_alert_method()
		 */
		@Override
		public void negative_button_alert_method() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.ControlsInterface#save_alert_dialog(android.app.AlertDialog)
		 */
		@Override
		public void save_alert_dialog(AlertDialog alertDialog) {
			// TODO Auto-generated method stub
			this.addDialog = alertDialog;
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.ControlsInterface#neutral_button_alert_method()
		 */
		@Override
		public void neutral_button_alert_method() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.ItemInterface#searchProductFailure()
		 */
		@Override
		public void searchProductFailure() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.ItemInterface#productSearchSuccess(java.util.ArrayList)
		 */
		@Override
		public void productSearchSuccess(ArrayList<Product> productList) {
		    ((android.support.v7.app.ActionBarActivity) getActivity()).setSupportProgressBarIndeterminateVisibility(true);
			// TODO Auto-generated method stub
			searchedProductList.addAll(productList);
			currentProductlist = productList;
	        mSearchAdapter = new ProductAdapter(currentProductlist,this);
	        mSearchRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

	        mSearchRecyclerView.setAdapter(mSearchAdapter);	
	        filter(itemSearchView.getQuery().toString());
		}
		
		public void filter(String charText) {
			charText = charText.toLowerCase(Locale.getDefault());
			charText = charText.replaceAll("[!?,]", "");
			String[] words = charText.split("\\s+");
			int count;
			currentProductlist.clear();
			if (charText.length() == 0) {
				currentProductlist.addAll(searchedProductList);
			} 
			else 
			{
				for (Product product : searchedProductList) 
				{
					count= 0;
					for(String word : words){
						if (product.getName().toLowerCase().contains(word)) 
						{
							count++;
						}
					}
					if(count == words.length){
						currentProductlist.add(product);
					}
				}
			}
			mSearchAdapter = new ProductAdapter(currentProductlist,this);
	        mSearchRecyclerView.setAdapter(mSearchAdapter);
		}
}
