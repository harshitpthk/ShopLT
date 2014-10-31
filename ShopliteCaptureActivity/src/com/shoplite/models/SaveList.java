/**
 * 
 */
package com.shoplite.models;

/**
 * @author I300291
 *
 */
public class SaveList {

	protected String saveListName;
	protected int totalItems;
	protected String savedDate;
	protected String listEntries;
	
	public String getSaveListName() {
		return saveListName;
	}
	public void setSaveListName(String saveListName) {
		this.saveListName = saveListName;
	}
	public int getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
	public String getSavedDate() {
		return savedDate;
	}
	public void setSavedDate(String savedDate) {
		this.savedDate = savedDate;
	}
	public String getListEntries() {
		return listEntries;
	}
	public void setListEntries(String listEntries) {
		this.listEntries = listEntries;
	}
}
