'use strict';

var rootPage = require('./pages/rootPage');
var loginPage = require('./pages/loginPage');
var approvalPage = require('./pages/approvalPage');
var resourcePage = require('./pages/resourcePage');

describe('JourneySpec', function () {

    describe('when not logged in', function () {
        beforeEach(function () {
            rootPage.goto();
        });

        it('should load the page', function () {
            expect(rootPage.getHeader()).toContain('weazboot gradle');
            expect(rootPage.getGreeting()).toContain('Glad you could make it!');
        });

        it('has a login link', function () {
            expect(rootPage.getNotLoggedInText()).toContain('Click here to login');
        });

        describe('clicking the login link', function () {
            beforeEach(function () {
                browser.ignoreSynchronization = true;
                rootPage.clickLoginLink();
            });

            afterEach(function () {
                browser.ignoreSynchronization = false;
            });

            it('takes you to the login page', function () {
                browser.getCurrentUrl().then(function (newUrl) {
                    expect(newUrl.substr(-11)).toEqual("/auth/login");
                });
            });
        });
    });

    describe('when logged in', function () {
        beforeAll(function () {
            browser.ignoreSynchronization = true;
            browser.manage().timeouts().pageLoadTimeout(40000);
            browser.manage().timeouts().implicitlyWait(25000);

            loginPage.goto();
            loginPage.setUsername("user");
            loginPage.setPassword("password");
            loginPage.submitLoginForm();
            browser.waitForAngular();
            browser.ignoreSynchronization = false;
        });

        it('says you are logged in', function () {
            expect(rootPage.getLoggedInText()).toContain("Welcome, user!\n\nWe heard that your favorite cat was Tippy! That's fantastic!");
        });

        it('has a link to the resource page', function () {
            expect(rootPage.getResourceText()).toContain('I wonder if hitting a resource might work?');
        });

        describe('clicking the resource link', function () {
            beforeAll(function () {
                rootPage.clickResourceLink();
            });

            it('takes you to the resource page', function () {
                browser.getCurrentUrl().then(function (newUrl) {
                    expect(newUrl.substr(-9)).toEqual("/resource");
                });
            });

            it('displays a message from the resource server', function () {
                expect(resourcePage.getBackendResponse().getText()).toEqual(
                    'We should have made a request to the backend. ' +
                    'Lets see if they wanted to tell us anything: Hello user! Your favorite cat was Tippy, right?');
            });

            it('has a go back link', function () {
                expect(resourcePage.getGoBackLink()).not.toEqual(undefined);
            });

            describe('clicking the go back link', function () {
                beforeAll(function () {
                    resourcePage.getGoBackLink().click();
                });

                it('takes you back to the root page', function () {
                    browser.getCurrentUrl().then(function (newUrl) {
                        expect(newUrl.substr(-3)).toEqual("/#/");
                    });
                });
            });
        });
    });
});