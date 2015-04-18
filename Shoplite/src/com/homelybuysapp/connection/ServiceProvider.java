package com.homelybuysapp.connection;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

import com.google.gson.JsonObject;
import com.homelybuysapp.models.Category;
import com.homelybuysapp.models.Input;
import com.homelybuysapp.models.OrderDetail;
import com.homelybuysapp.models.OrderItemDetail;
import com.homelybuysapp.models.PackProducts;
import com.homelybuysapp.models.Product;
import com.homelybuysapp.models.Shop;
import com.homelybuysapp.models.User;

public interface ServiceProvider {

	@POST("/registeruser")
	void signup(@Body User obj , Callback<Integer> cb  );
	
	@POST("/adduser")
	void addUser(@Body Integer auth_token, Callback<ArrayList<String>> cb);
	
	@POST("/getproduct")
	void getItem(@Body Input itemInput, Callback<Product> callback);
	
	@POST("/getproduct")
	void getItems(@Body Input brandInput, Callback<ArrayList<Product>> callback);
	
	@POST("/login")
	void login(@Header("shoplite-client-id") String authCode,@Body String email,Callback<String> cb);
	
	@POST("/getshop")
	void getshop(@Body com.homelybuysapp.models.Location loc, Callback<Shop> cb);

	@POST("/connect")
	void loginShop(@Body Integer shopID, Callback<JsonObject> callback);
	
	@POST("/getshoplist")
	void getshoplist(@Body  com.homelybuysapp.models.Location loc, Callback<ArrayList<Shop>> callback);
	
	@POST("/getproducts")
	void getProducts(@Body Input input, Callback<ArrayList<Product>> callback);

	
	@POST("/packproducts")
	void packList(@Body PackProducts pckProd, Callback<JsonObject> callback);
	
	@POST("/submitorder")
	void submitOrder(@Body OrderDetail orderDetail, Callback<Integer> orderID);
	
	@POST("/getorderstate")
	void getOrderStatus(@Body int orderId, Callback<com.homelybuysapp.Utils.Constants.ORDERState> orderStatus);
	
	@POST("/getorderdetails")
	void getOrderDetails(@Body int orderId, Callback<ArrayList<OrderItemDetail>> orderedProductList);
	
	@GET("/")
	void getPlacesSuggestion(Callback<JsonObject> callback);
	
	@GET("/getcategories")
	void getCategories(Callback<ArrayList<Category>> callback);
	
	@POST("/getsearchresults")
	void searchProducts(@Body String query, Callback<ArrayList <Product>> callback);
	
}
