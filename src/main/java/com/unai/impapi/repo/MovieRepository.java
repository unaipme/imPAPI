package com.unai.impapi.repo;

import java.io.IOException;

import org.springframework.stereotype.Repository;

import com.unai.impapi.data.Movie;
import com.unai.impapi.parser.MoviePageParser;

@Repository
public class MovieRepository implements PapiRepository<Movie> {
	
	public Movie findOne(String id) {
		return findOne(id, "en-US");
	}
	
	public Movie findOne(String id, String language) {
		if (!id.startsWith("tt")) return null;
		else try {
				return new MoviePageParser(id, language).parse();
			} catch (IOException e) {
				return null;
			}
	}
	
}
