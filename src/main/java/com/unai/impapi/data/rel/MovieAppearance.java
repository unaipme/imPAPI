package com.unai.impapi.data.rel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.unai.impapi.data.Movie;
import com.unai.impapi.data.Person;
import com.unai.impapi.data.TitleCharacter;

public class MovieAppearance {
	
	private Movie movie;
	private Person person;
	private TitleCharacter character;
	private String detail;
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
	
	@JsonInclude(Include.NON_NULL)
	public String getDetail() {
		return detail;
	}
	
	public void setDetail(String detail) {
		this.detail = detail;
	}

	@JsonInclude(Include.NON_NULL)
	public String getAs() {
		return as;
	}

	public void setAs(String as) {
		this.as = as;
	}
	
}
