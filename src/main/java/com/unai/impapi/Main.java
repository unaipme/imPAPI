package com.unai.impapi;

import static com.unai.impapi.Utils.autoparser;

import java.io.IOException;

import com.unai.impapi.parser.PageParser;

public class Main {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Needs one parameter.");
			return;
		}
		String id = args[0];
		PageParser<?> parser = autoparser(id);
		if (parser == null) {
			System.out.println("No parser could be found for the given ID");
		} else {
			try {
				System.out.println(parser.parse(id));
			} catch (IOException e) {
				System.out.println("The parser failed");
			}
		}
	}

}
