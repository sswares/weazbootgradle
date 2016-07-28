'use strict';
require('angular/angular');
require('angular-ui-router');

window.jQuery = require('jquery');

var app = angular.module('app', [
    'ui.router'
]);

var states = require('./states');

app.config(states).run(function ($rootScope, $location, $window, $http) {
    $rootScope.$on('$stateChangeStart', function (event) {
        var tokenX = $location.search().tokenX;

        if (tokenX !== undefined) {
            $http.get('/user', {
                    headers: {
                        Authorization: 'Bearer ' + tokenX
                    }
                }
            ).then(function (response) {
                if (response.data.name) {
                    $rootScope.authenticated = true;
                    $rootScope.username = response.data.name;
                }
                event.preventDefault();
            }, function () {
                $rootScope.authenticated = false;
                $rootScope.username = undefined;
            }).finally(function () {
                var redirectionUrl = $location.protocol() +
                    '://' +
                    $location.host() +
                    ':' +
                    $location.port() +
                    '/#' +
                    $location.path();

                console.log("redirection path was", redirectionUrl);
                $window.location.href = redirectionUrl;
            });
        }
        // event.preventDefault();
    });
});

module.exports = app;
