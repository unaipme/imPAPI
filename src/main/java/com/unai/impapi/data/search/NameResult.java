package com.unai.impapi.data.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unai.impapi.data.Person;

public class NameResult implements SearchResult {
	
	private Person person;
	private String detail;
	private String role;
	private String knownForMovie;
	private Integer knownForMovieYear;

	public String getPersonId() {
		return person.getId();
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@JsonIgnore
	public String getPersonName() {
		return person.getName();
	}

	@JsonProperty("name")
	public String getNameWithNumber() {
		return String.format("%s (%s)", person.getName(), person.getNumber());
	}
	
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getKnownForMovie() {
		return knownForMovie;
	}

	public void setKnownForMovie(String knownForMovie) {
		this.knownForMovie = knownForMovie;
	}
	
	public Integer getKnownForMovieYear() {
		return knownForMovieYear;
	}

	public void setKnownForMovieYear(Integer knownForMovieYear) {
		this.knownForMovieYear = knownForMovieYear;
	}

	@JsonIgnore
	public String getNumber() {
		return person.getNumber();
	}
	
	@JsonIgnore
	public String getFormattedNumber() {
		if (person.hasNumber()) return "(" + person.getNumber() + ")";
		else return null;
	}
	
	@JsonIgnore
	public Person getPerson() {
		return person;
	}

	@Override @JsonIgnore
	public int getResultType() {
		return NAME_RESULT;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(person.getName().concat(" "));
		if (person.hasNumber()) {
			s.append(String.format("(%s) ", person.getNumber()));
		}
		if (detail != null)
			s.append(detail.concat(" "));
		s.append(String.format("(ID: %s)%n", person.getId()));
		if (role != null) {
			s.append(String.format("\t%s, known for %s", role, knownForMovie));
			if (knownForMovieYear != null) 
				s.append(String.format(" (%d)%n", knownForMovieYear));
			else s.append("\n");
		}
		return s.toString();
	}
	
}
