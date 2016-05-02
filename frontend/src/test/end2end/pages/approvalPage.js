'use strict';

var loginPage = require('./loginPage');

module.exports = {
    goto: function (username, password) {
        loginPage.goto();
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.submitLoginForm();
    },
    clickApprove: function () {
        element(by.buttonText("Approve")).click();
    }
};