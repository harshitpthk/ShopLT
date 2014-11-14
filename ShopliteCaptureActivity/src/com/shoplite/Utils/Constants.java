package com.shoplite.Utils;

public class Constants {
	
	public enum DBState {INSERT,UPDATE,DELETE};
	
	public enum ORDERStatus {INITIAL,FORPAYMENT,FORHOMEDELIVERYPAYMENT,FORHOMEDELIVERY,FORDELIVERY,CANCELED};
	
	public enum ORDERState {PACKING,PACKED,FORDELIVERY,DELIVERED};
	public enum PAYMENTMode {CASH,ONLINE,SWIPE};
	
	public static int MAX_NOT_SENT_ITEMS = 2;
}
