package com.unai.impapi;

import java.util.ListIterator;
import java.util.function.Consumer;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.unai.impapi.parser.MoviePageParser;
import com.unai.impapi.parser.PageParser;
import com.unai.impapi.parser.PersonPageParser;

public class Utils {
	
	public static String trim(String s) {
		return s.replace("\u00a0", "").trim();
	}
	
	public static void iterate(Elements els, Consumer<Element> func) {
		ListIterator<Element> it = els.listIterator();
		while (it.hasNext()) {
			func.accept(it.next());
		}
	}
	
	public static PageParser<?> autoparser(String id) {
		if (id.startsWith("tt")) {
			return new MoviePageParser();
		} else if (id.startsWith("nm")) {
			return new PersonPageParser();
		}
		return null;
	}
	
}
