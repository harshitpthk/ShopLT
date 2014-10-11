package com.shoplite.UI;

import com.shoplite.Utils.Globals;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import android.content.Context;

public class ItemCard {

	public static Card createItemCard(Context context, String title , String description,boolean buttonVisible)
	{
		Card card = new Card(context);
		
		CardHeader header = new CardHeader(context);
		header.setTitle(title);
		header.setOtherButtonVisible(buttonVisible);
		card.addCardHeader(header);
		
		ItemThumbnail thumbnail = new ItemThumbnail(context,"URL");
		thumbnail.setExternalUsage(true);
		card.addCardThumbnail(thumbnail);
		
		card.setTitle(description);
		
		return card;
	}
}
