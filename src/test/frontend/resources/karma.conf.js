module.exports = function (config) {
    config.set({
        basePath: '../../../../',

        preprocessors: {
            'node_modules/angular/angular.js': ['browserify'],
            'src/main/frontend/js/app.js': ['browserify'],
            'src/main/frontend/js/**/*.js': ['browserify'],
            'src/test/frontend/specs/**/*.js': ['browserify'],
            'src/main/frontend/partials/**/*.html': ['ng-html2js']
        },

        ngHtml2JsPreprocessor: {
            stripPrefix: 'src/main/frontend/',
            moduleName: 'partials'
        },

        // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
        logLevel: config.LOG_INFO,

        files: [
            {pattern: 'src/main/frontend/assets/**/*', watched: false, included: false, served: true},
            'node_modules/angular/angular.js',
            'src/main/frontend/js/app.js',
            'src/main/frontend/js/**/*.js',
            'src/test/frontend/specs/**/*.js',
            'src/main/frontend/partials/**/*.html'
        ],

        autoWatch: false,

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
            outputDir: 'build/test-results/',
            outputFile: 'karma-unit.xml',
            suite: 'unit'
        }
    });
};