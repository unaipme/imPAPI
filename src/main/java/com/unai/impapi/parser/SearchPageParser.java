package com.unai.impapi.parser;

import static com.unai.impapi.Utils.ifNullThen;
import static org.jsoup.Jsoup.connect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.unai.impapi.PAPI;
import com.unai.impapi.Utils;
import com.unai.impapi.data.Movie;
import com.unai.impapi.data.Person;
import com.unai.impapi.data.search.NameResult;
import com.unai.impapi.data.search.SearchResult;
import com.unai.impapi.data.search.SearchType;
import com.unai.impapi.data.search.TitleResult;

public class SearchPageParser {
	
	private Pattern yearPattern = Utils.yearPattern;
	private Pattern romnumPattern = Utils.romnumPattern;
	
	private List<SearchResult> parseResults(Document doc) {
		final ArrayList<SearchResult> resultset = new ArrayList<>();
		Elements sections = doc.select("div.findSection");
		sections.forEach(s -> {
			switch (s.select("h3.findSectionHeader").get(0).ownText()) {
			case "Names":
				resultset.addAll(parseNameResults(s));
				break;
			case "Titles":
				resultset.addAll(parseTitleResults(s));
				break;
			}
		});
		return resultset;
	}
	
	public List<NameResult> parseNameResults(Element el) {
		final ArrayList<NameResult> nameList = new ArrayList<>();
		ListIterator<Element> it = el.select("table.findList tr.findResult td.result_text").listIterator();
		int amount = 1;
		while (it.hasNext()) {
			Element e = it.next();
			NameResult r = new NameResult();
			Element namehref = e.select(">a").get(0);
			String idhref = namehref.attr("href");
			String personId = idhref.substring("/name/".length(), idhref.length() - ("/?ref_=fn_al_nm_" + amount++).length());
			Person person = null;
			person = PAPI.findPerson(personId);
			if (person == null) {
				person = new Person(personId);
				PAPI.addPerson(person);
				r.setPerson(person);
			}
			person.setName(namehref.text());
			Matcher romanNumber = romnumPattern.matcher(e.ownText());
			if (romanNumber.find()) {
				String match = romanNumber.group();
				person.setNumber(match.substring(1, match.length() - 1));
			}
			if (!e.select(">i").isEmpty()) {
				String detail = e.ownText().replace(ifNullThen(r.getFormattedNumber(), ""), "").trim();
				r.setDetail(String.format("%s %s", detail, e.select(">i").get(0).text()));
			}
			if (!e.select(">small").isEmpty()) {
				Element knownFor = e.select(">small").get(0);
				r.setRole(knownFor.ownText().split(",")[0].substring(1));
				r.setKnownForMovie(knownFor.child(0).text());
				Matcher m = yearPattern.matcher(knownFor.ownText().split(",")[1]);
				if (m.find()) {
					r.setKnownForMovieYear(Integer.parseInt(m.group().substring(1, 5)));
				}
			}
			nameList.add(r);
		}
		return nameList;
	}
	
	public List<TitleResult> parseTitleResults(Element el) {
		final ArrayList<TitleResult> titleList = new ArrayList<>();
		ListIterator<Element> it = el.select("table.findList tr.findResult td.result_text").listIterator();
		int amount = 1;
		while (it.hasNext()) {
			Element e = it.next();
			TitleResult result = new TitleResult();
			String idhref = e.select(">a").get(0).attr("href");
			String movieId = idhref.substring("/title/".length(), idhref.length() - ("/?ref_=fn_tt_tt_" + amount++).length());
			Movie movie = null;
			movie = (Movie) PAPI.findTitle(movieId);
			if (movie == null) {
				movie = new Movie(movieId);
				PAPI.addTitle(movie);
			}
			result.setMovie(movie);
			movie.setTitle(e.select(">a").get(0).text());
			Matcher yearMatcher = yearPattern.matcher(e.ownText());
			if (yearMatcher.find()) {
				movie.setReleaseYear(yearMatcher.group());
			}
			Matcher numMatcher = romnumPattern.matcher(e.ownText());
			if (numMatcher.find()) {
				String rs = numMatcher.group();
				movie.setNumber(rs.substring(1, rs.length() - 1));
			}
			Matcher typeMatcher = Pattern.compile("[(]([a-zA-Z])+[)]").matcher(e.ownText());
			if (typeMatcher.find()) {
				for (int i=0; i<typeMatcher.groupCount(); i++) {
					if (!typeMatcher.group(i).matches(romnumPattern.pattern()))
						result.setType(typeMatcher.group(i));
				}
			}
			if (!e.select(">i").isEmpty()) {
				String detail = e.ownText().replace(ifNullThen(result.getType(), ""), "")
						.replace(result.getFormattedYear(), "");
				result.setDetail(String.format("%s %s", detail, e.select(">i").get(0).text()));
			}
			titleList.add(result);
		}
		return titleList;
	}

	public List<SearchResult> parse(String query, SearchType type, boolean isExact) throws IOException {
		StringBuilder uri = new StringBuilder();
		uri.append("http://www.imdb.com/find?");
		uri.append(String.format("q=%s", query));
		uri.append(String.format("&s=%s", type.getAbbr().toLowerCase()));
		uri.append(String.format("&exact=%s", Boolean.valueOf(isExact)));
		return parseResults(connect(uri.toString()).header("Accept-Language", "en-US").get());
	}
	
	public Map<SearchType, List<SearchResult>> parse(String query) throws IOException {
		Map<SearchType, List<SearchResult>> result = new HashMap<>();
		List<SearchResult> allResults = parse(query, SearchType.ALL, false);
		result.put(SearchType.NAME, allResults.stream().filter(r -> r instanceof NameResult).collect(Collectors.toList()));
		result.put(SearchType.TITLE, allResults.stream().filter(r -> r instanceof TitleResult).collect(Collectors.toList()));
		return result;
	}

}
