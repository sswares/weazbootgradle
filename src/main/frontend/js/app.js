'use strict';
require('angular/angular');
require('angular-ui-router');
require('templates');

window.jQuery = require('jquery');

var states = require('./states.js');

var weazBootGradle = angular.module('weazBootGradle', ['templates', 'ui.router']);

weazBootGradle.config(states);

module.exports = weazBootGradle;