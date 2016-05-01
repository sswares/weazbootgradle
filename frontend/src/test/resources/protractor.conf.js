'use strict';

exports.config = {
    allScriptsTimeout: 11000,

    specs: [
        '../end2end/**/*.js'
    ],

    baseUrl: 'http://localhost:9000/',

    seleniumPort: 4444,
    seleniumArgs: ['-browserTimeout=60'],

    troubleshoot: false,

    capabilities: {
        'browserName': 'chrome',
        'chromeOptions': {
            'args': ['show-fps-counter=true']
        }
    },

    framework: 'jasmine2',

    onPrepare: function () {
        var Jasmine2HtmlReporter = require('protractor-jasmine2-html-reporter');
        jasmine.getEnv().addReporter(
            new Jasmine2HtmlReporter({
                savePath: '../server-ui/build/test-results/end2end-html/',
                screenshotsFolder: 'images'
            })
        );
    },

    jasmineNodeOpts: {
        showColors: true,
        defaultTimeoutInterval: 30000
    }
};