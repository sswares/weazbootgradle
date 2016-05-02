'use strict';

var rootPage = require('./rootPage');

module.exports = {
    getHeaderText: function () {
        return element(by.css('h1')).getText();
    },
    getBackendResponse: function () {
        return element(by.css("span#backend-response"));
    },
    getGoBackLink: function () {
        return element(by.id("go-back-link"));
    }
};