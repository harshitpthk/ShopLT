package com.shoplite.interfaces;

import com.shoplite.models.OrderItemDetail;
import com.shoplite.models.PackList;

public interface PackListInterface {
	public void sendPackList();
	public void PackListSuccess(PackList obj);
	public void editPackList();
	public void deletePackList(OrderItemDetail itemToDelete);
	
}
