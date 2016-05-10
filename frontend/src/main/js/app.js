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

app.config(states).run(function ($rootScope) {

    $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
        console.log(event);
        console.log("toState",toState);
        console.log("toParams",toParams);

        console.log("fromState",fromState);
        console.log("fromParams",fromParams);
    });
});


module.exports = app;
