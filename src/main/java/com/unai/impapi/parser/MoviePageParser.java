package com.unai.impapi.parser;

import static com.unai.impapi.Utils.iterate;
import static org.jsoup.Jsoup.connect;

import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.unai.impapi.PAPI;
import com.unai.impapi.data.Movie;
import com.unai.impapi.data.Person;
import com.unai.impapi.data.rel.DirectedBy;
import com.unai.impapi.data.rel.WrittenBy;

public class MoviePageParser implements PageParser<Movie> {
	
	private static final String URL_TEMPLATE = "http://www.imdb.com/title/%s";
	
	private String getTitle(Document doc) {
		Element el = doc.select("h1[itemprop=name]").get(0);
		return el.ownText();
	}
	
	private Integer getReleaseDate(Document doc) {
		Element el = doc.select("h1[itemprop=name]").get(0);
		String text = el.child(0).text();
		return Integer.valueOf(text.substring(1, text.length() - 1));
	}
	
	private Double getRating(Document doc) {
		String text = doc.select("span[itemprop=ratingValue]").get(0).text();
		return Double.valueOf(text.replaceAll(",", "."));
	}
	
	private Elements getDirectors(Document doc) {
		return doc.select("span[itemprop=director]");
	}
	
	private Elements getWriters(Document doc) {
		return doc.select("span[itemprop=creator][itemtype$=Person]");
	}
	
	private String getDirectorId(Element e) {
		String href = e.child(0).attr("href");
		return href.substring("/name/".length(), href.length() - "?ref_=tt_ov_dr".length());
	}
	
	private String getWriterId(Element e) {
		String href = e.child(0).attr("href");
		return href.substring("/name/".length(), href.length() - "?ref_=tt_ov_wr".length());
	}
	
	private void parseDirectors(Document doc, Movie movie) {
		iterate(getDirectors(doc), e -> {
			Person p = null;
			DirectedBy directedBy = new DirectedBy();
			String id = getDirectorId(e);
			p = PAPI.findPerson(id);
			if (p == null) {
				p = new Person(id);
				p.setName(e.child(0).text());
				PAPI.addPerson(p);
			}
			directedBy.setDirector(p);
			directedBy.setMovie(movie);
			directedBy.setAs(e.ownText());
			movie.addDirector(directedBy);
		});
	}
	
	private void parseWriters(Document doc, Movie movie) {
		iterate(getWriters(doc), e -> {
			Person p = null;
			WrittenBy writtenBy = new WrittenBy();
			String id = getWriterId(e);
			p = PAPI.findPerson(id);
			if (p == null) {
				p = new Person(id);
				p.setName(e.child(0).text());
				PAPI.addPerson(p);
			}
			writtenBy.setWriter(p);
			writtenBy.setMovie(movie);
			if (e.ownText().indexOf(")") < e.ownText().length() - 1 && e.ownText().indexOf(")") != -1)
				writtenBy.parseText(e.ownText());
			else {
				if (e.ownText().startsWith("(as ")) writtenBy.setAs(e.ownText());
				else if (e.ownText().startsWith("(")) writtenBy.setDetail(e.ownText());
			}
			movie.addWriter(writtenBy);
		});
	}
	
	public Movie parse(String mId) throws IOException {
		return parse(mId, "en-US");
	}
	
	public Movie parse(String mId, String language) throws IOException {
		Movie movie = null;
		movie = (Movie) PAPI.findTitle(mId);
		if (movie == null) {
			movie = new Movie(mId);
			PAPI.addTitle(movie);
		}
		Document doc = connect(String.format(URL_TEMPLATE, mId)).header("Accept-Language", language).get();
		if (movie.getTitle() == null) movie.setTitle(getTitle(doc));
		if (movie.getReleaseYear() == null) movie.setReleaseYear(getReleaseDate(doc));
		if (movie.getRating() == null) movie.setRating(getRating(doc));
		if (movie.getDirectors().isEmpty()) parseDirectors(doc, movie);
		if (movie.getWriters().isEmpty()) parseWriters(doc, movie);
		return movie;
	}
	
}
