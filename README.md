#Weazboot Gradle Edition
Travis: [![Build Status](https://travis-ci.org/themadweaz/weazbootgradle.svg?branch=master)](https://travis-ci.org/themadweaz/weazbootgradle)
Jenkins: [![Build Status](http://jenkins.weaz.net/buildStatus/icon?job=weazboot)](http://jenkins.weaz.net/job/weazboot/)
###Introduction
**Weazboot Gradle** is a starter **Intellij** project designed for easily bootstrapping
**Spring Boot** / **Angular** web applications.  It uses **Gradle** as the main build tool
with **Grunt.js** building the javascript artifacts.

It is not split into separate frontend/backend builds, but instead combined so that
the entire build lifecycle for an application can be run in a single: ``./gradlew build`` command.

It integrates with many popular **Intellij** plugins to run tests, format and check code, debug, right after your
first build.

--
###Goals
The goal is to be able to be able to pull the sample project from _Github_ into **Intellij**
on windows/mac/linux, change the project name to whatever it is you need to work on,
and have a skeleton **Angular** / **Spring Boot** app with sane defaults/test suites/run configurations/dependencies
so you can get started driving out your web app quickly.  It should be able to run on ci, dev, and prod without changing
anything about the build.  That's the plan, at least.
####Why?
Bootstrapping apps takes time, and you generally cut corners to get it working quickly.  Having done this **n+1**
times already, it makes sense to try to abstract is.  Moreover, building good test suites is hard and integrating
them into the build lifecycle is even more difficult.  I'm pretty good at it, so this is my shot at doing it on this stack
for the last time.  I also want to be able to utilize the tools in my IDE, so all tools should be configured
so that they can be run using standalone ide tools.  That is important.


--
###Tooling
####Java
#####Spring Boot
The project comes with a pretty sane **Spring Boot** gradle setup.  It has a lot of _spring-boot-starters_ included
and already has an index.html configured with app.js built from the attached **Angular** sources.  It is similar to
what you would get from the _spring initializer_, but with some tweaks.

#####Testing
The build has some additional testing tasks for launching **jUnit** category annotated test suites.  Also includes
extra test tools.

#####Checkstyle
The build runs check on the code base as part of the build lifecycle.  It is pre-configured with a modified version
of Google's checkstyle configuration.  The **Intellij** project is configured to be able to run this checkstyle variant
from the Checkstyle **Intellij** plugin.  The project contains a Code Style xml configuration that matches the style rules
in the checkstyle configurations.  Can be easily modified as needed.

--
####Javascript
#####Node / NPM Install
Using a few new **Gradle** plugins, the first ```./gradlew build``` run will install **node.js** and pull down
any **npm** dependencies defined in _Package.json_.  If you already have **node** installed, it will use that.

#####Testing
The sample project comes pre-configured with test suites for most types of angular testing:
 * Javascript unit test suite powered by **Karma** using **Phantom.js**, which can be run through
 Intellij's Karma plugin as well.
 * **Protractor.js** end-to-end test suite for running integration type tests.  When run through **Grunt** (or **Gradle**),
 stands up the spring boot uber jars and runs tests against that app.  When run through ide, can run against an app you
 stand up via a pre-configured run configuration.


--
##TODO:
* Most things, really.

--
##Known Issues:
The Karma plugin in **Intellij** is buggy with **karma-browserify**.  It will fail to reload tests.  We recommend using the ``grunt watchTest``
run configuration from Intellij
There is a workaround on the [bug report](https://youtrack.jetbrains.com/issue/WEB-12496).