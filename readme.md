# imPAPI

Project for a IMDb public API based on web scraping library JSoup, written in Java. The name stands for 'internet movie public API'.

### Running

```
mvn package
java -jar target/impapi-<whatever>.jar <person or movie id>
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

### Disclaimer

Actually not a great fan of Guardians of the Galaxy. It's just the first thing I found on IMDb's homepage when writing this readme.