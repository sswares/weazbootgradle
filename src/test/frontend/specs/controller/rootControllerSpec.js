"use strict";

describe("EulaController", function () {
    var subject = require('../../../../main/frontend/js/controllers/rootController.js');
    var $scope = {};

    beforeEach(function () {
        subject($scope)
    });

    it('sets a name on the scope', function () {
        expect($scope.name).toEqual('weazboot gradle');
    });
});