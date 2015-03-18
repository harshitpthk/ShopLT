package com.shoplite.models;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.util.Log;

import com.shoplite.Utils.Globals;
import com.shoplite.connection.ConnectionInterface;
import com.shoplite.connection.ServerConnectionMaker;
import com.shoplite.connection.ServiceProvider;
import com.shoplite.interfaces.ItemInterface;

public class Product implements ConnectionInterface{

	private int id;
	private int categoryId;
	private String name;
	private int brandId;
	private ArrayList<ProductVariance> varianceList;
	private int currentItemId;
	private int currentQty;
	private String currentMeasure;
	private Double currentMsrPrice;
	private Double totalPrice;
	private int quantity;
	private String imageUrl;
	private boolean isSent;
	private boolean isSelected;
	
	
	private  ItemInterface calling_class_object;
	private Input ItemInput;
	private Input brandInput;
	
	
	private boolean update_item_bool;
	private boolean get_item_bool;
	private boolean get_items_from_brand_bool;
	private Input catInput;
	private boolean get_items_from_cat;
	private boolean search_items_from_text;
	private String search_text;
	
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public int getCurrentItemId() {
		return currentItemId;
	}
	public void setCurrentItemId(int currentItemId) {
		this.currentItemId = currentItemId;
	}
	public int getCurrentQty() {
		return currentQty;
	}
	public void setCurrentQty(int currentQty) {
		this.currentQty = currentQty;
	}
	public String getCurrentMeasure() {
		return currentMeasure;
	}
	public void setCurrentMeasure(String currentMeasure) {
		this.currentMeasure = currentMeasure;
	}
	public Double getCurrentMsrPrice() {
		return currentMsrPrice;
	}
	public void setCurrentMsrPrice(Double currentMsrPrice) {
		this.currentMsrPrice = currentMsrPrice;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public boolean isSent() {
		return isSent;
	}
	public void setSent(boolean isSent) {
		this.isSent = isSent;
	}
	
		
	public int getBrandId() {
		return brandId;
	}
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	public Product(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public ArrayList<ProductVariance> getItemList() {
		return varianceList;
	}
	public void setItemList(ArrayList<ProductVariance> itemList) {
		this.varianceList = itemList;
	}
	
	
	
	
	public static int countNotSent(ArrayList<Product> item_order_list){
		int count = 0;
		for(int i = 0 ; i < item_order_list.size();i++){
			if(!item_order_list.get(i).isSent()){
				count++;
			}
		}
		return count;
	}
	
	public static void setSentList(ArrayList<Product> item_order_list) {
		int i;
		for(i = 0 ; i < item_order_list.size() ;i++){
			if(!item_order_list.get(i).isSent()){
				item_order_list.get(i).setSent(true);
			}
		}
	}
	
	public static ArrayList<OrderItemDetail> getToSendList(ArrayList<Product> item_order_list)
	{
		int i;
		ArrayList<OrderItemDetail> toSendList = new ArrayList<OrderItemDetail>();
		for(i = 0 ; i < item_order_list.size() ; i++){
			if(!item_order_list.get(i).isSent()){
				OrderItemDetail itemToOrder = new OrderItemDetail(item_order_list.get(i).getCurrentItemId(),item_order_list.get(i).getCurrentQty());
				toSendList.add(itemToOrder);
			}
		}
		return toSendList;
	}
	
	public  void getItem(Input input, ItemInterface calling_class_object) {
		
		this.calling_class_object = calling_class_object;
		this.get_item_bool = true;
		this.ItemInput = input;
		ServerConnectionMaker.sendRequest(this);
		
	}
	
	public  void updateItem(Input input, ItemInterface calling_class_object) {
		
		this.calling_class_object = calling_class_object;
		this.update_item_bool = true;
		this.ItemInput = input;
		ServerConnectionMaker.sendRequest(this);
		
	}
	
	public  void getItems( ItemInterface calling_class_object,int brandId) {
		
		this.calling_class_object = calling_class_object;
		this.get_items_from_brand_bool = true;
		this.brandInput = new Input(brandId,"brand");
		
		ServerConnectionMaker.sendRequest(this);
		
	}
	public  void getproducts( ItemInterface calling_class_object,Input input) {
		
		this.calling_class_object = calling_class_object;
		this.get_items_from_cat = true;
		this.catInput = input;
		
		ServerConnectionMaker.sendRequest(this);
		
	}
	public void searchProducts(ItemInterface calling_class_object,String query ){
		this.calling_class_object = calling_class_object;
		this.search_items_from_text = true;
		this.search_text = query;
		
		ServerConnectionMaker.sendRequest(this);
	}
	
	/* (non-Javadoc)
	 * @see com.shoplite.connection.ConnectionInterface#sendRequest(com.shoplite.connection.ServiceProvider)
	 */
	
	@Override
	public void sendRequest(ServiceProvider serviceProvider) {
		// TODO Auto-generated method stub
		final Product thisProductObject = this;
		if(this.get_items_from_brand_bool == true ){
			serviceProvider.getItems( brandInput,new Callback<ArrayList<Product>>(){

				@Override
				public void failure(RetrofitError response) {
					if (response.isNetworkError()) {
						Log.e("Service Unavailable", "503"); // Use another code if you'd prefer
				    }
					else{
						Log.e("Get Items Failure",response.getMessage());
					}
					
					ServerConnectionMaker.recieveResponse(null);
					
				}

				@Override
				public void success(ArrayList<Product> itemFamily, Response response) {
					
					ServerConnectionMaker.recieveResponse(response);
					Globals.simmilar_item_list = itemFamily;
					thisProductObject.calling_class_object.ItemListGetSuccess(itemFamily);
				}
				
			});
		}
		else if(search_items_from_text == true){
			serviceProvider.searchProducts(thisProductObject.search_text, new Callback <ArrayList<Product>>(){

				@Override
				public void failure(RetrofitError response) {
					// TODO Auto-generated method stub
					if (response.isNetworkError()) {
						Log.e("Service Unavailable", "503"); 	
				    }
					else{
						Log.e("Search Failure",response.getMessage());
					}
					
					ServerConnectionMaker.recieveResponse(null);
					thisProductObject.calling_class_object.searchProductFailure();
				}

				@Override
				public void success(ArrayList<Product> productList, Response response) {
					// TODO Auto-generated method stub
					ServerConnectionMaker.recieveResponse(response);
					thisProductObject.calling_class_object.productSearchSuccess(productList);
				}});
		}
		else if(this.get_items_from_cat == true){
			serviceProvider.getProducts(catInput, new Callback<ArrayList<Product>>() {

				@Override
				public void failure(RetrofitError response) {
					// TODO Auto-generated method stub
					if (response.isNetworkError()) {
						Log.e("Service Unavailable", "503"); 	
				    }
					else{
						Log.e("Get ProductVariance Failure",response.getMessage());
					}
					
					ServerConnectionMaker.recieveResponse(null);
					thisProductObject.calling_class_object.productsGetFailure();
				}

				@Override
				public void success(ArrayList<Product> productList, Response response) {
					// TODO Auto-generated method stub

					ServerConnectionMaker.recieveResponse(response);
					thisProductObject.calling_class_object.productsGetSuccess(productList);
				}
			});
		}
		else if(this.get_item_bool == true){
			serviceProvider.getItem( ItemInput,new Callback<Product>(){

				@Override
				public void failure(RetrofitError response) {
					if (response.isNetworkError()) {
						Log.e("Service Unavailable", "503"); 	
				    }
					else{
						Log.e("Get ProductVariance Failure",response.getMessage());
					}
					
					ServerConnectionMaker.recieveResponse(null);
					thisProductObject.calling_class_object.ItemGetFailure();
				}

				@Override
				public void success(Product item, Response response) {
					
					ServerConnectionMaker.recieveResponse(response);
					Globals.fetched_item_category = item;
								//Currently calling all the products at the same time of the item fetch, have to move it to demand based fetching
					thisProductObject.calling_class_object.ItemGetSuccess(item);
					
				}
				
			});
		}
		else if(this.update_item_bool == true){
			
			serviceProvider.getItem( ItemInput,new Callback<Product>(){

				@Override
				public void failure(RetrofitError response) {
					if (response.isNetworkError()) {
						Log.e("Service Unavailable", "503"); 	
				    }
					else{
						Log.e("Get ProductVariance Failure",response.getMessage());
					}
					
					ServerConnectionMaker.recieveResponse(null);
					thisProductObject.calling_class_object.updateItemFailure();
				}

				@Override
				public void success(Product updatedProduct, Response response) {
					
					ServerConnectionMaker.recieveResponse(response);
					updatedProduct.setCurrentItemId(thisProductObject.getCurrentItemId());
					updatedProduct.setCurrentMeasure(thisProductObject.getCurrentMeasure());
					for(int i = 0 ; i < updatedProduct.getItemList().size();i++)
					{
						if(updatedProduct.getItemList().get(i).getId() == updatedProduct.getCurrentItemId()){
							if(updatedProduct.getItemList().get(i).getQuantity()<
									thisProductObject.getCurrentQty())
							{
								updatedProduct.setCurrentQty(updatedProduct.getItemList().get(i).getQuantity());
								
							}
							else{
								updatedProduct.setCurrentQty(thisProductObject.getCurrentQty());
							}
							updatedProduct.setCurrentMsrPrice(updatedProduct.getItemList().get(i).getPrice());
							break;
						}
						
					}
					updatedProduct.setTotalPrice(updatedProduct.getCurrentMsrPrice()*
							updatedProduct.getCurrentQty());
					Log.e("updation",updatedProduct.toString());
					
					
					thisProductObject.calling_class_object.updateItemSuccess(updatedProduct);
					
				}
				
			});
		}
	}
	
	
}
