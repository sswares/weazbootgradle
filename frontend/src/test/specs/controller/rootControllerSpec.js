"use strict";
require('angular-mocks');

describe("RootController", function () {
    var subject = require('controllers/rootController');
    var $scope = {};
    var $rootScope = {};
    var $httpBackend, $http;

    beforeEach(inject(function ($injector) {
        $http = $injector.get('$http');
        $httpBackend = $injector.get('$httpBackend');
        
        subject($scope, $rootScope, $http);
    }));

    it('sets a name on the scope', function () {
        expect($scope.name).toEqual('weazboot gradle');
    });

    describe('making a request to the user endpoint', function () {

        afterEach(function () {
            $httpBackend.verifyNoOutstandingExpectation();
            $httpBackend.verifyNoOutstandingRequest();
        });

        describe('when it responds successfully with a user name', function () {
            it('sets authenticated to true on the rootScope', function () {
                $httpBackend.expectGET('/user').respond(200, {name: "user"});
                $httpBackend.flush();
                expect($rootScope.authenticated).toEqual(true);
                expect($rootScope.username).toEqual("user");
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
});