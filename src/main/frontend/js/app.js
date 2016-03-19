'use strict';
require('angular/angular');
require('angular-ui-router');
require('templates');

window.jQuery = require('jquery');

var states = require('./states.js');

var app = angular.module('app', ['templates', 'ui.router']);

app.config(states);

module.exports = app;