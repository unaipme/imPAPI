package com.unai.impapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unai.impapi.data.Movie;
import com.unai.impapi.repo.MovieRepository;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

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
	public ResponseEntity<Movie> getMovieWithId(@PathVariable String id) {
		Movie movie = movieRepository.findById(id);
		HttpStatus status = HttpStatus.OK;
		if (movie == null) status = HttpStatus.NOT_FOUND;
		else {
			movie.getWriters().forEach(w -> 
				w.add(linkTo(methodOn(PersonController.class).getPersonWithId(w.getWriterId())).withSelfRel())
			);
			movie.getDirectors().forEach(d -> 
				d.add(linkTo(methodOn(PersonController.class).getPersonWithId(d.getDirectorId())).withSelfRel())
			);
		}
			
		return new ResponseEntity<>(movie, status);
	}
	
}
