package com.unai.impapi.parser;

import static com.unai.impapi.Utils.iterate;
import static org.jsoup.Jsoup.connect;

import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.unai.impapi.DBIM;
import com.unai.impapi.data.Movie;
import com.unai.impapi.data.Person;
import com.unai.impapi.rel.DirectedBy;
import com.unai.impapi.rel.WrittenBy;

public class MoviePageParser implements PageParser {
	
	private static final String urlTemplate = "http://www.imdb.com/title/%s";
	
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
		return Double.valueOf(text);
	}
	
	private Elements getDirectors(Document doc) {
		Elements el = doc.select("span[itemprop=director]");
		return el;
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
			p = DBIM.findPerson(id);
			if (p == null) {
				p = new Person(id);
				p.setName(e.child(0).text());
				DBIM.addPerson(p);
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
			p = DBIM.findPerson(id);
			if (p == null) {
				p = new Person(id);
				p.setName(e.child(0).text());
				DBIM.addPerson(p);
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
		Document doc = connect(String.format(urlTemplate, mId)).header("Accept-Language", "en-US").get();
		Movie movie = new Movie(mId);
		movie.setTitle(getTitle(doc));
		movie.setReleaseYear(getReleaseDate(doc));
		movie.setRating(getRating(doc));
		parseDirectors(doc, movie);
		parseWriters(doc, movie);
		DBIM.addMovie(movie);
		return movie;
	}
	
}
