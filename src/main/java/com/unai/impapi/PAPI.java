package com.unai.impapi;

import java.util.HashSet;
import java.util.Set;

import com.unai.impapi.data.Movie;
import com.unai.impapi.data.Person;

public final class PAPI {
	
	private static Set<Person> people = new HashSet<>();
	private static Set<Movie> movies = new HashSet<>();
	
	private PAPI() {}
	
	public static void addPerson(Person p) {
		people.add(p);
	}
	
	public static void addMovie(Movie m) {
		movies.add(m);
	}
	
	public static Person findPerson(String id) {
		for (Person p : people) {
			if (p.getId() == id) return p;
		}
		return null;
	}
	
	public static Movie findMovie(String id) {
		for (Movie m : movies) {
			if (m.getId() == id) return m;
		}
		return null;
	}

}
