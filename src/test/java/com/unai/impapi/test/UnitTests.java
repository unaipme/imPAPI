package com.unai.impapi.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.unai.impapi.data.Movie;
import com.unai.impapi.data.Person;
import com.unai.impapi.data.rel.DirectedBy;
import com.unai.impapi.data.rel.PersonKnownForRole;
import com.unai.impapi.data.rel.WrittenBy;
import com.unai.impapi.exception.WrongIdTypeException;
import com.unai.impapi.parser.MoviePageParser;
import com.unai.impapi.parser.PersonPageParser;

public class UnitTests {

	private PersonPageParser personParser;
	private MoviePageParser movieParser;
	
	private final String person_id = "nm0000129";
	private final String movie_id = "tt0339291";
	
	@Before
	public void setUp() throws IOException {
		personParser = new PersonPageParser(person_id);
		movieParser = new MoviePageParser(movie_id);
	}
	
	@Test
	public void personParsingTest() throws IOException {
		Person person = personParser.parse();
		assertEquals("Name is not parsed correctly", "Tom Cruise", person.getName());
		assertEquals("Birthday is not parsed correctly", "1962-7-3", person.getBirthdayString());
		assertEquals("Birthplace is not parsed correctly", "Syracuse, New York, USA", person.getBirthplace());
		PersonKnownForRole role = person.getKnownFor().get(0);
		assertEquals("First 'known for' movie title is not parsed correctly", "Top Gun", role.getTitle().getTitle());
		assertEquals("First 'known for' movie ID is not parsed correctly", "tt0092099", role.getTitle().getId());
		assertEquals("First 'known for' role is not correctly parsed", "Maverick", role.getRoleName());
	}
	
	@Test
	public void movieParsingTest() throws IOException {
		Movie movie = movieParser.parse();
		assertEquals("Title is not parsed correctly", "A Series of Unfortunate Events", movie.getTitle());
		assertTrue("Rating is not parsed correctly", movie.getRating() < 7.5 && movie.getRating() > 6.5);
		assertEquals("Release year is not parsed correctly", 2004, movie.getReleaseYear().intValue());
		assertEquals("The amount of directors must be 1, is not", movie.getDirectors().size(), 1);
		DirectedBy director = movie.getDirectors().get(0);
		assertEquals("Director's name is not parsed correctly", "Brad Silberling", director.getDirectorName());
		assertNull("Director's pseudonym should be null, is not", director.getAs());
		assertEquals("The amount of writers must be 2, is not", movie.getWriters().size(), 2);
		WrittenBy firstWriter = movie.getWriters().get(0);
		WrittenBy secondWriter = movie.getWriters().get(1);
		assertEquals("First writer's name is not parsed correctly", "Robert Gordon", firstWriter.getWriterName());
		assertEquals("First writer's ID is not parsed correctly", "nm0330565", firstWriter.getWriterId());
		//assertEquals("First writer's detail is not parsed correctly", "screenplay", firstWriter.getDetail());
		assertNull("First writer's pseudonym should be null, is not", firstWriter.getAs());
		assertEquals("Second writer's name is not parsed correctly", "Daniel Handler", secondWriter.getWriterName());
		assertEquals("Second writer's ID is not parsed correctly", "nm1274516", secondWriter.getWriterId());
		//assertEquals("Second writer's detail is not parsed correctly", "books", secondWriter.getDetail());
		assertEquals("Second writer's pseudonym is not parsed correctly", "Lemony Snicket", secondWriter.getAs());
	}
	
	@Test(expected = WrongIdTypeException.class)
	public void tryToParseMovieFromPersonId() throws IOException {
		new MoviePageParser(person_id).parse();
	}
	
	@Test(expected = WrongIdTypeException.class)
	public void trytoParsePersonFromMovieId() throws IOException {
		new PersonPageParser(movie_id).parse();
	}
	
}
