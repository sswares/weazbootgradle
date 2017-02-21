![Angular](./docs/images/angular-logo.png) ![Spring Boot](./docs/images/spring-logo.png) ![OAuth2](./docs/images/oauth2-logo.png)
# Weazbootgradle
Tool | Status
 --- | ---
| Travis  |  [![Build Status](https://travis-ci.org/themadweaz/weazbootgradle.svg?branch=master)](https://travis-ci.org/themadweaz/weazbootgradle) |
| Jenkins | [![Build Status](http://jenkins.weaz.net/buildStatus/icon?job=Weazboot/master)](http://jenkins.weaz.net/job/weazboot/) |
| Java | [![Coverage Status](https://coveralls.io/repos/github/themadweaz/weazbootgradle/badge.svg)](https://coveralls.io/github/themadweaz/weazbootgradle) |

## Introduction
**Weazboot Gradle** is a starter **[Spring Boot](http://projects.spring.io/spring-boot/)** / **[Angular](https://angular.io/)** project with many features 
designed to be used with **[Intellij IDE](https://www.jetbrains.com/idea/)**.  
It uses **[Gradle](https://gradle.org/)** as the main build tool with **[Angular CLI](https://cli.angular.io/)** for the frontend.  It is cross platform, 
**[travis-ci](https://travis-ci.org/)** and **[Jenkins](https://jenkins.io/)** friendly, and loaded with multiple testing frameworks.  It comes pre-configured with 
**[Intellij IDE](https://www.jetbrains.com/idea/)** configuration settings so that you can get started quickly.

The entire build lifecycle for this project can be run in a single: ``./gradlew build`` command.


## Goal
To build Angular application fully secured by [OAuth2](https://tools.ietf.org/html/rfc6749) authorization code flow, with an example OAuth2 authentication server.  The 
authentication server could be used in an organization, or replaced by an existing service (Google, Facebook, Okta, etc).
 or 
as a 
 
## Why?
Bootstrapping apps takes time, and you generally cut corners to get it working quickly.  Having done this **n+1** times already, it makes sense to try to abstract is.

## Setup

In your workspace directory:
```Shell
git clone https://github.com/themadweaz/weazbootgradle.git
```

Then open the project directly in **[Intellij IDE](https://www.jetbrains.com/idea/)**.  *Do not Import*, **Open**.
Import would work, but you would lose many of the **[Intellij IDE](https://www.jetbrains.com/idea/)** specific configuration features of this sample.
 
Once that is done, either run the `./gradlew build` from the command line or the `build` *Run Configuration* in  **[Intellij IDE](https://www.jetbrains.com/idea/)**.
This will pull down all dependencies, install **[Node.js](https://nodejs.org)** and **[npm](https://www.npmjs.com/)**, and run the test suite.

See each module's README.md for more information about commands available.

#### Optional
- Load pre-configured settings for **[Intellij IDE](https://www.jetbrains.com/idea/)** by importing the settings jar in `$PROJECT_ROOT/config/intellij/settings-*.jar`
using `File->Import Settings...`.  Use at your own risk, as this will overwrite your own **[Intellij IDE](https://www.jetbrains.com/idea/)** settings.

##Tooling
###Java
####Spring Boot
The project comes with a pretty sane **[Spring Boot](http://projects.spring.io/spring-boot/)** multi-module setup.  It contains a main application that serves the Angular app,
and an authorization server example app.  It is designed to be extended, and has classes named **Custom** which are targets for enhancement.

####Spring Security (OAuth2)
Instead of the default basic auth setup, this project comes with a pre-configured **[OAuth2](http://oauth.net/2/)** implementation.  
It follows the example by Dave Syer from [this github](https://github.com/spring-guides/tut-spring-boot-oauth2) with lots of modifications.

The default username/password is: `user/password`

####Testing
Everything is tested using the most modern **[Spring Boot](http://projects.spring.io/spring-boot/)** patterns.  It has [jacoco](http://www.eclemma.org/jacoco/) coverage tooling
to monitor how well the application is covered.  Also included is an example integration with [Coveralls](https://coveralls.io/) to record coverage over time.

####Devtools
Pre-configured with **[Spring Developer Tools](http://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-devtools.html)** for hot-swapping classes and resources.

####Checkstyle
The build runs **[Checkstyle](http://checkstyle.sourceforge.net/)** on the code base as part of the build lifecycle.  It is pre-configured with a modified version
of [Google's checkstyle configuration](https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml).
The **[Intellij IDE](https://www.jetbrains.com/idea/)** project is configured to be able to run this checkstyle variant
from the Checkstyle **[Intellij IDE](https://www.jetbrains.com/idea/)** plugin.  The project contains a Code Style XML configuration that matches the style rules in the 
checkstyle configurations.  Can be easily modified as needed.

--
### Javascript
#### Node / NPM Install
Using a few **[Gradle](https://gradle.org/)** plugins, the first ``./gradlew build`` run will install **[Node.js](https://nodejs.org)** and pull down
any **[npm](https://www.npmjs.com/)** dependencies defined in _Package.json_.  If you already have **[Node.js](https://nodejs.org)** installed, it will use your local version.

####Grunt
We use **[Grunt](http://gruntjs.com/)** to help stand up the jars and then run  ``ng test`` from the **[Angular CLI](https://cli.angular.io/)**.  

####Testing
**[Angular CLI](https://cli.angular.io/)** includes tools for running both unit and end2end types of testing.  More information can be found on in the [frontend README](
./frontend/README.md).


##Contributing:
I will happily take pull requests.  Please open an issue first!

##TODO:
* Single sign-out
* Finish saml branch
* Better setup / documentation

--
##Known Issues / Workarounds:

###Intellij / Devtools
To get devtools working correctly on class changes, you need to do a few things.  First, you will need to go to `Settings -> Compiler` and enable the option `Make Project Automatically`.  Next, you will need to enable the `compiler.automake.allow.when.app.running` registry setting in **[Intellij IDE](https://www.jetbrains.com/idea/)**.  
You can access the registry in by using the shortcut `Shift + Command + A`, then searching for `registry`.  Set this value to `true`.
