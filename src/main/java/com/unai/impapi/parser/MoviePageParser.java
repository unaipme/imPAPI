package com.unai.impapi.parser;

import static com.unai.impapi.Utils.ifNullThen;
import static com.unai.impapi.Utils.trim;
import static com.unai.impapi.parser.SeriesPageParser.isSeriesPage;
import static org.jsoup.Jsoup.connect;

import java.io.IOException;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.unai.impapi.PAPI;
import com.unai.impapi.Utils;
import com.unai.impapi.data.Movie;
import com.unai.impapi.data.Person;
import com.unai.impapi.data.Title;
import com.unai.impapi.data.TitleCharacter;
import com.unai.impapi.data.rel.DirectedBy;
import com.unai.impapi.data.rel.MovieAppearance;
import com.unai.impapi.data.rel.WrittenBy;
import com.unai.impapi.exception.WrongIdTypeException;

public class MoviePageParser implements PageParser<Movie> {
	
	private static final String MOVIE_PAGE_TEMPLATE = "http://www.imdb.com/title/%s";
	private static final String CREDITS_PAGE_TEMPLATE = "http://www.imdb.com/title/%s/fullcredits";
	
	private static final Pattern asPattern = Utils.asPattern;
	private static final Pattern detailPattern = Utils.detailPattern;
	
	private Movie movie;
	private Document moviePage;
	private Document castPage;
	
	public MoviePageParser(String id) throws IOException {
		this(id, "en-US");
	}
	
	public MoviePageParser(String id, String language) throws IOException {
		if (isSeriesPage(id)) throw new WrongIdTypeException("ID does not belong to a movie");
		if (PAPI.findTitle(id) != null) {
			Title title = PAPI.findTitle(id);
			this.movie = (Movie) title;
		} else {
			this.movie = new Movie(id);
			PAPI.addTitle(this.movie);
		}
		this.moviePage = connect(String.format(MOVIE_PAGE_TEMPLATE, id)).header("Accept-Language", language).get();
		this.castPage = connect(String.format(CREDITS_PAGE_TEMPLATE, id)).header("Accept-Language", language).get();
	}
	
	private String getTitle() {
		Element el = moviePage.select("h1[itemprop=name]").get(0);
		return trim(el.ownText());
	}
	
	private Integer getReleaseDate() {
		Element el = moviePage.select("h1[itemprop=name]").get(0);
		String text = el.child(0).text();
		return Integer.valueOf(text.substring(1, text.length() - 1));
	}
	
	private Double getRating() {
		String text = moviePage.select("span[itemprop=ratingValue]").get(0).text();
		return Double.valueOf(text.replaceAll(",", "."));
	}
	
	private Elements getDirectors() {
		return castPage.select("table.simpleCreditsTable:first-of-type tr");
	}
	
	private Elements getWriters() {
		return castPage.select("table.simpleCreditsTable:nth-of-type(2) tr");
	}
	
	private Elements getCast() {
		return castPage.select("table.cast_list tr.odd, table.cast_list tr.even");
	}
	
	private String getDirectorId(Element e, int n) {
		String href = e.child(0).child(0).attr("href");
		return href.substring("/name/".length(), href.length() - ("/?ref_=ttfc_fc_dr" + n).length());
	}
	
	private String getWriterId(Element e, int n) {
		String href = e.child(0).child(0).attr("href");
		return href.substring("/name/".length(), href.length() - ("/?ref_=ttfc_fc_wr" + n).length());
	}
	
	private void parseCast() {
		int n = 1;
		ListIterator<Element> it = getCast().listIterator();
		while (it.hasNext()) {
			Element e = it.next();
			Person p = null;
			TitleCharacter c = null;
			MovieAppearance app = new MovieAppearance();
			String href = e.select("td.itemprop>a[itemprop=url]").get(0).attr("href");
			String pId = href.substring("/name/".length(), href.length() - ("/?ref_=tt_cl_t" + n).length());
			p = PAPI.findPerson(pId);
			if (p == null) {
				p = new Person(pId);
				p.setName(e.select("td[itemprop=actor] span[itemprop=name]").get(0).ownText());
				PAPI.addPerson(p);
			}
			Matcher asMatcher = asPattern.matcher(e.select("td.character div").get(0).text());
			if (asMatcher.find()) {
				String as = asMatcher.group();
				app.setAs(as.substring(4, as.length() - 1));
			}
			Matcher detailMatcher = detailPattern.matcher(e.select("td.character div").get(0).text().replace("(as " + ifNullThen(app.getAs() + ")", ""), ""));
			while (detailMatcher.find()) {
				String detail = detailMatcher.group();
				app.addDetail(detail.substring(1, detail.length() - 1));
			}
			app.setPerson(p);
			if (!e.select("td.character>div>a").isEmpty()) {
				href = e.select("td.character>div>a").get(0).attr("href");
				String cId = href.substring("/character/".length(), href.length() - ("/?ref_=tt_cl_t" + n++).length());
				c = PAPI.findCharacter(cId);
				if (c == null) {
					c = new TitleCharacter(cId);
					c.setName(e.select("td.character>div>a").get(0).ownText());
					PAPI.addCharacter(c);
				}
			} else {
				c = new TitleCharacter();
				String name = e.select("td.character>div").get(0).text().replace("(as " + ifNullThen(app.getAs(), "") + ")", "").replaceAll("\"", "'").trim();
				app.getDetails().forEach(d -> name.replace("(" + d + ")", ""));
				c.setName(name);
			}
			app.setCharacter(c);
			app.setMovie(movie);
			movie.addAppearance(app);
		};
	}
	
	private void parseDirectors() {
		ListIterator<Element> it = getDirectors().listIterator();
		int n = 1;
		while (it.hasNext()) {
			Element e = it.next();
			if (e.child(0).attr("colspan").equals("3")) continue;
			Person p = null;
			DirectedBy directedBy = new DirectedBy();
			String id = getDirectorId(e, n++);
			p = PAPI.findPerson(id);
			if (p == null) {
				p = new Person(id);
				PAPI.addPerson(p);
			}
			if (p.getName() == null) p.setName(e.select("td.name>a").get(0).ownText());
			directedBy.setDirector(p);
			directedBy.setMovie(movie);
			if (!e.select("td.credit").isEmpty()) {
				Matcher asMatcher = asPattern.matcher(e.select("td.credit").get(0).ownText());
				if (asMatcher.find()) {
					directedBy.setAs(asMatcher.group());
				}
				Matcher detailMatcher = detailPattern.matcher(e.select("td.credit").get(0).ownText().replaceAll("(as " + ifNullThen(directedBy.getAs(), "") + ")", ""));
				while (detailMatcher.find()) {
					String match = detailMatcher.group();
					directedBy.addDetail(match.substring(1, match.length() - 1).replaceAll("\"", "'"));
				}
			}
			movie.addDirector(directedBy);
		}
	}
	
	private void parseWriters() {
		ListIterator<Element> it = getWriters().listIterator();
		int n = 1;
		while (it.hasNext()) {
			Element e = it.next();
			if (e.child(0).attr("colspan").equals("3")) continue;
			Person p = null;
			WrittenBy writtenBy = new WrittenBy();
			String id = getWriterId(e, n++);
			p = PAPI.findPerson(id);
			if (p == null) {
				p = new Person(id);
				PAPI.addPerson(p);
			}
			if (p.getName() == null) p.setName(e.select("td.name>a").get(0).ownText());
			writtenBy.setWriter(p);
			writtenBy.setMovie(movie);
			if (!e.select("td.credit").isEmpty()) {
				Matcher asMatcher = asPattern.matcher(e.select("td.credit").get(0).ownText());
				if (asMatcher.find()) {
					writtenBy.setAs(asMatcher.group());
				}
				Matcher detailMatcher = detailPattern.matcher(e.select("td.credit").get(0).ownText().replaceAll("(as " + ifNullThen(writtenBy.getAs(), "") + ")", ""));
				while (detailMatcher.find()) {
					String match = detailMatcher.group();
					writtenBy.addDetail(match.substring(1, match.length() - 1).replaceAll("\"", "'"));
				}
			}
			movie.addWriter(writtenBy);
		}
	}
	
	public Movie parse() throws IOException {
		if (movie.getTitle() == null) movie.setTitle(getTitle());
		if (movie.getReleaseYear() == null) movie.setReleaseYear(getReleaseDate());
		if (movie.getRating() == null) movie.setRating(getRating());
		if (movie.getDirectors().isEmpty()) parseDirectors();
		if (movie.getWriters().isEmpty()) parseWriters();
		if (movie.getCast().isEmpty()) parseCast();
		return movie;
	}
	
}
