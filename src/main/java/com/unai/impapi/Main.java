package com.unai.impapi;

import static com.unai.impapi.Utils.autoparser;
import static com.unai.impapi.Utils.ifNullThen;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.unai.impapi.data.PageData;
import com.unai.impapi.data.search.SearchType;
import com.unai.impapi.parser.PageParser;
import com.unai.impapi.parser.SearchPageParser;

public class Main {
	
	public Options options() {
		Options options = new Options();
		options.addOption(Option.builder("s").longOpt("search").desc("Use in search mode").build());
		options.addOption(Option.builder("e").longOpt("exact").desc("Search for exact query, in search mode only").build());
		options.addOption(Option.builder("q").longOpt("query").hasArg().desc("Query to search for, in search mode only").build());
		options.addOption(Option.builder("t").longOpt("type").hasArg().desc("Specify type of search, in search mode only. Options:\n\t'name' to search for people\n\t'title' to search movies and series\n\t'company' to search companies\n\t'chars' to search characters\n\t'all' to search all (default)").build());
		options.addOption(Option.builder("i").longOpt("id").hasArg().desc("Specify ID of element to parse, when not using search mode").build());
		options.addOption(Option.builder("h").longOpt("help").desc("Shows this help message").build());
		return options;
	}
	
	private void searchMode(CommandLine cmd) {
		try {
			SearchPageParser parser = new SearchPageParser();
			String query = cmd.getOptionValue("q");
			if (query == null) {
				System.out.println("Query must be set for search mode.");
				System.exit(-1);
			}
			SearchType type = SearchType.parse(ifNullThen(cmd.getOptionValue("t"), "all"));
			if (type == SearchType.ALL) System.out.println(parser.parse(query));
			else System.out.println(parser.parse(query, type, cmd.hasOption("e")));
		} catch (IllegalArgumentException e) {
			System.out.println("An error occurred. You may have introduced an incorrect type of search. Check that you have written a valid one.");
			System.out.println(e.getLocalizedMessage());
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("Something happened when trying to connect to IMDb. Try again later or report the error.");
			System.exit(-1);
		}
	}
	
	private void specificMode(CommandLine cmd) {
		String id = cmd.getOptionValue("i");
		if (id == null) {
			System.out.println("ID must be specified.");
			System.exit(-1);
		}
		PageParser<? extends PageData> parser = autoparser(id);
		if (parser == null) {
			System.out.println("No parser could be found for the given ID");
			System.exit(-1);
		} else {
			try {
				System.out.println(parser.parse(id));
			} catch (IOException e) {
				System.out.println("The parser failed");
				System.exit(-1);
			}
		}
	}
	
	public void app(String [] args) {
		CommandLine cmd = null;
		try {
			cmd = new DefaultParser().parse(options(), args);
			if (cmd.hasOption("h")) {
				new HelpFormatter().printHelp("imPAPI", options());
				System.exit(0);
			}
			if (cmd.hasOption("s")) searchMode(cmd);
			else specificMode(cmd);
		} catch (ParseException e) {
			System.out.println("Arguments could not be correctly parsed. Check if they have been correctly written.");
			System.exit(-1);
		}
		
	}

	public static void main(String[] args) {
		new Main().app(args);
	}

}
