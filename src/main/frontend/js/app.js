'use strict';
require('angular/angular');
require('angular-ui-router');

window.jQuery = require('jquery');

var states = require('./states.js');

var app = angular.module('app', ['ui.router']);

app.config(states);

module.exports = app;