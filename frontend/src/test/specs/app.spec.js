'use strict';

describe('app', function () {
    var subject = require('app');

    it('requires ui.router', function () {
        expect(subject.requires).toContain('ui.router');
        expect(false).toBeFalsy();
    });
});