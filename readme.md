# imPAPI [![Build Status](https://travis-ci.org/unaipme/imPAPI.svg?branch=master)](https://travis-ci.org/unaipme/imPAPI) [![codecov](https://codecov.io/gh/unaipme/imPAPI/branch/master/graph/badge.svg)](https://codecov.io/gh/unaipme/imPAPI)

Project for a IMDb public API based on web scraping library JSoup, written in Java. The name stands for 'internet movie public API'.

## Running from command line / terminal

As of now, there's two ways to use the application from the terminal. One is in "specific mode", which gets the information of the provided ID, and the "search mode".

### Specific mode

To use imPAPI in specific mode, clone this repository and run `mvn package` to generate a .jar file. To get the information of a person or movie with known ID, type the following:

```
java -jar target/impapi-<whatever>.jar --id <ID>
```

An example of a person id is nm0348181, which will produce the following output:

```
James Gunn
Born August 5, 1966
Known for:
        Writer of Guardians of the Galaxy (2014)
        Writer of Scooby-Doo (2002)
        Writer of Dawn of the Dead (2004)
        Writer of Guardians of the Galaxy Vol. 2 (2017)
```

An example of a movie id is tt3896198, which will produce the following output:

```
Guardians of the Galaxy Vol. 2 (2017) *8,1
Directed by:
        James Gunn
Written by:
        James Gunn
        Dan Abnett (based on the Marvel comics by)
```

### Search mode

It's possible to tell the application to search for specific information, following the given parameters. Once the .jar is generated, to see all options, type:

```
java -jar target/impapi-<whatever>.jar --help
```

As a quick example, let's say we want to search query 'robert' for any type of information. Search mode must be specified (`-s`, `--search`). As searching for any type is the default operation, we won't specify any type (with `-t`, `--type`). Lastly, we'll specify the query (with `-q`, `--query`). This will result in the following command:

```
java -jar target/impapi-<whatever>.jar -s -q robert
```

If we want to find only people with the exact name (with `-e`, `--exact`) of Robert de Niro, the command must be written as follows:

```
java -jar target/impapi-<whatever>.jar -s -q "Robert de Niro" -e
```

## Running as REST API

To run imPAPI as a REST API, type the following command after cloning the repository:

```
mvn spring-boot:run
```

The REST endpoints will be available at [http://localhost:8080](http://localhost:8080). The documentation of all the endpoints is available at [http://localhost:8080/docs](http://localhost:8080/docs). Search mode is now supported by the REST interface.

## Disclaimer

Actually not a great fan of Guardians of the Galaxy. It's just the first thing I found on IMDb's homepage when writing this readme.
