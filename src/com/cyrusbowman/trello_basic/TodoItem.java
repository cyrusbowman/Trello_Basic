package com.cyrusbowman.trello_basic;
public class TodoItem {
	private String id;
	private String title;
	private String description;
	private String dateModified;
	private int done;
	private int synced;

	public TodoItem() {
		
	}
	
	public TodoItem(String id, String theTitle, String theDescription, String theDate, int theDone, int theSynced) {
		this.id = id;
		this.title = theTitle;
		this.description = theDescription;
		this.dateModified = theDate;
		this.done = theDone;
		this.synced = theSynced;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String theId) {
		this.id = theId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String theTitle){
		title = theTitle;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String theDescription){
		description = theDescription;
	}


	public String getDate() {
		return dateModified;
	}
	
	public void setDate(String theDate){
		dateModified = theDate;
	}
	
	public int getDone() {
		return done;
	}
	
	public void setDone(int theDone){
		done = theDone;
	}
	
	public int getSynced() {
		return synced;
	}
	
	public void setSynced(int theSynced){
		synced = theSynced;
	}
}
