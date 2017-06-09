package com.unai.impapi;

import java.util.HashSet;
import java.util.Set;

import com.unai.impapi.data.Person;
import com.unai.impapi.data.Title;

public final class PAPI {
	
	private static Set<Person> people = new HashSet<>();
	private static Set<Title> titles = new HashSet<>();
	
	private PAPI() {}
	
	public static void addPerson(Person p) {
		people.add(p);
	}
	
	public static void addTitle(Title title) {
		titles.add(title);
	}
	
	public static Person findPerson(String id) {
		for (Person p : people) {
			if (p.getId() == id) return p;
		}
		return null;
	}
	
	public static Title findTitle(String id) {
		for (Title t : titles) {
			if (t.getId() == id) return t;
		}
		return null;
	}

}
