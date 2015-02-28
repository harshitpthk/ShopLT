package com.shoplite.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.shoplite.models.Address;
import com.shoplite.models.PreviousOrder;
import com.shoplite.models.Product;
import com.shoplite.models.SaveList;
import com.shoplite.models.Shop;

public class DbHelper extends  SQLiteOpenHelper{
	
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME="storage.db";
    private static final String TABLE_NAME = "dictionary";
    private static final String KEY="KEY";
    private static final String VALUE ="VALUE";
    private String cols[]={KEY,VALUE};
    
    private static final String SHOP_ID = "_ID";
    private static final String SHOP_LOCATION_TABLE = "connected_shop_location";
    private static final String SHOP_NAME = "SHOP_NAME";
    private static final String SHOP_URL = "SHOP_URL";
    private static final String SHOP_LAT = "SHOP_LAT";
    private static final String SHOP_LNG = "SHOP_LNG";
    
    //SHOPPING LIST COLUMNS
    private static final String SHOPPING_LIST_TABLE="SHOPPING_LIST_TABLE";
    private static final String LIST_ID = "_ID";
    private static final String LIST_NAME="LIST_NAME";
    private static final String LIST_ENTRIES="LIST_ENTRIES";
    private static final String TOTAL_ITEMS = "TOTAL_ITEMS";
    private static final String SAVED_DATE="SAVED_DATE";
    
    //PREVIOU ORDERS
    private static final String ORDERS_TABLE = "ORDERS_TABLE";
    private static final String ORDER_ID = "ORDER_ID";
    private static final String ORDER_TOTAL_ITEMS ="TOTAL_ITEMS";
    private static final String ORDER_DATE = "DATE";
    private static final String ORDER_AMOUNT = "ORDER_AMOUNT";
    private static final String LAST_KNOWN_STATUS = "LAST_KNOWN_STATUS";
    
    private static final String ORDERS_TABLE_CREATE=
    		"CREATE TABLE " + ORDERS_TABLE +" ( " +
    				ORDER_ID + " INTEGER PRIMARY KEY NOT NULL, " +
    				ORDER_DATE + " TEXT NOT NULL, " +
    				ORDER_TOTAL_ITEMS + " INTEGER NOT NULL, " +
    				ORDER_AMOUNT + " REAL NOT NULL, " +
    				LAST_KNOWN_STATUS + " INTEGER NOT NULL "+
    				");";
    
    
    private static final String DICTIONARY_TABLE_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                KEY + " TEXT PRIMARY KEY NOT NULL, " +
                VALUE + " TEXT);";
    
    private static final String SHOP_LOCATION_TABLE_CREATE =
            "CREATE TABLE " + SHOP_LOCATION_TABLE + " (" +
            		SHOP_NAME + " TEXT  NOT NULL, " +
            		SHOP_URL + " TEXT PRIMARY KEY NOT NULL, " +
            		SHOP_LAT + " REAL NOT NULL, " +
            		SHOP_LNG +" REAL NOT NULL" +
            		");";
    private static final String SHOPPING_LIST_TABLE_CREATE =
    		"CREATE TABLE " + SHOPPING_LIST_TABLE +" ( "+
    				LIST_NAME + " TEXT  UNIQUE NOT NULL , " + 
    				TOTAL_ITEMS + " INTEGER NOT NULL, " +
    				SAVED_DATE+ " TEXT NOT NULL," +
    				LIST_ENTRIES +" BLOB NOT NULL "+ 
    				");";
    
    
    private static final String USER_ADDRESSES_TABLE = "ADDRESSES_TABLE";
    private static final String ADDRESS_ID = "ADDRESS_ID";
    private static final String ADDRESS = "ADDRESS";
    private static final String USER_ADDRESSES_TABLE_CREATE= "CREATE TABLE " + USER_ADDRESSES_TABLE +" ( "+
    		   		ADDRESS +" BLOB NOT NULL "+
			");" ;
    
    
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
        db.execSQL(SHOP_LOCATION_TABLE_CREATE);
        db.execSQL(SHOPPING_LIST_TABLE_CREATE);
        db.execSQL(ORDERS_TABLE_CREATE);
        db.execSQL(USER_ADDRESSES_TABLE_CREATE);
    }


	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// upgrade
		
	}
	
	public  String getItem(String key)
	{
		
		String value=null;
		Cursor cursor=null;
		try {
			SQLiteDatabase database=  this.getReadableDatabase();
			String selctionArgs[] = {key};
			cursor = database.query(TABLE_NAME, cols, KEY+"=?", selctionArgs, null, null, null);
			cursor.moveToFirst();
			
			value = null;
			if(!cursor.isAfterLast())
			{
				value = cursor.getString(1);
			}
		} catch (Exception e) {
			return null;
			
		}finally 
        {
			if(cursor!=null)
      	  		cursor.close(); 
       }
		
		return value;
		
	}
	
	public boolean  setItem(String key, String value)
	{
		
		
		try {
			
			SQLiteDatabase database=  this.getWritableDatabase();
			
			ContentValues values = new ContentValues();
		    values.put(KEY, key);
		    values.put(VALUE, value);
		    
		    Long rowID = database.insertWithOnConflict(TABLE_NAME,null,values,SQLiteDatabase.CONFLICT_IGNORE);
		    
		    if(rowID==-1)
		    {
		    	String selctionArgs[] = {key};
		    	int id = database.update(TABLE_NAME, values, KEY+"=?", selctionArgs);
		    	
		    	if(id==1)
		    		return true;
		    	else
		    		return false;
		    }
		    
		    return true;
			
		} catch (Exception e) {
			return false;
			
		}
		
	}
	
	public boolean storeShopLocation(String ShopName,String ShopURL, double lat, double lng)
	{
		try {
			
			SQLiteDatabase database=  this.getWritableDatabase();
			
			ContentValues values = new ContentValues();
		    values.put(SHOP_NAME, ShopName);
		    values.put(SHOP_URL, ShopURL);
		    values.put(SHOP_LAT, lat);
		    values.put(SHOP_LNG, lng);
		    
		      
		    long id = database.insertWithOnConflict(SHOP_LOCATION_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		  
	    	
	    	if(id != -1)
	    		return true;
	    	else
	    		return false;
	    	
			
		} catch (Exception e) {
			return false;
			
		}
		
			
	}
	
	public ArrayList<Shop> getNearConnectedShop(Double lat , Double lng)
	{
		Cursor cursor=null;
		Shop shp;
		ArrayList<Shop> shoplist = new ArrayList<Shop>();
		try{
			Double lat_lower_bound = lat - 0.02;
			Double lat_upper_bound = lat + 0.02;
			Double lng_lower_bound = lng - 0.02;
			Double lng_upper_bound = lng + 0.02;
			
						
			SQLiteDatabase database = this.getReadableDatabase();
			String query = " SELECT * FROM " + SHOP_LOCATION_TABLE +" WHERE " +
						"(" + SHOP_LAT + " BETWEEN  "+ lat_lower_bound +	" AND " + lat_upper_bound +") " 
						+ " AND " +
						" ( "+SHOP_LNG + " BETWEEN "+ lng_lower_bound +" AND "+ lng_upper_bound +" ) "; 
			
			//cursor = database.query(SHOP_LOCATION_TABLE, columns, query, loc_radius, null, null, null);
			
			cursor = database.rawQuery(query,null );
			
			if(cursor.moveToFirst()){ 
				 while (cursor.isAfterLast() == false) {
					shp = new Shop();
					try{
						com.shoplite.models.Location loc = new com.shoplite.models.Location(cursor.getDouble(cursor.getColumnIndex(SHOP_LAT)) , cursor.getDouble(cursor.getColumnIndex(SHOP_LNG)) );
						
						shp.setName(cursor.getString(cursor.getColumnIndex(SHOP_NAME)));
						shp.setUrl(cursor.getString(cursor.getColumnIndex(SHOP_URL)));
						shp.setLocation(loc);
						shoplist.add(shp);
						
						
					}
					catch(Exception e){
						e.printStackTrace();
						return null;
					}
					cursor.moveToNext();
				 }
				
			}
			else{
				if(cursor!=null)
	      	  		cursor.close(); 
				return null;
				
			}
			
			
			return shoplist;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			cursor.close();
		}
		
		
	
	}
	
	public Shop getShopfromPosition(double lat, double lng)
	{
		
		return null;
		
	}


	public  boolean  storeShoppingList(String listName ,ArrayList<Product> shoppingList)
	{
		String listEntries = new Gson().toJson(shoppingList);
		int totalItems = shoppingList.size();
		Date dt = new Date();
		SimpleDateFormat dtformat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
		String date =  dtformat.format(dt);
		
		try {
			
			SQLiteDatabase database=  this.getWritableDatabase();
			ContentValues values = new ContentValues();
			
			values.put(LIST_NAME, listName);
		    values.put(TOTAL_ITEMS, totalItems );
		    values.put(SAVED_DATE,date);
		    values.put(LIST_ENTRIES,listEntries);
		    
		    
		      
		    long id = database.insertWithOnConflict(SHOPPING_LIST_TABLE, null, values, SQLiteDatabase.CONFLICT_FAIL);
		  
	    	
	    	if(id != -1)
	    		return true;
	    	else
	    		return false;
	    	
			
		} catch (Exception e) {
			return false;
			
		}
		
		
	}
	
	public SaveList getSavedShopList(String listName)
	{
		SaveList savedList = new SaveList();
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor=null;
		try{
			cursor = database.query(SHOPPING_LIST_TABLE,new String[]{LIST_NAME,TOTAL_ITEMS,SAVED_DATE,LIST_ENTRIES},LIST_NAME + "=?",new String[]{listName},null,null,null);
			cursor.moveToFirst();
			if(!cursor.isAfterLast()){
				savedList.setSaveListName(cursor.getString(0));
				savedList.setTotalItems(cursor.getInt(1));
				savedList.setSavedDate(cursor.getString(2));
				savedList.setListEntries(cursor.getString(3));
			}
		}
		catch(Exception e)
		{
			return null;
		}
		finally 
        {
			if(cursor!=null)
      	  		cursor.close(); 
        }
		return savedList;
		
	}
	public ArrayList<SaveList> getAllSavedShopList()
	{
		ArrayList<SaveList>  allSavedLists = new ArrayList<SaveList>();
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = null;
		try{
			cursor = database.query(SHOPPING_LIST_TABLE,new String[]{LIST_NAME,TOTAL_ITEMS,SAVED_DATE,LIST_ENTRIES},null,null,null,null,SAVED_DATE);
			cursor.moveToFirst();
		
			while(cursor.isAfterLast() == false){
				SaveList savedList = new SaveList();
				savedList.setSaveListName(cursor.getString(0));
				savedList.setTotalItems(cursor.getInt(1));
				savedList.setSavedDate(cursor.getString(2));
				savedList.setListEntries(cursor.getString(3));
				allSavedLists.add(savedList);
				cursor.moveToNext();
			}
		}
		catch(Exception e)
		{
			return null;
		}
		finally 
        {
			if(cursor!=null)
      	  		cursor.close(); 
        }
		
		return allSavedLists;
		
	}
	
	
	/**
	 * @param packing 
	 * @return
	 */
	public  boolean  storeOrder(int orderId ,double orderAmount, int totalItems, int state)
	{
		Date dt = new Date();
		SimpleDateFormat dtformat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
		String date =  dtformat.format(dt);
		
		try {
			
			SQLiteDatabase database=  this.getWritableDatabase();
			ContentValues values = new ContentValues();
			
			values.put(ORDER_ID, orderId);
			values.put(ORDER_DATE,date);
			values.put(ORDER_TOTAL_ITEMS,totalItems );
		    values.put(ORDER_AMOUNT,orderAmount);
		    values.put(LAST_KNOWN_STATUS, state);
		    
		    
		      
		    long id = database.insertWithOnConflict(ORDERS_TABLE, null, values, SQLiteDatabase.CONFLICT_FAIL);
		  
	    	
	    	if(id != -1)
	    		return true;
	    	else
	    		return false;
	    	
			
		} catch (Exception e) {
			return false;
			
		}
		
		
	}
	
	public ArrayList<PreviousOrder> getAllOrders() {
		ArrayList<PreviousOrder>  previousOrderLists = new ArrayList<PreviousOrder>();
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = null;
		try{
			cursor = database.query(ORDERS_TABLE,new String[]{ORDER_ID,ORDER_DATE,ORDER_TOTAL_ITEMS,ORDER_AMOUNT,LAST_KNOWN_STATUS},null,null,null,null,ORDER_DATE);
			cursor.moveToFirst();
		
			while(cursor.isAfterLast() == false){
				PreviousOrder previousOrder = new PreviousOrder();
				previousOrder.setOrderId(cursor.getInt(0));
				previousOrder.setOrderDate(cursor.getString(1));
				previousOrder.setOrderTotalItems(cursor.getInt(2));
				previousOrder.setOrderAmount(cursor.getDouble(3));
				previousOrder.setOrderState(cursor.getInt(4));
				previousOrderLists.add(previousOrder);
				cursor.moveToNext();
			}
		}
		catch(Exception e)
		{
			return null;
		}
		finally 
        {
			if(cursor!=null)
      	  		cursor.close(); 
        }
		
		return previousOrderLists;
	}
	
	public boolean updateOrderState(int orderID, int orderState)
	{
		
		try {
			SQLiteDatabase database=  this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(LAST_KNOWN_STATUS, orderState);
	    	long id = 0;
	    	String selctionArgs[] = {Integer.toString(orderID)};
	    	id = database.update(ORDERS_TABLE, values, ORDER_ID+"=?", selctionArgs);
	    	if(id != -1)
	    		return true;
	    	else
	    		return false;
	    	
				
		} catch (Exception e) {
			return false;
			
		}
	
		
		
	}

	public boolean storeAddress(Address address){
	
		Gson gson = new Gson();
		String addressString = gson.toJson(address);
		
		try {
			
			SQLiteDatabase database=  this.getWritableDatabase();
			ContentValues values = new ContentValues();
			
			values.put(ADDRESS, addressString);
				      
		    long id = database.insertWithOnConflict(USER_ADDRESSES_TABLE, null, values, SQLiteDatabase.CONFLICT_FAIL);
		  
	    	
	    	if(id != -1)
	    		return true;
	    	else
	    		return false;
	    	
			
		} catch (Exception e) {
			return false;
			
		}
		
		
	}
	
	public ArrayList<Address> getStoreAddress()
	{
		ArrayList<Address>  userAddresses = new ArrayList<Address>();
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = null;
		try{
			cursor = database.query(USER_ADDRESSES_TABLE,new String[]{ADDRESS},null,null,null,null,null);
			cursor.moveToFirst();
			Gson gson = new Gson();
			while(cursor.isAfterLast() == false){
				Address address = new Address();
				address = gson.fromJson(cursor.getString(0), Address.class);
				userAddresses.add(address);
				cursor.moveToNext();
			}
		}
		catch(Exception e)
		{
			return null;
		}
		finally 
        {
			if(cursor!=null)
      	  		cursor.close(); 
        }
		
		return userAddresses;
		
		
	}

	public Address getLastRecentDeliveredAddress()
	{
		Address lastRecentAddress = new Address();
		return null;
		
		
	}
}
