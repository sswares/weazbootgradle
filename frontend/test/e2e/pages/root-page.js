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
		return element(by.id('not-logged-in')).getText();
	},
	getLoggedInText: function () {
		return element(by.id('logged-in-message')).getText();
	},
	getResourceText: function () {
		return element(by.id('resource-message')).getText();
	},
	clickLoginLink: function () {
		return element(by.css('a[href="/login"]')).click();
	},
	clickResourceLink: function () {
		return element(by.id('resource-link')).click();
	}
};