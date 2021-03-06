package com.unai.impapi.data;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unai.impapi.data.rel.PersonKnownForRole;
import com.unai.impapi.exception.WrongIdTypeException;

public class Person implements PageData {
	
	private String name;
	private String id;
	private String birthplace;
	private LocalDate birthday;
	private List<PersonKnownForRole> knownFor = new ArrayList<>();
	private String number;

	public Person(String id) {
		if (!id.startsWith("nm")) throw new WrongIdTypeException("Person IDs start with \"nm\"");
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	@JsonIgnore
	public LocalDate getBirthday() {
		return birthday;
	}
	
	@JsonProperty("birthday")
	public String getBirthdayString() {
		return String.format("%d-%d-%d", birthday.getYear(), birthday.getMonthValue(), birthday.getDayOfMonth());
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
	
	public void setBirthday(Integer day, Integer month, Integer year) {
		this.birthday = LocalDate.of(year, month, day);
	}
	
	public String getBirthplace() {
		return birthplace;
	}
	
	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<PersonKnownForRole> getKnownFor() {
		return knownFor;
	}

	@JsonInclude(Include.NON_NULL)
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Person)) return false;
		Person p = (Person) obj;
		return p.getId().equals(getId());
	}
	
	@Override
	public int hashCode() {
		int result = 4;
		return 37 * result + getId().hashCode();
	}
	
	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder();
		s.append(String.format("%s%n", name));
		s.append(String.format("Born %s %d, %d, in %s%n", Month.of(birthday.getMonthValue()).getDisplayName(TextStyle.FULL, Locale.ENGLISH), birthday.getDayOfMonth(), birthday.getYear(), birthplace));
		s.append("Known for:\n");
		knownFor.forEach(r -> {
			if (r.getTitle() instanceof Movie) {
				Movie m = (Movie) r.getTitle();
				s.append(String.format("\t%s of %s (%d)%n", r.getRoleName(), m.getTitle(), m.getReleaseYear()));
			} else if (r.getTitle() instanceof Series) {
				Series m = (Series) r.getTitle();
				s.append(String.format("\t%s of %s (%d-%d)%n", r.getRoleName(), m.getTitle(), m.getStartYear(), m.getEndYear()));
			}
		});
		return s.toString();
	}
	
	public void addKnownForRole(PersonKnownForRole role) {
		knownFor.add(role);
	}
	
	public Person isKnownFor(PersonKnownForRole role) {
		knownFor.add(role);
		return this;
	}
	
	public boolean hasNumber() {
		return number != null;
	}
	
}
