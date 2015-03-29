package com.homelybuysapp.models;


public class RegistrationTokenizer {
	private int token;
	private String validator;
	private boolean userExists;
	private User user;
	
	public int getToken() {
		return token;
	}
	public void setToken(int token) {
		this.token = token;
	}
	public String getValidator() {
		return validator;
	}
	public void setValidator(String msg) {
		this.validator = msg;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public boolean isUserExists() {
		return userExists;
	}
	public void setUserExists(boolean userExists) {
		this.userExists = userExists;
	}

}
