'use strict';

describe('Application', function () {
    it('should load the page', function () {
        browser.get('/');

        var header = element(by.css('h1')).getText();
        expect(header).toContain('weazboot gradle');

        var content = element(by.css('span#resource')).getText();
        expect(content).toContain('I wonder if hitting a resource might work?');
    });

    describe('clicking the resource link', function () {
        beforeAll(function () {
            element(by.id('resource-link')).click();
        });

        it('takes me to the resource page', function () {
            expect(browser.getCurrentUrl()).toContain("resource");

            var content = element(by.css('span#backend-response')).getText();
            expect(content).toContain('We should have made a request to the backend. Lets see if they wanted to tell us anything: Hello World!');
        });

        it('can take me back to the index', function () {
            element(by.id("go-back-link")).click();
            browser.getCurrentUrl().then(function (newUrl) {
                expect(newUrl.substr(-2)).toEqual("#/");
            });
        });
    });
});