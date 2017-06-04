package com.unai.impapi.test;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.unai.impapi.data.Person;
import com.unai.impapi.parser.PersonPageParser;
import static org.junit.Assert.*;

public class UnitTests {

	private PersonPageParser parser;
	
	@Before
	public void setUp() {
		parser = new PersonPageParser();
	}
	
	@Test
	public void personParsingTest() throws IOException {
		Person person = parser.parse("nm0000129");
		assertEquals("Name is not parsed correctly", "Tom Cruise", person.getName());
	}
	
}
