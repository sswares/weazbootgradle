'use strict';

require('angular-mocks');

describe('RootController', function () {
	var subject = require('controllers/root-controller');
	var $scope = {};
	var $rootScope = {};
	var $httpBackend, $http;
	var $location;

	beforeEach(inject(function ($injector) {
		$http = $injector.get('$http');
		$httpBackend = $injector.get('$httpBackend');

		$location = $injector.get('$location');

		spyOn($location, 'path');

		subject($scope, $rootScope, $http, $location);
	}));

	it('sets a name on the scope', function () {
		expect($scope.name).toEqual('weazboot gradle');
	});

	describe('making a request to the user endpoint', function () {

		afterEach(function () {
			$httpBackend.verifyNoOutstandingExpectation();
			$httpBackend.verifyNoOutstandingRequest();
		});

		describe('when it responds successfully with a user name and enabled', function () {
			describe('when the data says enabled true', function () {
				it('sets authenticated to true on the rootScope', function () {
					$httpBackend.expectGET('/user').respond(200,
						{
							username: 'user',
							enabled: true,
							favoriteCat: 'Girlcat'
						}
					);
					$httpBackend.flush();
					expect($rootScope.authenticated).toEqual(true);
					expect($rootScope.username).toEqual('user');
					expect($rootScope.favoriteCat).toEqual('Girlcat');
				});
			});

			describe('when the data says enabled false', function () {
				it('sets authenticated to true on the rootScope', function () {
					$httpBackend.expectGET('/user').respond(200, {enabled: false});
					$httpBackend.flush();
					expect($rootScope.authenticated).toEqual(false);
					expect($location.path).toHaveBeenCalledWith('/');
				});
			});
		});

		describe('when it responds unsuccessfully', function () {
			it('sets authenticated to false on the rootScope', function () {
				$httpBackend.expectGET('/user').respond(302);
				$httpBackend.flush();

				expect($rootScope.authenticated).toEqual(false);
				expect($rootScope.username).toBeUndefined();
			});
		});
	});

	describe('#logout', function () {

		beforeEach(function () {
			$httpBackend.expectGET('/user').respond(200,
				{
					username: 'user',
					enabled: true,
					favoriteCat: 'Girlcat'
				}
			);
			$httpBackend.flush();
			expect($rootScope.authenticated).toBeTruthy();
		});

		afterEach(function () {
			$httpBackend.verifyNoOutstandingExpectation();
			$httpBackend.verifyNoOutstandingRequest();
		});

		it('posts to the logout function, sets authenticated = false, and redirects', function () {
			$httpBackend.expectPOST('logout').respond(200);

			$scope.logout();
			$httpBackend.flush();

			expect($rootScope.authenticated).toBeFalsy();
			expect($location.path).toHaveBeenCalledWith('/');
		});
	});
});