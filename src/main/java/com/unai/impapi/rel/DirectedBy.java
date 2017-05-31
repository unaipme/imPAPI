package com.unai.impapi.rel;

import static com.unai.impapi.Utils.trim;

import com.unai.impapi.data.Movie;
import com.unai.impapi.data.Person;

public class DirectedBy {
	
	private Person director;
	private String as;
	private Movie movie;
	
	public Person getDirector() {
		return director;
	}
	
	public void setDirector(Person director) {
		this.director = director;
	}
	
	public String getAs() {
		return this.as;
	}
	
	public void setAs(String as) {
		if (as.startsWith("(as ")) {
			if (as.contains(","))
				as = as.substring(0, as.length() - 1);
			as = as.substring(4, as.length() - 1);
			this.as = trim(as);
		} else
			this.as = null;
	}
	
	public Movie getMovie() {
		return movie;
	}
	
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
}
