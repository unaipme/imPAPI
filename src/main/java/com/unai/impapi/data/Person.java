package com.unai.impapi.data;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.unai.impapi.rel.Role;

public class Person {
	
	private String name;
	private String id;
	private LocalDate birthday;
	private Set<Role> knownFor = new HashSet<>();
	
	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
	
	public void setBirthday(Integer day, Integer month, Integer year) {
		this.birthday = LocalDate.of(year, month, day);
	}

	public Person(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		else if (!(obj instanceof Person)) return false;
		Person p = (Person) obj;
		return p.getId() == getId();
	}

	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder();
		s.append(String.format("%s\n", name));
		s.append(String.format("Born %s %d, %d\n", Month.of(birthday.getMonthValue()).getDisplayName(TextStyle.FULL, Locale.ENGLISH), birthday.getDayOfMonth(), birthday.getYear()));
		s.append("Known for:\n");
		knownFor.forEach(r -> {
			s.append(String.format("\t%s of %s (%d)\n", r.getRole(), r.getMovie().getTitle(), r.getMovie().getReleaseYear()));
		});
		return s.toString();
	}
	
	public void addKnownForRole(Role role) {
		knownFor.add(role);
	}
	
	public Person isKnownFor(Role role) {
		knownFor.add(role);
		return this;
	}
	
}
