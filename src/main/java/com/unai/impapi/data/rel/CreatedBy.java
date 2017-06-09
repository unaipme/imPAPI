package com.unai.impapi.data.rel;

import com.unai.impapi.data.Person;
import com.unai.impapi.data.Series;

public class CreatedBy {
	
	private Series series;
	private Person person;
	
	public Series getSeries() {
		return series;
	}
	
	public void setSeries(Series series) {
		this.series = series;
	}
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	@Override
	public String toString() {
		return person.getName();
	}
	
}
