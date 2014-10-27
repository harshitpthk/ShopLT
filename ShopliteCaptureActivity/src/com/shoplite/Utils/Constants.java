package com.shoplite.Utils;

public class Constants {
	
	public enum DBState {INSERT,UPDATE,DELETE};
	
	public enum ORDERState {INITIAL,FORPAYMENT,FORHOMEDELIVERY,FORDELIVERY,CLOSED};
	
	public enum PAYMENTMode {CASH,ONLINE,SWIPE};
	
	public static int MAX_NOT_SENT_ITEMS = 10;
}
