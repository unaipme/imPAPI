package com.unai.impapi.data.search;

public interface SearchResult {
	public static final int NAME_RESULT = 0;
	public static final int TITLE_RESULT = 1;
	public static final int CHARACTER_RESULT = 2;
	public static final int COMPANY_RESULT = 3;
	
	public int getResultType();
}
