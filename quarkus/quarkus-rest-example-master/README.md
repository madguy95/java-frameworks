# Quarkus RESt API example

## Prerequisites

To complete this guide, you need:

An IDE

JDK 11+ installed with JAVA_HOME configured appropriately

Apache Maven 3.8.8

A working container runtime (Docker or Podman)

This project uses Quarkus to build REST APIs. 

For the tutorials check the below links,
- [Building REST APIs with Quarkus](https://www.geekyhacker.com/2020/06/06/building-rest-apis-with-quarkus/)
- [Use MySQL in Quarkus with Hibernate and Panache](https://www.geekyhacker.com/2020/06/09/use-mysql-in-quarkus-with-hibernate-and-panache/)
- [How to add Swagger to Quarkus](https://www.geekyhacker.com/2020/06/12/how-to-add-swagger-to-quarkus/)
- [Secure REST APIs in Quarkus using Basic Auth](https://www.geekyhacker.com/2020/06/17/secure-rest-apis-in-quarkus-using-basic-auth/)

## Running the application in `dev` mode

First start the docker:

```bash
$ cd docker && docker-compose up -d
```

Then create tables with some predefined data,

```bash
$ ./db_initializer.sh
```

Finally, you can run your application in dev mode that enables live coding using:

```bash
$ ./mvnw quarkus:dev
```

## Interacting with APIs

The app runs on `localhost:8080`. You can interact with the APIs via Swagger `localhost:8080/swagger-ui`

Alternatively you can use CURL as follows,

```bash
# get list of users (secured, accessible to users with 'ADMIN' or 'USER' role)
$ curl --anyauth --user leo:1234 localhost:8080/v1/users/

# get a specific user (secured, accessible to users with 'ADMIN' or 'USER' role)
$ curl --anyauth --user leo:1234 localhost:8080/v1/users/2

# create a user (open)
$ curl --request POST 'localhost:8080/v1/users' --header 'Content-Type: application/json' \
--data-raw '{
	"firstName": "Tom",
	"lastName": "Cruise",
	"age": 57
}'

# edit a user (secured, accessible to users with 'ADMIN' role only)
$ curl --anyauth --user admin:admin --request PUT 'localhost:8080/v1/users/1' --header 'Content-Type: application/json' \
--data-raw '{
	"firstName": "Leonardo",
	"lastName": "DiCaprio",
	"age": 46
}'

# delete a user (secured, accessible to users with 'ADMIN' role only)
$ curl --anyauth --user admin:admin --request DELETE 'localhost:8080/v1/users/2'
```

## Debugging the application

To debug the app, run the following command first,

```bash
$ ./mvnw quarkus:dev -Ddebug
```

This activates debug on port `5005`. Then use your IDE to connect to `localhost:5005` to debug the application.


## build app

To build native file 

```bash
$ mvn package -Pnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true -DskipTests
```

-Dquarkus.native.container-build=true -Dquarkus.container-image.build=true : Setting to build in container without graalvm installed local

To build image and run with docker

```bash
$ docker build -f src/main/docker/Dockerfile.native -t quarkus/quarkus-rest-example .
$ docker run -i --rm -p 8080:8080 quarkus/quarkus-rest-example
```

To build with JIB

application.properties

```txt
quarkus.container-image.build=true  
quarkus.container-image.group=htl-leonding
quarkus.container-image.name=vehicle
quarkus.container-image.tag=latest
quarkus.jib.ports=8080
```

quarkus.container-image.build=true  - property is mandatory for building the docker image


```bash
$ mvn quarkus:add-extension -Dextensions=container-image-jib
$ mvn clean package -DskipTests
```

## Refer

https://htl-leonding-college.github.io/quarkus-docker-gh-actions-demo/#_using_jib

https://quarkus.io/guides/container-image#docker