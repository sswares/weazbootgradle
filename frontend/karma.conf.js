'use strict';

module.exports = function (config) {
	config.set({
		basePath: './',

		preprocessors: {
			'app/js/**/*.js': ['browserify'],
			'test/unit/**/*.js': ['browserify'],
			'app/partials/**/*.html': ['ng-html2js']
		},

		ngHtml2JsPreprocessor: {
			stripPrefix: 'src/main/frontend/',
			moduleName: 'partials'
		},

		browserify: {
			paths: ['app/js'],
			debug: true
		},

		logLevel: config.LOG_INFO,

		files: [
			{pattern: 'app/assets/**/*', watched: false, included: false, served: true},
			'app/js/**/*.js',
			'test/unit/**/*.js',
			'app/partials/**/*.html'
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