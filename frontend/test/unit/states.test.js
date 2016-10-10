'use strict';

require('angular-mocks');

describe('states', function () {
	var subject = require('states');

	var $stateProvider;
	var $urlRouterProvider;
	var $httpProvider;
	var $state;

	beforeEach(angular.mock.module('app'));

	beforeEach(inject(function ($injector) {
		$state = $injector.get('$state');

		$stateProvider = jasmine.createSpyObj('$stateProvider', ['state']);
		$stateProvider.state.and.returnValue($stateProvider);

		$urlRouterProvider = jasmine.createSpyObj('$urlRouterProvider', ['otherwise']);
		$urlRouterProvider.otherwise.and.returnValue($urlRouterProvider);

		$httpProvider = {
			defaults: {
				headers: {
					common: []
				}
			}
		};

		subject($stateProvider, $urlRouterProvider, $httpProvider);
	}));

	it('sets the X-Requested-With header correctly', function () {
		expect($httpProvider.defaults.headers.common['X-Requested-With']).toEqual('XMLHttpRequest');
	});

	it('sets the default route to /', function () {
		expect($urlRouterProvider.otherwise).toHaveBeenCalledWith('/');
	});

	describe('the root state', function () {
		var rootState;

		beforeEach(function () {
			rootState = $state.get('root');
		});

		it('has the correct url', function () {
			expect(rootState.url).toEqual('/');
		});

		it('has the correct template url', function () {
			expect(rootState.views['@'].templateUrl).toEqual('partials/root.html');
		});
	});

	describe('the resource state', function () {
		var rootState;

		beforeEach(function () {
			rootState = $state.get('resource');
		});

		it('has the correct url', function () {
			expect(rootState.url).toEqual('/resource');
		});

		it('has the correct template url', function () {
			expect(rootState.views['@'].templateUrl).toEqual('partials/resource.html');
		});
	});
});