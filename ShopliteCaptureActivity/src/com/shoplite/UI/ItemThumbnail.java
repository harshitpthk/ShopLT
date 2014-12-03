package com.shoplite.UI;

import android.content.Context;


public class ItemThumbnail  {

	private String itemThumbnailURL = null;

	 public ItemThumbnail(Context context,String Url) {
		
		this.itemThumbnailURL = Url;
		
	}



	       

	public String getItemThumbnailURL() {
		return itemThumbnailURL;
	}





	public void setItemThumbnailURL(String itemThumbnailURL) {
		this.itemThumbnailURL = itemThumbnailURL;
	}





//	@Override
//    public void setupInnerViewElements(ViewGroup parent, View viewImage) {
//
//        //Here you have to set your image with an external library
//		
//        Picasso.with(getContext())
//               .load(itemThumbnailURL)
//               .resize(96, 96)
//               .into((ImageView)viewImage);
//        
//       
//    }
}
