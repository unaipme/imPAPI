package com.unai.impapi.data;

import static com.unai.impapi.Utils.trim;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.unai.impapi.exception.WrongIdTypeException;

public abstract class Title implements PageData {
	
	protected String id;
	protected String title;
	protected Double rating;
	protected String number;

	public Title(String id) {
		if (!id.startsWith("tt")) throw new WrongIdTypeException("Movie IDs start with \"tt\"");
		this.id = id;
	}
	
	protected Title() {}
	
	@JsonInclude(Include.NON_NULL)
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
	
	public void setRating(String s) {
		this.rating = Double.valueOf(trim(s).replaceAll(",", "."));
	}
	
	@JsonInclude(Include.NON_NULL)
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public boolean hasNumber() {
		return number != null;
	}

	public String getId() {
		return id;
	}
	
	@JsonIgnore
	public abstract boolean isSeries();
}
