package com.unai.impapi.data;

import static com.unai.impapi.Utils.trim;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unai.impapi.exception.WrongIdTypeException;
import com.unai.impapi.rel.DirectedBy;
import com.unai.impapi.rel.WrittenBy;

public class Movie implements PageData {
	
	private String id;
	private String title;
	private Integer releaseYear;
	private List<DirectedBy> directors = new ArrayList<>();
	private List<WrittenBy> writers = new ArrayList<>();
	private Double rating;
	private String number;
	
	public Movie(String id) {
		if (!id.startsWith("tt")) throw new WrongIdTypeException("Movie IDs start with \"tt\"");
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}
	
	public void setRating(String s) {
		this.rating = Double.valueOf(trim(s));
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = trim(title);
	}
	
	public Integer getReleaseYear() {
		return releaseYear;
	}
	
	public void setReleaseYear(Integer releaseYear) {
		this.releaseYear = releaseYear;
	}
	
	public void setReleaseYear(String y) {
		if (y.contains("("))
			this.releaseYear = Integer.parseInt(y.substring(1, y.length() - 1));
		else 
			this.releaseYear = Integer.parseInt(y);
	}
	
	@JsonProperty("directedBy")
	public List<DirectedBy> getDirectors() {
		return directors;
	}
	
	@JsonIgnore
	public List<Person> getDirectorList() {
		return directors.stream().map(DirectedBy::getDirector).collect(Collectors.toList());
	}
	
	@JsonProperty("writtenBy")
	public List<WrittenBy> getWriters() {
		return writers;
	}
	
	@JsonIgnore
	public List<Person> getWriterList() {
		return writers.stream().map(WrittenBy::getWriter).collect(Collectors.toList());
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Movie withDirector(DirectedBy p) {
		directors.add(p);
		return this;
	}
	
	public void addDirector(DirectedBy p) {
		directors.add(p);
	}
	
	public Movie withWriter(WrittenBy p) {
		writers.add(p);
		return this;
	}
	
	public void addWriter(WrittenBy p) {
		writers.add(p);
	}
	
	public boolean hasNumber() {
		return number != null;
	}
	
	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder();
		s.append(String.format("%s (%d) *%.1f%n", title, releaseYear, rating));
		s.append("Directed by:\n");
		directors.forEach(d -> {
			if (d.getAs() != null) s.append(String.format("\t%s (as %s)%n", d.getDirector().getName(), d.getAs()));
			else s.append(String.format("\t%s\n", d.getDirector().getName()));
		});
		s.append("Written by:\n");
		writers.forEach(w -> {
			if (w.getAs() != null) {
				if (w.getDetail() != null) s.append(String.format("\t%s (%s) (as %s)%n", w.getWriter().getName(), w.getDetail(), w.getAs()));
				else s.append(String.format("\t%s (as %s)%n", w.getWriter().getName(), w.getAs()));
			} else if (w.getDetail() != null)
				s.append(String.format("\t%s (%s)%n", w.getWriter().getName(), w.getDetail()));
			else s.append(String.format("\t%s%n", w.getWriter().getName()));
		});
		return s.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof Movie)) return false;
		Movie m = (Movie) o;
		return m.getId().equals(getId());
	}
	
	@Override
	public int hashCode() {
		int result = 7;
		return 37 * result + getId().hashCode();
	}
	
}
