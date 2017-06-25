package com.unai.impapi.parser;

import static com.unai.impapi.parser.SeriesPageParser.isSeriesPage;
import static org.jsoup.Jsoup.connect;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.unai.impapi.PAPI;
import com.unai.impapi.data.Movie;
import com.unai.impapi.data.Person;
import com.unai.impapi.data.Series;
import com.unai.impapi.data.Title;
import com.unai.impapi.data.rel.PersonKnownForRole;

public class PersonPageParser implements PageParser<Person> {
	
	private static final String TEMPLATE_URL = "http://www.imdb.com/name/%s";
	
	private Person person;
	private Document personPage;
	
	public PersonPageParser(String id) throws IOException {
		this.person = PAPI.findPerson(id);
		if (this.person == null) {
			this.person = new Person(id);
			PAPI.addPerson(this.person);
		}
		this.personPage = connect(String.format(TEMPLATE_URL, id)).header("Accept-Language", "en-US").get();
	}
	
	private String getName() {
		return personPage.select("span.itemprop[itemprop=name]").get(0).text();
	}
	
	private LocalDate getBirthday() {
		String [] nums  = personPage.select("time[itemprop=birthDate]").get(0).attr("datetime").split("-");
		List<Integer> list = Arrays.asList(nums).stream().map(Integer::valueOf).collect(Collectors.toList());
		return LocalDate.of(list.get(0), list.get(1), list.get(2));
	}
	
	private String getBirthplace() {
		return personPage.select("div#name-born-info>a").text();
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
		String text = el.select("div.knownfor-year span.knownfor-ellipsis").get(0).text();
		if (text.contains("-")) return Integer.valueOf(text.substring(6, 10));
		else return null;
	}
	
	private String getKnownForRole(Element el) {
		return el.select("div.knownfor-title-role span.knownfor-ellipsis").get(0).text();
	}
	
	private String getKnownForMovieId(Element el) {
		String text = el.select("> a").get(0).attr("href");
		return text.substring("/title/".length(), text.length() - "/?ref_=nm_knf_i1".length());
	}
	
	private boolean isKnownForMovie(String id) {
		return !isSeriesPage(id);
	}
	
	private void parseKnownFor() {
		ListIterator<Element> it = personPage.select("div.knownfor-title").listIterator();
		while (it.hasNext()) {
			Element e = it.next();
			PersonKnownForRole role = new PersonKnownForRole();
			String titleId = getKnownForMovieId(e);
			boolean isMovie = isKnownForMovie(titleId);
			Title title = null;
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
			role.setRoleName(getKnownForRole(e).replaceAll("\"", "'"));
			person.addKnownForRole(role);
		}
	}

	@Override
	public Person parse() throws IOException {
		if (person.getName() == null) person.setName(getName());
		if (person.getBirthday() == null) person.setBirthday(getBirthday());
		if (person.getBirthplace() == null) person.setBirthplace(getBirthplace());
		if (person.getKnownFor().isEmpty()) parseKnownFor();
		return person;
	}

}
