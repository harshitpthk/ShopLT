package com.shoplite.connection;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

import com.google.gson.JsonObject;
import com.shoplite.models.Input;
import com.shoplite.models.ItemCategory;
import com.shoplite.models.Shop;
import com.shoplite.models.User;

public interface ServiceProvider {

	@POST("/service/user/registeruser")
	void signup(@Body User obj , Callback<Integer> cb  );
	
	@POST("/service/user/adduser")
	void addUser(@Body Integer auth_token, Callback<ArrayList<String>> cb);
	
	@POST("/service/user/getitem")
	void getItem(@Body Input itemInput, Callback<ItemCategory> callback);
	
	@POST("/service/user/getitems")
	void getItems(@Body Input brandInput, Callback<ArrayList<ItemCategory>> callback);
	
	@POST("/service/user/login")
	void login(@Header("shoplite-client-id") String authCode,@Body String email,Callback<String> cb);
	
	@POST("/service/user/getshop")
	void getshop(@Body com.shoplite.models.Location loc, Callback<Shop> cb);

	@POST("/service/user/login")
	void loginShop(@Body String sessionID, Callback<JsonObject> callback);
	
	@POST("/service/user/getshoplist")
	void getshoplist(@Body  com.shoplite.models.Location loc, Callback<ArrayList<Shop>> callback);

	
	@POST("/service/user/packitems")
	void packList(@Body com.shoplite.models.PackList packlist, Callback<JsonObject> callback);
	
	@POST("/service/user/submitorder")
	void submitOrder(@Body com.shoplite.models.SubmitOrderDetails submitOrderDetails, Callback<Integer> orderID);
	
	@POST("/service/user/createorder")
	void createOrderStar(@Body com.shoplite.models.SubmitOrderStar submitOrderStarDetails, Callback<String> response);
	
	
	@GET("/")
	void getPlacesSuggestion(Callback<JsonObject> callback);
	
}
