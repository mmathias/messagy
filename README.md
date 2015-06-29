README for messagy
==========================

# Local installation (recommended for normal users)
* Install Java from the Oracle website.
* Install Maven (recommended). If you prefer to use Gradle instead, don't install it, as JHipster ships with the Gradle Wrapper.
* Install Git from git-scm.com.
* Install Node.js from the Node.js website. This will also install npm, which is the node package manager we are using in the next commands.
* Install Bower: npm install -g bower
* Install either Grunt (recommended) with npm install -g grunt-cli
* bower install


# Running the application
This is configured to run using H2 database
* mvn spring-boot:run

# Running the application in production mode
This is configured to run using Postgresql database
* mvn -Pprod spring-boot:run

# Running backend tests
* mvn clean test

# Running frontend tests
* grunt test
