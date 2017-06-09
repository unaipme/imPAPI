package com.unai.impapi.data.rel;

import static com.unai.impapi.Utils.trim;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unai.impapi.data.Movie;
import com.unai.impapi.data.Person;

public class WrittenBy extends ResourceSupport {
	
	private Person writer;
	private Movie movie;
	private String as;
	private String detail;
	
	@JsonProperty("name")
	public String getWriterName() {
		return writer.getName();
	}
	
	@JsonIgnore
	public Movie getMovie() {
		return movie;
	}
	
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	@JsonInclude(Include.NON_NULL)
	public String getAs() {
		return as;
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
	
	@JsonInclude(Include.NON_NULL)
	public String getDetail() {
		return detail;
	}
	
	public void setDetail(String detail) {
		this.detail = detail.substring(1, detail.length() - 1);
	}
	
	@JsonIgnore
	public Person getWriter() {
		return writer;
	}
	
	@JsonIgnore
	public String getWriterId() {
		return writer.getId();
	}
	
	public void setWriter(Person writer) {
		this.writer = writer;
	}
	
	public void parseText(String s) {
		s = s.replaceAll(",", "").trim();
		s = s.substring(1, s.length() - 1);
		String [] info = s.split("\\) \\(");
		for (String i : info) {
			if (i.startsWith("as ")) {
				setAs("(" + i + ")");
			} else
				setDetail("(" + i + ")");
		}
	}

}
