package com.homelybuysapp.interfaces;

import com.homelybuysapp.models.OrderItemDetail;
import com.homelybuysapp.models.PackList;

public interface PackListInterface {
	public void sendPackList();
	public void PackListSuccess(PackList obj);
	public void editPackList();
	public void deletePackList(OrderItemDetail itemToDelete);
	
}
