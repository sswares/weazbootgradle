'use strict';
require('angular/angular');
require('angular-ui-router');

window.jQuery = require('jquery');

var app = angular.module('app', [
    'ui.router'
]);

var states = require('./states');

app.config(states);

module.exports = app;
