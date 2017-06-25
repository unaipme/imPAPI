package com.unai.impapi.data.rel;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unai.impapi.data.Person;
import com.unai.impapi.data.Series;

public class CreatedBy extends ResourceSupport {
	
	private Series series;
	private Person person;
	
	@JsonIgnore
	public Series getSeries() {
		return series;
	}
	
	public void setSeries(Series series) {
		this.series = series;
	}
	
	@JsonIgnore
	public Person getPerson() {
		return person;
	}
	
	public String getCreatorName() {
		return person.getName();
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	@JsonIgnore
	public String getPersonId() {
		return person.getId();
	}
	
	@Override
	public String toString() {
		return person.getName();
	}
	
}
