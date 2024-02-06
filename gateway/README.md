# Gateway-Service

Gateway service application.

This application includes the following:

	- Springboot Eureka Server Service2service communication
	- Springboot Gateway
	- Springboot Gateway monitor with spring-boot-starter-actuator

_______________________________________________________________________________
## Development setup

Install OpenJDK 17
Minimum required Java version is 17:

    sudo apt install openjdk-17-jdk

Install Eclipse IDE Java EE:
    
    https://www.eclipse.org/downloads/

Clone this project and open the folder in Eclipse.

_______________________________________________________________________________
## Build

**Run the following to build the project for stand alone application:**

    ./gradlew clean
    ./gradlew bootJar

This will create the Jar file:
    
    "build/libs/gateway-X.X.X-SNAPSHOT.jar"

Run jar file:

    java -jar build/libs/gateway-X.X.X-SNAPSHOT.jar
    
## Access the Gateway

	http://localhost:9000/**

## Access the Gateway monitor

	http://localhost:9000/actuator

_______________________________________________________________________________
## JavaDoc

	$ ./gradlew javadoc

Open file build/docs/javadoc/index.html

