'use strict';

require('angular-mocks');

describe('ResourceController', function () {
	var subject = require('controllers/resource-controller');
	var $scope = {};
	var $httpBackend, $http;

	beforeEach(inject(function ($injector) {
		$http = $injector.get('$http');
		$httpBackend = $injector.get('$httpBackend');

		subject($scope, $http);
	}));

	afterEach(function () {
		$httpBackend.verifyNoOutstandingExpectation();
		$httpBackend.verifyNoOutstandingRequest();
	});

	it('makes a call to the greeting controller and sets the value on the scope', function () {
		$httpBackend.expectGET('/api/greeting').respond(200, {message: 'Hello World!'});
		$httpBackend.flush();

		expect($scope.backendResponse).toEqual('Hello World!');
	});
});