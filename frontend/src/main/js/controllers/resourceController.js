"use strict";

module.exports = function ($scope, $http) {
    $http.get('/api/hello-world').then(function (response) {
        $scope.backendResponse = response.data.message;
    }, function (response) {
        $scope.backendResponse = response.data.message;
    });
};