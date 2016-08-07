'use strict';

module.exports = function (config) {
    config.set({
        basePath: '../../../',

        preprocessors: {
            'src/main/js/**/*.js': ['browserify'],
            'src/test/specs/**/*.js': ['browserify'],
            'src/main/partials/**/*.html': ['ng-html2js']
        },

        ngHtml2JsPreprocessor: {
            stripPrefix: 'src/main/frontend/',
            moduleName: 'partials'
        },

        browserify: {
            paths: ['src/main/js'],
            debug: true
        },

        logLevel: config.LOG_INFO,

        files: [
            {pattern: 'src/main/assets/**/*', watched: false, included: false, served: true},
            'src/main/js/**/*.js',
            'src/test/specs/**/*.js',
            'src/main/partials/**/*.html'
        ],

        autoWatch: true,
        singleRun: false,

        autoWatchBatchDelay: 100,

        frameworks: ['browserify', 'jasmine'],

        browsers: ['PhantomJS'],

        browserNoActivityTimeout: 30000,

        plugins: [
            'karma-junit-reporter',
            'karma-phantomjs-launcher',
            'karma-jasmine',
            'karma-ng-html2js-preprocessor',
            'karma-browserify'
        ],

        reporters: ['junit', 'dots'],

        junitReporter: {
            outputDir: '../server-main/build/test-results/',
            outputFile: 'karma-unit.xml',
            suite: 'unit'
        }
    });
};