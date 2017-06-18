package com.unai.impapi.repo;

import java.io.IOException;

import org.springframework.stereotype.Repository;

import com.unai.impapi.data.Series;
import com.unai.impapi.parser.SeriesPageParser;

@Repository
public class SeriesRepository implements PapiRepository<Series> {
	
	private SeriesPageParser parser = new SeriesPageParser();

	public Series findOne(String id) {
		if (!id.startsWith("tt")) {
			return null;
		} else try {
			return parser.parse(id);
		} catch (IOException e) {
			return null;
		}
	}
	
}
