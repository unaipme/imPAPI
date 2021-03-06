package com.unai.impapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unai.impapi.data.Person;
import com.unai.impapi.repo.PersonRepository;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/people")
public class PersonController {
	
	@Autowired
	private PersonRepository personRepository;
	
	@GetMapping
	public ResponseEntity<String> ping() {
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Person> getPersonWithId(@PathVariable String id) {
		Person person = personRepository.findOne(id);
		HttpStatus status = HttpStatus.OK;
		if (person == null) status = HttpStatus.NOT_FOUND;
		else {
			person.getKnownFor().forEach(m -> {
				if (!m.hasLink("self")) {
					if (m.getTitle().isSeries()) m.add(linkTo(methodOn(SeriesController.class).getSeriesWithId(m.getTitleId())).withSelfRel());
					else m.add(linkTo(methodOn(MovieController.class).getMovieWithId(m.getTitleId(), null)).withSelfRel());
				}
			});
		}
		return new ResponseEntity<>(person, status);
	}
	
}
