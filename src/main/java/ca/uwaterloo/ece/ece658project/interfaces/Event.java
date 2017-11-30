package ca.uwaterloo.ece.ece658project.interfaces;

import java.util.Date;

public class Event {

	String title;
	Date date;
	String description;

	public Event(String title, Date date, String description) {
		this.title = title;
		this.date = date;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public Date getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

}
