package com.unai.impapi.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unai.impapi.data.Series;
import com.unai.impapi.repo.SeriesRepository;

@RestController
@RequestMapping("/series")
public class SeriesController {
	
	@Autowired
	private SeriesRepository repository;
	
	@GetMapping
	public ResponseEntity<String> ping() {
		return ResponseEntity.ok("OK");
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Series> getSeriesWithId(@PathVariable String id) {
		Series series = repository.findOne(id);
		HttpStatus status = HttpStatus.OK;
		if (series == null) status = HttpStatus.NOT_FOUND;
		else {
			series.getCreatorList().forEach(c -> {
				if (!c.hasLink("self"))
					c.add(linkTo(methodOn(PersonController.class).getPersonWithId(c.getPersonId())).withSelfRel());
			});
			series.getStarring().forEach(s -> {
				if (!s.hasLink("self"))
					s.add(linkTo(methodOn(PersonController.class).getPersonWithId(s.getPersonId())).withSelfRel());
			});
		}
		return new ResponseEntity<>(series, status);
	}
	
}
