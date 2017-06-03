package com.unai.impapi.rel;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unai.impapi.data.Movie;
import com.unai.impapi.data.Person;

public class Role extends ResourceSupport {
	
	private Movie movie;
	private Person person;
	private String role;
	
	@JsonIgnore
	public Movie getMovie() {
		return movie;
	}
	
	@JsonProperty("title")
	public String getMovieTitle() {
		return movie.getTitle();
	}
	
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	@JsonIgnore
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
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof Role)) return false;
		Role r = (Role) o;
		return r.getMovie().equals(movie) && r.getPerson().equals(person) && r.getRole().equals(role);
	}
	
}
