package com.unai.impapi.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unai.impapi.data.rel.CreatedBy;
import com.unai.impapi.data.rel.StarringInSeries;

public class Series extends Title {
	
	private Integer startYear;
	private Integer endYear;
	private List<CreatedBy> creatorList = new ArrayList<>();
	private List<StarringInSeries> starList = new ArrayList<>();

	public Series(String id) {
		super(id);
	}
	
	@JsonIgnore
	public Integer getStartYear() {
		return startYear;
	}

	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}

	@JsonIgnore
	public Integer getEndYear() {
		return endYear;
	}

	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}
	
	public String getRunningYears() {
		if (endYear == null) return String.format("(%d-)", startYear);
		else return String.format("(%d-%d)", startYear, endYear);
	}
	
	public List<StarringInSeries> getStarring() {
		return starList;
	}
	
	public Series withStar(StarringInSeries s) {
		addStar(s);
		return this;
	}
	
	public void addStar(StarringInSeries s) {
		starList.add(s);
	}
	
	public List<CreatedBy> getCreatorList() {
		return creatorList;
	}
	
	public Series withCreator(CreatedBy c) {
		addCreator(c);
		return this;
	}
	
	public void addCreator(CreatedBy c) {
		creatorList.add(c);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof Series)) return false;
		Series s = (Series) o;
		return id.equals(s.getId());
	}
	
	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder();
		s.append(String.format("%s (%d-%d) *%.1f%n", title, startYear, endYear, rating));
		s.append("Created by:\n");
		creatorList.forEach(c -> s.append(String.format("\t%s%n", c)));
		s.append("Starring:\n");
		starList.forEach(l -> s.append(String.format("\t%s%n", l)));
		return s.toString();
	}
	
	@Override
	public boolean isSeries() {
		return true;
	}

}
