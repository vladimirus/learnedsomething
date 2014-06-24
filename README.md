learnedsomething
===

Learned Something: reddit to facebook plugin

===

[![Build Status](http://adapter.afterburna.com/jenkins/job/learnedsomething/badge/icon)](http://adapter.afterburna.com/jenkins/job/learnedsomething/)

### To build:

Prerequisites:

    * Java 7 or newer
    * Maven 3.0.4 or newer
    * MongoDb 2.4 or newer
    * Tomcat 7 or newer

Install:

    Define the follow in your Tomcat script:
    -Denvfile="file:/var/server/environment.properties"

    mvn clean install
    cp target/*.war into your Tomcat's webapps directory
    start your tomcat

### To run the tests:

    mvn test
