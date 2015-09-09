#Overview
This sample REST application is built on the following technology stack:

* Java 8
* Gradle
* Groovy
* Spring Boot
* Spring MVC
* Spring Cloud
* Spring REST Docs
* Spring HATEOAS
* Docker
* JaCoCo
* CodeNarc
* Spock
* Cucumber JVM
* Redis
* Tomcat 8
* SLF4J
* Logback
* Jackson

The entire project contains 74 files and showcases the following:

* Modularizing the Gradle build for greater understandability
* Spring Boot simplifying dependency and configuration management
* Spring Boot managing packaging and self-hosting via embedded Tomcat
* Automated code inspection via CodeNarc
* Automated testing via Spock and Cucumber JVM
* Automated test detection via JaCoCo
* Application packaging and deployment via Docker
* Automated REST API documentation generation via Sprig REST Docs
* Automated acceptance testing via Cucumber JVM
* Construction of hypermedia controls via Spring HATEOAS and Jackson
* Marking and running different types of test in different contexts via JUnit Categories
* Categorizing Spring beans using custom stereotypes for clearer readability and programmer intent
* Thorough error handling using custom Spring MVC error handlers
* Enterprise class log message handling designed to use in distributed logging solutions, such as Logback

The application itself is trivial.  It allows for temporary storage of binary assets in a Redis database.  The
REST API documentation provides details, if you are interested.
