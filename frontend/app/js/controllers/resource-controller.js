'use strict';

module.exports = function ($scope, $http) {
	$http.get('/api/greeting').then(function (response) {
		$scope.backendResponse = response.data.message;
	});
};