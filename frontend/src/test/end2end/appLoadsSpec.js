'use strict';

describe('Application', function () {
    it('should load the page', function () {
        browser.get('/');

        var content = element(by.css('body')).getText();

        expect(content).toContain('weazboot gradle');
    });
});