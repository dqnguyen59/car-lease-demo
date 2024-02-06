# Customer Lease Demo : Customer-Service

User-Service application.

This application includes the following:

	- Springboot ORM Hibernate
	- Springboot H2Database (H2-Console) / MySQL Database (modify application.properties for Mysql-connector)
	- Springboot security
	- Springboot Eureka client Service2service communication    
	- JSON Web Token Security
	- JUnit with MockMvc
	- SwaggerUI Open API
	- JavaDoc
 
 This application contains a simple data model with one entity:
 
	- Customer
 
 Result:
 
	- REST-API (http://localhost:6000/api/v1/*)
	- SwaggerUI (http://localhost:6000/swagger-ui/index.html)
	- H2-Console (http://localhost:6000/h2-console/)

Object-Relational Mapping (ORM) allows us to convert Java data class into relational database tables,
without the need to design a database model. Changes made in the code will have an effect on the
database. In other words, it will synchronize the database with the data class, 
using the spring.jpa.hibernate.ddl-auto=update. When running for the first time it will create a database and its tables.

For convenience, H2Database is used to have a relational database that will run in memory only.
There's a MySQL connector included, if one wishes to test it on a real database. You can use docker/mysql_docker.sh to create a docker image/container.

	mysql_docker.sh -b	# build MySQL-server docker image
	mysql_docker.sh -n -d	# Run docker and MySQL server
	mysql_docker.sh -q	# Stop docker and MySQL server

JSON Web Token Security is used to have a secured Rest-API.
JUnit is included to test the API end-points using MockMvc.

Swagger-UI is included to document the API end-points.

This customer-service application use a gateway to access user authentication.
Change the gateway details in the application.properties if needed:

	carlease.gateway.****
_______________________________________________________________________________
## Development setup

Install OpenJDK 21
Minimum required Java version is 21:

    sudo apt install openjdk-21-jdk

Install Eclipse IDE Java EE:
    
    https://www.eclipse.org/downloads/

Clone this project and open the folder in Eclipse.

_______________________________________________________________________________
## Required initializations

**Install Lombok in Eclipse**

Right mouse click on the project folder and click on "Gradle"-> "Refresh Gradle Project".

Install Lombok:
	Find the full path lombok-X.X.X.jar in "Project and External Dependencies" and run it like here below:
	
	java -jar $HOME/.gradle/caches/modules-2/files-2.1/org.projectlombok/lombok/1.18.30/f195ee86e6c896ea47a1d39defbe20eb59cd149d/lombok-1.18.30.jar

	Then follow the instructions when a new "Project Lombok Installer" Window appears.
	
Restart Exclipse.

Right mouse click on project folder and click on "Gradle"-> "Refresh Gradle Project".

_______________________________________________________________________________
## Build

**Run the following to build the project for stand alone application:**

    ./gradlew clean
    ./gradlew bootJar

This will create the Jar file:
    
    "build/libs/customer-service-X.X.X-SNAPSHOT.jar"

Run jar file:

    java -jar build/libs/customer-service-X.X.X-SNAPSHOT.jar
    
Run jar file with a custom port:

    SERVER_PORT={ANY_PORT} java -jar build/libs/customer-service-X.X.X-SNAPSHOT.jar
    
_______________________________________________________________________________
## JavaDoc

	$ ./gradlew javadoc

Open file build/docs/javadoc/index.html
_______________________________________________________________________________
## Swagger UI

After successfully running the application, the link below can be accessed.

http://localhost:6000/swagger-ui/index.html

