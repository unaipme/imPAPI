package com.unai.impapi.parser;

import java.io.IOException;

public interface PageParser<T> {
	public T parse() throws IOException;
}
