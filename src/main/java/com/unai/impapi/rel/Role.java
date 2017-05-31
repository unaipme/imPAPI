package com.unai.impapi.rel;

import com.unai.impapi.data.Movie;
import com.unai.impapi.data.Person;

public class Role {
	
	private Movie movie;
	private Person person;
	private String role;
	
	public Movie getMovie() {
		return movie;
	}
	
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
}
