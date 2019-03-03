# Dragons of Mugloar

Dragons of Mugloar scripting adventure for (http://www.dragonsofmugloar.com/)

## Getting Started

Instructions for building, running and configuring.

### Prerequisites

Requires software installed
```$xslt
Java 8
Maven 3
```

### Installing

Open sources folder and run

```mvn clean install```

### Running

Open **target** directory and run

```java -jar dragonsofmugloar-1.0.0-SNAPSHOT.jar```

This will initiate 10 (default) games to be played. Every game result will be logged in the console

If you want you can specify retry games counter in application.properties by changing the property: game.retry.count

## Built with

* Spring Boot
* Jackson
* Mockito
* JUnit

## Authors

Martynas [GitHub](https://github.com/Marciokas)