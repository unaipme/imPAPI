package com.unai.impapi.data.rel;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unai.impapi.data.Person;
import com.unai.impapi.data.Series;

public class StarringInSeries extends ResourceSupport {
	
	private Person star;
	private Series series;
	
	@JsonIgnore
	public Person getStar() {
		return star;
	}
	
	public void setStar(Person star) {
		this.star = star;
	}
	
	public String getStarName() {
		return star.getName();
	}
	
	@JsonIgnore
	public Series getSeries() {
		return series;
	}
	
	public void setSeries(Series series) {
		this.series = series;
	}
	
	@Override
	public String toString() {
		return star.getName();
	}
	
}
