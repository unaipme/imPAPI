package com.unai.impapi.parser;

import static com.unai.impapi.Utils.iterate;
import static org.jsoup.Jsoup.connect;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.unai.impapi.PAPI;
import com.unai.impapi.Utils;
import com.unai.impapi.data.Movie;
import com.unai.impapi.data.Person;
import com.unai.impapi.data.Series;
import com.unai.impapi.data.Title;
import com.unai.impapi.data.rel.Role;

public class PersonPageParser implements PageParser<Person> {
	
	private static final String TEMPLATE_URL = "http://www.imdb.com/name/%s";
	
	private static final Pattern intervalPattern = Utils.intervalPattern;
	
	private Document loadPage(String id) throws IOException {
		return connect(String.format(TEMPLATE_URL, id)).header("Accept-Language", "en-US").get();
	}
	
	private String getName(Document doc) {
		return doc.select("span.itemprop[itemprop=name]").get(0).text();
	}
	
	private LocalDate getBirthday(Document doc) {
		String [] nums  = doc.select("time[itemprop=birthDate]").get(0).attr("datetime").split("-");
		List<Integer> list = Arrays.asList(nums).stream().map(Integer::valueOf).collect(Collectors.toList());
		return LocalDate.of(list.get(0), list.get(1), list.get(2));
	}
	
	private String getBirthplace(Document doc) {
		return doc.select("div#name-born-info>a").text();
	}
	
	private String getKnownForMovieTitle(Element el) {
		return el.select("div.knownfor-title-role a.knownfor-ellipsis").get(0).text();
	}
	
	private Integer getKnownForMovieReleaseYear(Element el) {
		return Integer.valueOf(el.select("div.knownfor-year span.knownfor-ellipsis").get(0).text().substring(1, 5));
	}
	
	private Integer getKnownForSeriesStartYear(Element el) {
		return Integer.valueOf(el.select("div.knownfor-year span.knownfor-ellipsis").get(0).text().substring(1, 5));
	}
	
	private Integer getKnownForSeriesEndYear(Element el) {
		return Integer.valueOf(el.select("div.knownfor-year span.knownfor-ellipsis").get(0).text().substring(6, 10));
	}
	
	private String getKnownForRole(Element el) {
		return el.select("div.knownfor-title-role span.knownfor-ellipsis").get(0).text();
	}
	
	private String getKnownForMovieId(Element el) {
		String text = el.select("> a").get(0).attr("href");
		return text.substring("/title/".length(), text.length() - "/?ref_=nm_knf_i1".length());
	}
	
	private boolean isKnownForMovie(Element el) {
		String text = el.select("div.knownfor-year span.knownfor-ellipsis").get(0).text();
		Matcher m = intervalPattern.matcher(text);
		if (m.find()) return false;
		return true;
	}
	
	private void parseKnownFor(Document doc, Person person) {
		iterate(doc.select("div.knownfor-title"), e -> {
			Role role = new Role();
			boolean isMovie = isKnownForMovie(e);
			Title title = null;
			String titleId = getKnownForMovieId(e);
			title = PAPI.findTitle(titleId);
			if (title == null) {
				if (isMovie) title = new Movie(titleId);
				else title = new Series(titleId);
				PAPI.addTitle(title);
			}
			if (isMovie) {
				((Movie) title).setReleaseYear(getKnownForMovieReleaseYear(e));
			} else {
				((Series) title).setStartYear(getKnownForSeriesStartYear(e));
				((Series) title).setEndYear(getKnownForSeriesEndYear(e));
			}
			title.setTitle(getKnownForMovieTitle(e));
			role.setTitle(title);
			role.setPerson(person);
			role.setRoleName(getKnownForRole(e));
			person.addKnownForRole(role);
		});
	}

	@Override
	public Person parse(String id) throws IOException {
		Person person = new Person(id);
		Document doc = loadPage(id);
		person.setName(getName(doc));
		person.setBirthday(getBirthday(doc));
		person.setBirthplace(getBirthplace(doc));
		parseKnownFor(doc, person);
		PAPI.addPerson(person);
		return person;
	}

}
