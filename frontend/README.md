# Weazbootgradle - *Frontend*

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 1.0.0-beta.32.3 (for the most part).

...and as such, comes with many nice commands, all of which are defined either by scripts in
[package.json](package.json) or provided by the ``ng`` helper command.  All tooling should work using global npm cli's like *tslint*, *karma-cli*, etc. 


## Development Commands

- ``npm run start-dev`` starts the frontend development server. 
  - This creates a proxy for the **Development Main** and **Development Auth** run configurations, which run on port ``8001`` and ``8002``.

- ``npm run start-test`` starts the frontend test server. 
  - This creates a proxy for the **Test Main** and **Test Auth** run configurations, which run on port ``9001`` and ``9002``.

- ``npm run test`` runs the karma unit tests on auto-watch.
  - You can run it through the [karma-cli](https://karma-runner.github.io) also, if you'd like, by running ``karma start``.

- ``npm run e2e`` runs [protractor](http://www.protractortest.org) end2end tests.

## Gruntjs

[Gruntjs](http://gruntjs.com/) was included so we could use the wonderful [grunt-run]() plugin to launch our .jar files for end2end testing
 via script, for CI in particular.


## TODO
* Additional unit test coverage - *(2/20/17)*
* End2end test coverage - *(2/20/17)*
* Logout - *(2/20/17)*
