package com.unai.impapi;

import static com.unai.impapi.parser.SeriesPageParser.isSeriesPage;

import java.io.IOException;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.unai.impapi.data.PageData;
import com.unai.impapi.parser.MoviePageParser;
import com.unai.impapi.parser.PageParser;
import com.unai.impapi.parser.PersonPageParser;
import com.unai.impapi.parser.SeriesPageParser;

public final class Utils {
	
	public static final Pattern yearPattern = Pattern.compile("[(]([0-9]){4}[)]");
	public static final Pattern romnumPattern = Pattern.compile("[(]([IVXLCDM])+[)]");
	public static final Pattern intervalPattern = Pattern.compile("[(]([0-9]){4}[–]([0-9]){4}[)]");
	public static final Pattern asPattern = Pattern.compile("[(](as )[a-zA-Z\\s\\.\\\"]+[)]");
	public static final Pattern detailPattern = Pattern.compile("[(][a-zA-Z\\s\\.\\\"\\-\\:]+[)]");
	
	private Utils() {}
	
	public static String trim(String s) {
		return s.replace("\u00a0", "").trim();
	}
	
	public static void iterate(Elements els, Consumer<Element> func) {
		ListIterator<Element> it = els.listIterator();
		while (it.hasNext()) {
			func.accept(it.next());
		}
	}
	
	public static PageParser<? extends PageData> autoparser(String id) throws IOException {
		if (id.startsWith("tt")) {
			if (isSeriesPage(id)) return new SeriesPageParser(id);
			else return new MoviePageParser(id);
		} else if (id.startsWith("nm")) {
			return new PersonPageParser(id);
		}
		return null;
	}
	
	public static <T> T ifNullThen(T nullable, T callback) {
		if (nullable == null) return callback;
		else return nullable;
	}
	
}
