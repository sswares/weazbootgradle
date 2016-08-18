# Weazbootgradle

CI | Status
 --- | ---
| Travis  |  [![Build Status](https://travis-ci.org/themadweaz/weazbootgradle.svg?branch=master)](https://travis-ci.org/themadweaz/weazbootgradle) |
| Jenkins | [![Build Status](http://jenkins.weaz.net/buildStatus/icon?job=weazboot)](http://jenkins.weaz.net/job/weazboot/) |

##Introduction
**Weazboot Gradle** is a starter **[Spring Boot](http://projects.spring.io/spring-boot/)** / **[AngularJS](https://angularjs.org/)** project with many features 
designed to be used with **[Intellij IDE](https://www.jetbrains.com/idea/)**.  
It uses **[Gradle](https://gradle.org/)** as the main build tool with **[Grunt](http://gruntjs.com/)** building the javascript artifacts.  It is cross platform, 
**[travis-ci](https://travis-ci.org/)** and **[jenkins](https://jenkins.io/)** friendly, and loaded with multiple testing frameworks.  It comes pre-configured with 
**[Intellij IDE](https://www.jetbrains.com/idea/)** configuration settings so that you can get started quickly.

The entire build lifecycle for this project can be run in a single: ``./gradlew build`` command.

--
##Mission Statement
The goal is to be able to be able to pull the sample project from **[Github](https://github.com)** into **[Intellij IDE](https://www.jetbrains.com/idea/)**
on windows/mac/linux, change the project name to whatever it is you need to work on, and have a skeleton **[AngularJS](https://angularjs.org/)** / **[Spring Boot]
(http://projects.spring.io/spring-boot/)** app with sane defaults/test suites/run configurations/dependencies so you can get started driving out your web app quickly.  It should
 be able to run on ci, dev, and prod without changing anything about the build.  That's the plan, at least.
 
##Why?
Bootstrapping apps takes time, and you generally cut corners to get it working quickly.  Having done this **n+1** times already, it makes sense to try to abstract is.  Moreover,
 building good test suites is hard and integrating them into the build lifecycle is even more difficult.  I'm pretty good at it, so this is my shot at doing it on this stack for
  the last time.  I also want to be able to utilize the tools in my IDE, so all tools should be configured so that they can be run using standalone ide tools.  That is important.

--
##Setup

In your workspace directory:
```Shell
git clone https://github.com/themadweaz/weazbootgradle.git
```

Then open the project directly in **[Intellij IDE](https://www.jetbrains.com/idea/)**.  Do not Import, open.
Import would work, but you would lose many of the **[Intellij IDE](https://www.jetbrains.com/idea/)** specific configuration features of this sample.
 
Once that is done, either run the `./gradlew build` from the command line or the `build` *Run Configuration* in  **[Intellij IDE](https://www.jetbrains.com/idea/)**.
This will pull down all dependencies, install **[Node.js](https://nodejs.org)** and **[npm](https://www.npmjs.com/)**, and run the test suite.

###Optional
- Load pre-configured settings for **[Intellij IDE](https://www.jetbrains.com/idea/)** by importing the settings jar in `$PROJECT_ROOT/config/intellij/settings-*.jar`
using `File->Import Settings...`.  Use at your own risk, as this will overwrite your own **[Intellij IDE](https://www.jetbrains.com/idea/)** settings.

--
##Tooling
###Java
####Spring Boot
The project comes with a pretty sane **[Spring Boot](http://projects.spring.io/spring-boot/)** multi-module setup.  It contains a main application that already has an _index.html_ 
configured with _app.js_ built from the attached **[AngularJS](https://angularjs.org/)** sources, and an auth server which handles login.  Both are configured to use a proxy to 
make the two apps work seamlessly together.

####Spring Security (OAuth2)
Instead of the default basic auth setup, this project comes with a pre-configured **[OAuth2](http://oauth.net/2/)** implementation.  
It follows the example by Dave Syer from [this github](https://github.com/spring-guides/tut-spring-boot-oauth2) with lots of modifications.
The default username/password is: `user/password`

####Testing
The build has some additional testing tasks for designed launching **[jUnit](http://junit.org)** category annotated test suites, but generally just uses jUnit and the built in 
test support from **[Spring Boot](http://projects.spring.io/spring-boot/)** on the backend.  

####Devtools
Pre-configured with **[Spring Developer Tools](http://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-devtools.html)** for hot-swapping classes and resources.

####Checkstyle
The build runs **[Checkstyle](http://checkstyle.sourceforge.net/)** on the code base as part of the build lifecycle.  It is pre-configured with a modified version
of [Google's checkstyle configuration](https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml).  The **[Intellij IDE](https://www.jetbrains.com/idea/)** project is configured to be able to run this checkstyle variant
from the Checkstyle **[Intellij IDE](https://www.jetbrains.com/idea/)** plugin.  The project contains a Code Style XML configuration that matches the style rules in the 
checkstyle configurations.  Can be easily modified as needed.

--
###Javascript
####Node / NPM Install
Using a few **[Gradle](https://gradle.org/)** plugins, the first ```./gradlew build``` run will install **[Node.js](https://nodejs.org)** and pull down
any **[npm](https://www.npmjs.com/)** dependencies defined in _Package.json_.  If you already have **[Node.js](https://nodejs.org)** installed, it will use your local version.

####Grunt
Asset generation with **[Grunt](http://gruntjs.com/)**.  Very opinionated about how the project is setup, but should be easily reconfigured if you choose a different set of 
frontend tools.

####Testing
The sample project comes pre-configured with test suites for most types of angular testing:
* Javascript unit test suite powered by **[Karma](https://karma-runner.github.io)** using **[PhantomJS](http://phantomjs.org/)**.  
* **[Protractor.js](http://www.protractortest.org)** end-to-end test suite for running integration type tests.  When run through **[Grunt](http://gruntjs.com/)** 
(or **[Gradle](https://gradle.org/)**), it stands up the **[Spring Boot](http://projects.spring.io/spring-boot/)** jar files to run tests against.  When run via IDE run 
configurations, can be run against applications currently running in regular or debug mode for extra flexibility.

--
##Contributing:
I will happily take pull requests.  Please open an issue first!

--
##TODO:
* Single sign-out
* Finish saml branch
* Better setup / documentation
* Most things, really.

--
##Known Issues / Workarounds:
###Karma Plugin
The Karma plugin in **[Intellij IDE](https://www.jetbrains.com/idea/)** is buggy with **[karma-browserify](https://github.com/nikku/karma-browserify)**.  
It will fail to reload tests. There is a workaround on this [bug report](https://youtrack.jetbrains.com/issue/WEB-12496).

###Intellij / Devtools
To get devtools working correctly on class changes, you need to do a few things.  First, you will need to go to `Settings -> Compiler` and enable the option `Make Project Automatically`.  Next, you will need to enable the `compiler.automake.allow.when.app.running` registry setting in **[Intellij IDE](https://www.jetbrains.com/idea/)**.  
You can access the registry in by using the shortcut `Shift + Command + A`, then searching for `registry`.  Set this value to `true`.
