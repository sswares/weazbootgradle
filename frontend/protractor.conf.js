'use strict';

exports.config = {
	allScriptsTimeout: 21000,

	specs: [
		'test/e2e/**/*-spec.js'
	],

	baseUrl: 'http://localhost:9000/',

	seleniumPort: 4444,
	seleniumArgs: ['-browserTimeout=60'],

	troubleshoot: false,
	directConnect: false,

	capabilities: {
		'browserName': 'chrome',
		'chromeOptions': {
			'args': ['show-fps-counter=true']
		}
	},

	framework: 'jasmine2',

	onPrepare: function () {
		var Jasmine2HtmlReporter = require('protractor-jasmine2-html-reporter');
		var ScreenShotReporter = require('protractor-screenshot-reporter');
		jasmine.getEnv().addReporter(
			new Jasmine2HtmlReporter({
				savePath: './server-main/build/test-results/e2e-html/',
				screenshotsFolder: 'images'
			})
		);
		jasmine.getEnv().addReporter(
			new ScreenShotReporter({
				baseDirectory: './server-main/build/test-results/e2e-html/screenshots'
			})
		);
	},

	jasmineNodeOpts: {
		showColors: true,
		defaultTimeoutInterval: 30000
	}
};