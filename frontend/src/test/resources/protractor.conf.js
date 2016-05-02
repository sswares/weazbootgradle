'use strict';

exports.config = {
    allScriptsTimeout: 21000,

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
        var ScreenShotReporter = require('protractor-screenshot-reporter');
        jasmine.getEnv().addReporter(
            new Jasmine2HtmlReporter({
                savePath: './server-ui/build/test-results/end2end-html/',
                screenshotsFolder: 'images'
            })
        );
        jasmine.getEnv().addReporter(
            new ScreenShotReporter({
                baseDirectory: './server-ui/build/test-results/end2end-html/screenshots'
            })
        );
    },

    jasmineNodeOpts: {
        showColors: true,
        defaultTimeoutInterval: 30000
    }
};