package com.unai.impapi.repo;

import java.io.IOException;

import org.springframework.stereotype.Repository;

import com.unai.impapi.data.Person;
import com.unai.impapi.parser.PersonPageParser;

@Repository
public class PersonRepository implements PapiRepository<Person> {
	
	public Person findOne(String id) {
		if (!id.startsWith("nm")) {
			return null;
		} else try {
				return new PersonPageParser(id).parse();
			} catch (IOException e) {
				return null;
			}
	}
	
}
