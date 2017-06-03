package com.unai.impapi.repo;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.unai.impapi.data.Movie;
import com.unai.impapi.parser.MoviePageParser;

@Service
public class MovieRepository {
	
	private MoviePageParser parser = new MoviePageParser();
	
	public Movie findById(String id) {
		if (!id.startsWith("tt")) return null;
		else try {
				return parser.parse(id);
			} catch (IOException e) {
				return null;
			}
	}
	
}
