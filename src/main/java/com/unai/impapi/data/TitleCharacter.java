package com.unai.impapi.data;

import com.unai.impapi.exception.WrongIdTypeException;

public class TitleCharacter {
	
	private String id;
	private String name;
	
	public TitleCharacter(String id) {
		if (!id.startsWith("ch")) throw new WrongIdTypeException("Character IDs start with \"ch\"");
		this.id = id;
	}
	
	public TitleCharacter() {
		
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
