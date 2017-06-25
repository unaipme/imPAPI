package com.unai.impapi.data.rel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.unai.impapi.data.Movie;
import com.unai.impapi.data.Person;
import com.unai.impapi.data.TitleCharacter;

public class MovieAppearance extends ResourceSupport {
	
	private Movie movie;
	private Person person;
	private TitleCharacter character;
	private List<String> details = new ArrayList<>();
	private String as;
	
	@JsonIgnore
	public Movie getMovie() {
		return movie;
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
	
	public String getPersonName() {
		return person.getName();
	}
	
	@JsonIgnore
	public TitleCharacter getCharacter() {
		return character;
	}
	
	public void setCharacter(TitleCharacter character) {
		this.character = character;
	}
	
	@JsonInclude(Include.NON_NULL)
	public String getCharacterName() {
		return character.getName();
	}
	
	@JsonInclude(Include.NON_EMPTY)
	public List<String> getDetails() {
		return details;
	}
	
	public void addDetail(String d) {
		details.add(d);
	}

	@JsonInclude(Include.NON_NULL)
	public String getAs() {
		return as;
	}

	public void setAs(String as) {
		this.as = as;
	}
	
	@JsonIgnore
	public String getPersonId() {
		return person.getId();
	}
	
}
