'use strict';

var rootPage = require('./rootyPage');

module.exports = {
    goto: function () {
        browser.ignoreSynchronization = true;
        rootPage.goto();
        rootPage.clickLoginLink();
    },
    setUsername: function (username) {
        element(by.id("username")).sendKeys(username);
    },
    setPassword: function (password) {
        element(by.id("password")).sendKeys(password);
    },
    submitLoginForm: function () {
        element(by.buttonText("Submit")).click();
    }
};