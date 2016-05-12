"use strict";

module.exports = function ($scope, $rootScope, $http, $location) {
    $scope.name = "weazboot gradle";

    $http.get('/user').then(function (response) {
        if (response.data.name) {
            $rootScope.authenticated = true;
            $rootScope.username = response.data.name;
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