package com.unai.impapi.repo;

import java.io.IOException;

import org.springframework.stereotype.Repository;

import com.unai.impapi.data.Movie;
import com.unai.impapi.parser.MoviePageParser;

@Repository
public class MovieRepository implements PapiRepository<Movie> {
	
	private MoviePageParser parser = new MoviePageParser();
	
	public Movie findOne(String id) {
		if (!id.startsWith("tt")) return null;
		else try {
				return parser.parse(id);
			} catch (IOException e) {
				return null;
			}
	}
	
}
