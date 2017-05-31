package com.unai.impapi.rel;

import static com.unai.impapi.Utils.trim;

import com.unai.impapi.data.Movie;
import com.unai.impapi.data.Person;

public class WrittenBy {
	
	private static final int TXT_THRESHOLD = 5;
	
	private Person writer;
	private Movie movie;
	private String as;
	private String detail;
	
	public Person getWriter() {
		return writer;
	}
	
	public void setWriter(Person writer) {
		this.writer = writer;
	}
	
	public Movie getMovie() {
		return movie;
	}
	
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
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
	
	public String getDetail() {
		return detail;
	}
	
	public void setDetail(String detail) {
		this.detail = detail.substring(1, detail.length() - 1);
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
