package com.unai.impapi.data.rel;

import java.util.List;

import com.unai.impapi.data.Person;
import com.unai.impapi.data.Title;

public class MovieByActor {
	
	private Person person;
	private Title title;
	private String as;
	private List<String> roleDetails;
	private List<String> titleDetails;
	private String character;
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public Title getTitle() {
		return title;
	}
	
	public void setTitle(Title title) {
		this.title = title;
	}
	
	public String getAs() {
		return as;
	}
	
	public void setAs(String as) {
		this.as = as;
	}
	
	public List<String> getRoleDetail() {
		return roleDetails;
	}
	
	public void addRoleDetail(String d) {
		roleDetails.add(d);
	}
	
	public List<String> getTitleDetails() {
		return titleDetails;
	}
	
	public void addTitleDetail(String d) {
		titleDetails.add(d);
	}
	
	public String getCharacter() {
		return character;
	}
	
	public void setCharacter(String c) {
		this.character = c;
	}
	
}
