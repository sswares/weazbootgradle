'use strict';
require('angular/angular');
require('angular-ui-router');

window.jQuery = require('jquery');

var app = angular.module('app', [
    'ui.router'
]);

app.config(require('./states'));

module.exports = app;