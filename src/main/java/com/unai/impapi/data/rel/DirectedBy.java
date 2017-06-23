package com.unai.impapi.data.rel;

import static com.unai.impapi.Utils.trim;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unai.impapi.data.Movie;
import com.unai.impapi.data.Person;

public class DirectedBy extends ResourceSupport {
	
	private Person director;
	private String as;
	private List<String> detail = new ArrayList<>();
	private Movie movie;
	
	@JsonProperty("name")
	public String getDirectorName() {
		return director.getName();
	}
	
	@JsonIgnore
	public Person getDirector() {
		return director;
	}
	
	public void setDirector(Person director) {
		this.director = director;
	}
	
	@JsonInclude(Include.NON_NULL)
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
	
	@JsonIgnore
	public Movie getMovie() {
		return movie;
	}
	
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	public List<String> getDetails() {
		return detail;
	}

	public void addDetail(String d) {
		detail.add(d);
	}

	@JsonIgnore
	public String getDirectorId() {
		return director.getId();
	}
	
}
