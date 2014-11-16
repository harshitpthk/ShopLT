package com.shoplite.connection;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import android.provider.SyncStateContract.Constants;

import com.google.gson.JsonObject;
import com.shoplite.models.Input;
import com.shoplite.models.OrderItemDetail;
import com.shoplite.models.Product;
import com.shoplite.models.Shop;
import com.shoplite.models.User;

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
	void getshop(@Body com.shoplite.models.Location loc, Callback<Shop> cb);

	@POST("/connect")
	void loginShop(@Body Integer shopID, Callback<JsonObject> callback);
	
	@POST("/getshoplist")
	void getshoplist(@Body  com.shoplite.models.Location loc, Callback<ArrayList<Shop>> callback);

	
	@POST("/packproducts")
	void packList(@Body com.shoplite.models.PackList packlist, Callback<JsonObject> callback);
	
	@POST("/submitorder")
	void submitOrder(@Body com.shoplite.models.SubmitOrderDetails submitOrderDetails, Callback<Integer> orderID);
	
	@POST("/getorderstate")
	void getOrderStatus(@Body int orderId, Callback<com.shoplite.Utils.Constants.ORDERState> orderStatus);
	
	@POST("/getorderdetails")
	void getOrderDetails(@Body int orderId, Callback<ArrayList<OrderItemDetail>> orderedProductList);
	
	@GET("/")
	void getPlacesSuggestion(Callback<JsonObject> callback);
	
}
