'use strict';

module.exports = {
	getBackendResponse: function () {
		return element(by.css('span#backend-response'));
	},
	getGoBackLink: function () {
		return element(by.id('go-back-link'));
	}
};