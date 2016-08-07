"use strict";

module.exports = function ($scope, $rootScope, $http, $location) {
    $scope.name = "weazboot gradle";

    $http.get('/user').then(function (response) {
        if (response.data.enabled) {
            $rootScope.authenticated = true;
            $rootScope.username = response.data.username;
            $rootScope.favoriteCat = response.data.favoriteCat;
        }
    }, function () {
        $rootScope.authenticated = false;
        $rootScope.username = undefined;
    });

    $scope.logout = function () {
        $http.post('logout', {}).finally(function () {
            $rootScope.authenticated = false;
            $location.path("/");
        });
    };
};