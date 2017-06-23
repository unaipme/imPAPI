package com.unai.impapi.data;

import static com.unai.impapi.Utils.trim;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unai.impapi.data.rel.DirectedBy;
import com.unai.impapi.data.rel.MovieAppearance;
import com.unai.impapi.data.rel.WrittenBy;

public class Movie extends Title {
	
	private Integer releaseYear;
	private List<DirectedBy> directors = new ArrayList<>();
	private List<WrittenBy> writers = new ArrayList<>();
	private List<MovieAppearance> cast = new ArrayList<>();
	
	public Movie(String id) {
		super(id);
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
		this.rating = Double.valueOf(trim(s).replaceAll(",", "."));
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
	
	@JsonProperty("cast")
	public List<MovieAppearance> getCast() {
		return cast;
	}
	
	@JsonIgnore
	public List<Person> getCastList() {
		return cast.stream().map(MovieAppearance::getPerson).collect(Collectors.toList());
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
	
	public Movie withAppearance(MovieAppearance a) {
		cast.add(a);
		return this;
	}
	
	public void addAppearance(MovieAppearance a) {
		cast.add(a);
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
			s.append(String.format("\t%s ", d.getDirectorName()));
			if (d.getAs() != null) s.append(String.format("(as %s) ", d.getAs()));
			if (!d.getDetails().isEmpty()) d.getDetails().forEach(f -> s.append(String.format("(%s)", f)));
			s.append("\n");
		});
		s.append("Written by:\n");
		writers.forEach(w -> {
			s.append(String.format("\t%s ", w.getWriterName()));
			if (w.getAs() != null) s.append(String.format("(as %s) ", w.getAs()));
			w.getDetails().forEach(f -> s.append(String.format("(%s)", f)));
			s.append("\n");
		});
		s.append("Cast:\n");
		cast.forEach(a -> {
			s.append(String.format("\t%s plays %s%n", a.getPerson().getName(), a.getCharacter().getName()));
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

	@Override
	public int getType() {
		return MOVIE_TITLE;
	}
	
}
