'use strict';

module.exports = {
    goto: function () {
        browser.ignoreSynchronization = true;
        browser.get('/');
    },
    getHeader: function () {
        return element(by.css('h1')).getText();
    },
    getGreeting: function () {
        return element(by.css('h4')).getText();
    },
    getNotLoggedInText: function () {
        return element(by.css('span#not-logged-in')).getText();
    },
    getLoggedInText: function () {
        return element(by.css('span#logged-in-message')).getText();
    },
    getResourceText: function () {
        return element(by.css('span#resource')).getText();
    },
    clickLoginLink: function () {
        return element(by.css('a[href="/login"]')).click();
    },
    clickResourceLink: function () {
        return element(by.id('resource-link')).click();
    },
    logout: function () {
        browser.get('/');
        element(by.id('logout-link')).click();
    }
};