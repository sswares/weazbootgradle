exports.config = {
    allScriptsTimeout: 11000,

    specs: [
        '../end2end/**/*.js'
    ],

    capabilities: {
        'browserName': 'PhantomJS'
    },

    baseUrl: 'http://localhost:9001/#/',

    framework: 'jasmine',

    jasmineNodeOpts: {
        showColors: true,
        defaultTimeoutInterval: 30000
    }
};
