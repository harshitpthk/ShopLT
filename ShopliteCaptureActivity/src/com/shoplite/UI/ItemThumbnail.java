package com.shoplite.UI;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

public class ItemThumbnail extends CardThumbnail {

	private String itemThumbnailURL = null;

	 public ItemThumbnail(Context context,String Url) {
		super(context);
		this.itemThumbnailURL = Url;
		// TODO Auto-generated constructor stub
	}



	       

	public String getItemThumbnailURL() {
		return itemThumbnailURL;
	}





	public void setItemThumbnailURL(String itemThumbnailURL) {
		this.itemThumbnailURL = itemThumbnailURL;
	}





	@Override
    public void setupInnerViewElements(ViewGroup parent, View viewImage) {

        //Here you have to set your image with an external library
		
        Picasso.with(getContext())
               .load(itemThumbnailURL)
               .resize(96, 96)
               .into((ImageView)viewImage);
        
       
    }
}
