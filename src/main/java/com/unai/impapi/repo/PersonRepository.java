package com.unai.impapi.repo;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.unai.impapi.data.Person;
import com.unai.impapi.parser.PersonPageParser;

@Service
public class PersonRepository {
	
	private PersonPageParser parser = new PersonPageParser();
	
	public Person findById(String id) {
		if (!id.startsWith("nm")) {
			return null;
		} else try {
				return parser.parse(id);
			} catch (IOException e) {
				return null;
			}
	}
	
}
