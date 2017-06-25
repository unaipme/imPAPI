package com.unai.impapi.parser;

import static com.unai.impapi.Utils.iterate;
import static com.unai.impapi.Utils.trim;
import static org.jsoup.Jsoup.connect;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

import com.unai.impapi.PAPI;
import com.unai.impapi.Utils;
import com.unai.impapi.data.Person;
import com.unai.impapi.data.Series;
import com.unai.impapi.data.rel.CreatedBy;
import com.unai.impapi.data.rel.StarringInSeries;
import com.unai.impapi.exception.WrongIdTypeException;

public class SeriesPageParser implements PageParser<Series> {
	
	private final static String URL_TEMPLATE = "https://www.imdb.com/title/%s";
	private final static Pattern intervalPattern = Utils.intervalPattern;
	
	private Series series;
	private Document seriesPage;
	
	public SeriesPageParser(String id, String language) throws IOException {
		if (!isSeriesPage(id)) throw new WrongIdTypeException("ID does not belong to a series");
		series = (Series) PAPI.findTitle(id);
		if (series == null) {
			series = new Series(id);
			PAPI.addTitle(series);
		}
		seriesPage = connect(String.format(URL_TEMPLATE, id)).header("Accept-Language", language).get();
	}
	
	public SeriesPageParser(String id) throws IOException {
		this(id, "en-US");
	}
	
	public static boolean isSeriesPage(String id) {
		try {
			Document doc = connect("https://www.imdb.com/title/" + id).get();
			return !doc.select("a.np_episode_guide").isEmpty();
		} catch (IOException e) {
			return false;
		}
	}
	
	private String getTitle() {
		return trim(seriesPage.select("h1[itemprop=name]").get(0).text());
	}
	
	private Double getRating() {
		return Double.valueOf(seriesPage.select("span[itemprop=ratingValue]").get(0).text().replaceAll(",", "."));
	}
	
	private Integer getStartYear() {
		Matcher m = intervalPattern.matcher(seriesPage.select("a[title=See more release dates]").get(0).text());
		if (m.find()) return Integer.valueOf(m.group().substring(1, 5));
		return null;
	}
	
	private Integer getEndYear() {
		Matcher m = intervalPattern.matcher(seriesPage.select("a[title=See more release dates]").get(0).text());
		try {
			if (m.find()) return Integer.valueOf(m.group().substring(6, 10));
			return null;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	
	private void parseCreators() {
		iterate(seriesPage.select("span[itemprop=creator]>a[href$=tt_ov_wr]"), el -> {
			CreatedBy createdBy = new CreatedBy();
			Person p = null;
			String personId = el.attr("href").substring("/name/".length(), el.attr("href").length() - "?ref_=tt_ov_wr".length());
			p = PAPI.findPerson(personId);
			if (p == null) {
				p = new Person(personId);
				PAPI.addPerson(p);
			}
			p.setName(el.child(0).text());
			createdBy.setPerson(p);
			createdBy.setSeries(series);
			series.addCreator(createdBy);
		});
	}
	
	private void parseStars() {
		iterate(seriesPage.select("span[itemprop=actors]>a[href$=tt_ov_st_sm]"), el -> {
			StarringInSeries starring = new StarringInSeries();
			Person p = null;
			String personId = el.attr("href").substring("/name/".length(), el.attr("href").length() - "?ref_=tt_ov_st_sm".length());
			p = PAPI.findPerson(personId);
			if (p == null) {
				p = new Person(personId);
				PAPI.addPerson(p);
			}
			p.setName(el.child(0).text());
			starring.setStar(p);
			starring.setSeries(series);
			series.addStar(starring);
		});
	}

	@Override
	public Series parse() throws IOException {
		if (series.getTitle() == null) series.setTitle(getTitle());
		if (series.getRating() == null) series.setRating(getRating());
		if (series.getStartYear() == null) series.setStartYear(getStartYear());
		if (series.getEndYear() == null) series.setEndYear(getEndYear());
		if (series.getCreatorList().isEmpty()) parseCreators();
		if (series.getStarring().isEmpty()) parseStars();
		return series;
	}

}
