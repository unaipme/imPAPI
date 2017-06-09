package com.unai.impapi.data.rel;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unai.impapi.data.Person;
import com.unai.impapi.data.Title;

public class Role extends ResourceSupport {
	
	private Title title;
	private Person person;
	private String roleName;
	
	@JsonIgnore
	public Title getTitle() {
		return title;
	}
	
	@JsonProperty("title")
	public String getTitleTitle() {
		return title.getTitle();
	}
	
	public void setTitle(Title title) {
		this.title = title;
	}
	
	@JsonIgnore
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public String getRoleName() {
		return roleName;
	}
	
	public void setRoleName(String role) {
		this.roleName = role;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof Role)) return false;
		Role r = (Role) o;
		return r.getTitle().equals(title) && r.getPerson().equals(person) && r.getRoleName().equals(roleName);
	}
	
	@Override
	public int hashCode() {
		int result = 6;
		result = 37 * result + getTitle().hashCode();
		result = 37 * result + getPerson().hashCode();
		result = 37 * result + getRoleName().hashCode();
		return result;
	}
	
}
