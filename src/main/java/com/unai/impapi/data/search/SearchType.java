package com.unai.impapi.data.search;

public enum SearchType {
	NAME("nm", "Names"),
	TITLE("tt", "Titles"),
	CHARS("ch", "Characters"),
	COMPANY("co", "Companies"),
	ALL("all", "All");
	
	private String abbr;
	private String name;
	
	private SearchType(String abbr, String name) {
		this.abbr = abbr;
		this.name = name;
	}
	
	public String getAbbr() {
		return abbr;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static SearchType parse(String s) {
		return valueOf(s.toUpperCase());
	}
}
