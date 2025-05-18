# Notifier API #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

This application is in charge of notification hub.

### How do I get set up? ###

* You need to set in your system:
    * docker
    * java 21+

### How do I run it locally with Docker? ###

* _mvn clean package -DskipTests=true docker:build_
* _docker run -d -p 8080:8080 notifier_

### How to use the API ###

* Go to swagger(http://localhost:8080/swagger-ui/index.html)

### Who do I talk to? ###

* delmoralcristian@gmail.com