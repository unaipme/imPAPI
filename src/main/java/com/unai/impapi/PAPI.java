package com.unai.impapi;

import java.util.HashSet;
import java.util.Set;

import com.unai.impapi.data.Person;
import com.unai.impapi.data.Title;
import com.unai.impapi.data.TitleCharacter;

public final class PAPI {
	
	private static Set<Person> people = new HashSet<>();
	private static Set<Title> titles = new HashSet<>();
	private static Set<TitleCharacter> characters = new HashSet<>();
	
	private PAPI() {}
	
	public static void addPerson(Person p) {
		people.add(p);
	}
	
	public static void addTitle(Title title) {
		titles.add(title);
	}
	
	public static void addCharacter(TitleCharacter chr) {
		characters.add(chr);
	}
	
	public static Person findPerson(String id) {
		for (Person p : people) {
			if (p.getId().equals(id)) return p;
		}
		return null;
	}
	
	public static Title findTitle(String id) {
		for (Title t : titles) {
			if (t.getId().equals(id)) return t;
		}
		return null;
	}
	
	public static TitleCharacter findCharacter(String id) {
		for (TitleCharacter c : characters) {
			if (c.getId().equals(id)) return c;
		}
		return null;
	}

}
