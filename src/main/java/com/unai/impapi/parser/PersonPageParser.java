package com.unai.impapi.parser;

import static com.unai.impapi.Utils.iterate;
import static org.jsoup.Jsoup.connect;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.unai.impapi.DBIM;
import com.unai.impapi.data.Movie;
import com.unai.impapi.data.Person;
import com.unai.impapi.rel.Role;

public class PersonPageParser implements PageParser<Person> {
	
	private final static String templateUrl = "http://www.imdb.com/name/%s";
	
	private Document loadPage(String id) throws IOException {
		return connect(String.format(templateUrl, id)).header("Accept-Language", "en-US").get();
	}
	
	private String getName(Document doc) {
		return doc.select("span.itemprop[itemprop=name]").get(0).text();
	}
	
	private LocalDate getBirthday(Document doc) {
		String [] nums  = doc.select("time[itemprop=birthDate]").get(0).attr("datetime").split("-");
		List<Integer> list = Arrays.asList(nums).stream().map(Integer::valueOf).collect(Collectors.toList());
		return LocalDate.of(list.get(0), list.get(1), list.get(2));
	}
	
	private String getKnownForMovieTitle(Element el) {
		return el.select("div.knownfor-title-role a.knownfor-ellipsis").get(0).text();
	}
	
	private Integer getKnownForReleaseYear(Element el) {
		String text = el.select("div.knownfor-year span.knownfor-ellipsis").get(0).text();
		return Integer.valueOf(text.substring(1, text.length() - 1));
	}
	
	private String getKnownForRole(Element el) {
		return el.select("div.knownfor-title-role span.knownfor-ellipsis").get(0).text();
	}
	
	private String getKnownForMovieId(Element el) {
		String text = el.select("> a").get(0).attr("href");
		return text.substring("/title/".length(), text.length() - "/?ref_=nmknf_i1".length());
	}
	
	private void parseKnownFor(Document doc, Person person) {
		iterate(doc.select("div.knownfor-title"), e -> {
			Role role = new Role();
			Movie movie = null;
			movie = DBIM.findMovie(getKnownForMovieId(e));
			if (movie == null) {
				movie = new Movie(person.getId());
				movie.setTitle(getKnownForMovieTitle(e));
				movie.setReleaseYear(getKnownForReleaseYear(e));
				DBIM.addMovie(movie);
			}
			role.setMovie(movie);
			role.setPerson(person);
			role.setRole(getKnownForRole(e));
			person.addKnownForRole(role);
		});
	}

	@Override
	public Person parse(String id) throws IOException {
		Person person = new Person(id);
		Document doc = loadPage(id);
		person.setName(getName(doc));
		person.setBirthday(getBirthday(doc));
		parseKnownFor(doc, person);
		DBIM.addPerson(person);
		return person;
	}

}
