'use strict';

exports.config = {
    allScriptsTimeout: 11000,

    specs: [
        '../end2end/**/*.js'
    ],

    baseUrl: 'http://localhost:9001/#/',

    seleniumPort: 4444,
    seleniumArgs: ['-browserTimeout=60'],

    troubleshoot: false,

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
            savePath: './build/test-results',
            filePrefix: 'end2end-protractor'
        }));
    },

    jasmineNodeOpts: {
        showColors: true,
        defaultTimeoutInterval: 30000
    }
};