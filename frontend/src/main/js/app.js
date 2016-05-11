'use strict';
require('angular/angular');
require('angular-ui-router');

window.jQuery = require('jquery');

var app = angular.module('app', [
    'ui.router'
]);

var states = require('./states');

app.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.interceptors.push('tokenInterceptor');
}]);

app.factory('tokenInterceptor', require('./tokenInterceptor'));

app.config(states).run(function ($rootScope, $location, $window) {
    $rootScope.$on('$viewContentLoaded', function () {
        var tokenX = $location.search().tokenX;
        if (tokenX !== undefined) {
            $window.localStorage.setItem('tokenX', tokenX);
        }
    });
});

module.exports = app;
