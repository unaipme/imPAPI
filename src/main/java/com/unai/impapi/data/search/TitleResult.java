package com.unai.impapi.data.search;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unai.impapi.data.Movie;
import com.unai.impapi.rest.MovieController;

public class TitleResult extends ResourceSupport implements SearchResult {
	
	private Movie movie;
	private String detail;
	private String type;

	@JsonIgnore
	public Movie getMovie() {
		return movie;
	}
	
	@JsonProperty("id")
	public String getMovieId() {
		return movie.getId();
	}
	
	@JsonProperty("title")
	public String getMovieTitle() {
		return movie.getTitle();
	}
	
	@JsonProperty("year")
	public Integer getMovieYear() {
		return movie.getReleaseYear();
	}
	
	@JsonIgnore
	public String getFormattedYear() {
		if (movie.getReleaseYear() == null) return null;
		else return "(" + movie.getReleaseYear() + ")";
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	@JsonInclude(Include.NON_NULL)
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@JsonInclude(Include.NON_NULL)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int getResultType() {
		return TITLE_RESULT;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(String.format("%s ", movie.getTitle()));
		if (movie.hasNumber()) s.append(String.format("(%s) ", movie.getNumber()));
		s.append(String.format("(ID: %s) (%d)", movie.getId(), movie.getReleaseYear()));
		if (type != null) s.append(String.format(" %s%n", type));
		else s.append("\n");
		if (detail != null) s.append(String.format("\t%s%n", detail));
		return s.toString();
	}

	@Override
	public Class<?> controllerClass() {
		return MovieController.class;
	}

}
