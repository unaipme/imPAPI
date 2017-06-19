package com.unai.impapi.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unai.impapi.data.search.NameResult;
import com.unai.impapi.data.search.SearchResult;
import com.unai.impapi.data.search.SearchType;
import com.unai.impapi.data.search.TitleResult;
import com.unai.impapi.parser.SearchPageParser;

@RestController
@RequestMapping("/search")
public class SearchController {
	
	private SearchPageParser parser = new SearchPageParser();
	
	@GetMapping("/all/{query}")
	public Map<SearchType, List<SearchResult>> searchAll(@PathVariable String query) throws IOException {
		return parser.parse(query);
	}
	
	@GetMapping("/titles/{query}")
	public List<TitleResult> searchTitles(@PathVariable String query) throws IOException {
		List<TitleResult> results = parser.parseTitleResults(query, false);
		results.forEach(r -> r.add(linkTo(methodOn(MovieController.class).getMovieWithId(r.getMovieId())).withSelfRel()));
		return results;
	}
	
	@GetMapping("/people/{query}")
	public List<NameResult> searchPeople(@PathVariable String query) throws IOException {
		List<NameResult> results = parser.parseNameResults(query, false);
		results.forEach(r -> r.add(linkTo(methodOn(PersonController.class).getPersonWithId(r.getPersonId())).withSelfRel()));
		return results;
	}
	
	@GetMapping("/titles/{query}/exact")
	public List<TitleResult> searchExactTitles(@PathVariable String query) throws IOException {
		List<TitleResult> results = parser.parseTitleResults(query, true);
		results.forEach(r -> r.add(linkTo(methodOn(MovieController.class).getMovieWithId(r.getMovieId())).withSelfRel()));
		return results;
	}
	
	@GetMapping("/people/{query}/exact")
	public List<NameResult> searchExactNames(@PathVariable String query) throws IOException {
		List<NameResult> results = parser.parseNameResults(query, true);
		results.forEach(r -> r.add(linkTo(methodOn(PersonController.class).getPersonWithId(r.getPersonId())).withSelfRel()));
		return results;
	}
	
}
