'use strict';

exports.config = {
	allScriptsTimeout: 21000,

	specs: [
		'test/e2e/**/*-spec.js'
	],

	baseUrl: 'http://localhost:9000/',

	seleniumPort: 4444,
	seleniumArgs: ['-browserTimeout=60'],

	troubleshoot: true,
	directConnect: false,

	capabilities: {
		'browserName': 'phantomjs',
		'phantomjs.binary.path': require('phantomjs-prebuilt').path,
		'phantomjs.cli.args': ['--ignore-ssl-errors=true', '--web-security=false']
	},

	framework: 'jasmine',

	onPrepare: function () {
		var jasmineReporters = require('jasmine-reporters');
		jasmine.getEnv().addReporter(new jasmineReporters.JUnitXmlReporter({
			consolidateAll: true,
			savePath: 'build/test-results',
			filePrefix: 'e2e-protractor'
		}));
	},

	jasmineNodeOpts: {
		showColors: true,
		defaultTimeoutInterval: 30000
	}
};