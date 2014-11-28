package com.shoplite.models;

public class Input {
	
		
		String type;
		int id;

		public Input(int id, String type) {
			// TODO Auto-generated constructor stub
				this.id = id;
				this.type = type;
				
			}
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
}
