package com.unai.impapi.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unai.impapi.data.Movie;
import com.unai.impapi.repo.MovieRepository;

@RestController
@RequestMapping("/movies")
public class MovieController {
	
	@Autowired
	private MovieRepository movieRepository;
	
	@GetMapping
	public ResponseEntity<String> ping() {
		return ResponseEntity.ok("OK");
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Movie> getMovieWithId(@PathVariable String id, @Autowired HttpServletRequest req) {
		Movie movie = null;
		if (req.getHeader("Accept-Language") != null) movie = movieRepository.findOne(id, req.getHeader("Accept-Language"));
		else movie = movieRepository.findOne(id);
		HttpStatus status = HttpStatus.OK;
		if (movie == null) status = HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(movie, status);
	}
	
}
