A simple project that, when built, produces a jar containing a servlet (Jetty) that hosts a web page and a REST API, as well as an embedded database (Neo4j).

## Requirements to build:
* [Java 8](https://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html)
* [Maven](https://maven.apache.org/)
* [Angular CLI](https://cli.angular.io/)

## To build:
From the base of the project, everything can be build simply by running:

```
mvn
```

### What it's doing:
The project will first compile the Angular project in the `client/` directory and place the output inside the proper directory in the `server/` project. The `server/` Java project is then built and everything is bundled into the `server/target/webserver.jar` file.

## To run the project:
From the base of the project, run:

```
java -jar server/target/webserver.jar
```

For easy copy/paste; to build and run in one step:

```
mvn && java -jar server/target/webserver.jar
```
