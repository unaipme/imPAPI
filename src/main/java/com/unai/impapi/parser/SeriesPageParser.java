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
import com.unai.impapi.data.rel.Starring;

public class SeriesPageParser implements PageParser<Series> {
	
	private final static String URL_TEMPLATE = "https://www.imdb.com/title/%s";
	private final static Pattern intervalPattern = Utils.intervalPattern;
	
	public static boolean isSeriesPage(String id) {
		try {
			Document doc = connect("https://www.imdb.com/title/" + id).get();
			return !doc.select("a.np_episode_guide").isEmpty();
		} catch (IOException e) {
			return false;
		}
	}
	
	private String getTitle(Document doc) {
		return trim(doc.select("h1[itemprop=name]").get(0).text());
	}
	
	private Double getRating(Document doc) {
		return Double.valueOf(doc.select("span[itemprop=ratingValue]").get(0).text());
	}
	
	private Integer getStartYear(Document doc) {
		Matcher m = intervalPattern.matcher(doc.select("a[title=See more release dates]").get(0).text());
		if (m.find()) {
			return Integer.valueOf(m.group().substring(1, 5));
		} else return null;
	}
	
	private Integer getEndYear(Document doc) {
		Matcher m = intervalPattern.matcher(doc.select("a[title=See more release dates]").get(0).text());
		if (m.find()) {
			return Integer.valueOf(m.group().substring(6, 10));
		} else return null;
	}
	
	private void parseCreators(Document doc, Series series) {
		iterate(doc.select("span[itemprop=creator]>a[href$=tt_ov_wr]"), el -> {
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
	
	private void parseStars(Document doc, Series series) {
		iterate(doc.select("span[itemprop=actors]>a[href$=tt_ov_st_sm]"), el -> {
			Starring starring = new Starring();
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
	public Series parse(String id) throws IOException {
		Series series = null;
		series = (Series) PAPI.findTitle(id);
		if (series == null) {
			series = new Series(id);
			PAPI.addTitle(series);
		}
		Document doc = connect(String.format(URL_TEMPLATE, id)).header("Accept-Language", "en-US").get();
		if (series.getTitle() == null) series.setTitle(getTitle(doc));
		if (series.getRating() == null) series.setRating(getRating(doc));
		if (series.getStartYear() == null) series.setStartYear(getStartYear(doc));
		if (series.getEndYear() == null) series.setEndYear(getEndYear(doc));
		if (series.getCreatorList().isEmpty()) parseCreators(doc, series);
		if (series.getStarring().isEmpty()) parseStars(doc, series);
		return series;
	}

}
