package com.unai.impapi.data.rel;

import com.unai.impapi.data.Person;
import com.unai.impapi.data.Series;

public class Starring {
	
	private Person star;
	private Series series;
	
	public Person getStar() {
		return star;
	}
	
	public void setStar(Person star) {
		this.star = star;
	}
	
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
