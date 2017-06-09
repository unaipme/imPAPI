package com.unai.impapi.data;

import com.unai.impapi.exception.WrongIdTypeException;

public abstract class Title implements PageData {
	public final static int MOVIE_TITLE = 1;
	public final static int SERIES_TITLE = 2;
	
	protected String id;
	protected String title;
	protected Double rating;
	protected String number;

	public Title(String id) {
		if (!id.startsWith("tt")) throw new WrongIdTypeException("Movie IDs start with \"tt\"");
		this.id = id;
	}
	
	protected Title() {}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getId() {
		return id;
	}
	
	public abstract int getType();
}
