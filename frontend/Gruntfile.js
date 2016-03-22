'use strict';

module.exports = function (grunt) {
    var mainPath = './src/main/';
    var testPath = './src/test/';

    var globalConfig = {
        buildDestination: '../build/resources/main/static/',

        mainPath: mainPath,
        jsPath: mainPath + 'js/',
        partialsPath: mainPath + 'partials/',
        lessPath: mainPath + 'less/',
        assetsPath: mainPath + 'assets/',

        specsPath: testPath + 'specs/',
        end2EndPath: testPath + 'end2end/',
        testResourcePath: testPath + 'resources/'
    };

    grunt.initConfig({
        globalConfig: globalConfig,

        browserify: {
            js: {
                src: ['<%= globalConfig.jsPath %>**/*.js'],
                dest: '<%= globalConfig.buildDestination %>js/app.js'
            }
        },

        watch: {
            scripts: {
                files: ['<%= globalConfig.mainPath %>**/*.js'],
                tasks: ['build'],
                options: {
                    spawn: false
                }
            },

            html: {
                files: ['<%= globalConfig.mainPath %>**/*.html'],
                tasks: ['build'],
                options: {
                    spawn: false
                }
            },

            css: {
                files: ['<%= globalConfig.lessPath %>**/*.less'],
                tasks: ['build'],
                options: {
                    spawn: false
                }
            }
        },

        copy: {
            html: {
                expand: true,
                src: ['**/*.html'],
                cwd: '<%= globalConfig.partialsPath %>',
                dest: '<%= globalConfig.buildDestination %>partials'
            },
            css: {
                expand: true,
                src: ['**/*.css'],
                cwd: '<%= globalConfig.lessPath %>',
                dest: '<%= globalConfig.buildDestination %>css'
            },
            assets: {
                expand: true,
                cwd: '<%= globalConfig.assetsPath %>',
                src: ['**/*.jpeg', '**/*.png', '**/*.eot', '**/*.ttf', '**/*.otf', '**/*.woff', '**/*.svg', '**/*.ico'],
                dest: '<%= globalConfig.buildDestination %>assets'
            }
        },

        less: {
            development: {
                options: {
                    paths: ["<%= globalConfig.lessPath %>"]
                },
                files: {
                    "<%= globalConfig.buildDestination %>css/app.css": "<%= globalConfig.lessPath %>app.less"
                }
            }
        },

        karma: {
            unit: {
                configFile: '<%= globalConfig.testResourcePath %>karma.conf.js',
                singleRun: true,
                browsers: ['PhantomJS']
            },
            watch: {
                configFile: '<%= globalConfig.testResourcePath %>karma.conf.js',
                singleRun: false,
                autoWatch: true,
                browsers: ['PhantomJS']
            }
        },

        protractor: {
            development: {
                options: {
                    configFile: '<%= globalConfig.testResourcePath %>protractor.conf.js',
                    args: {} // Target-specific arguments
                }
            },
            build: {
                options: {
                    configFile: '<%= globalConfig.testResourcePath %>protractor.ci.conf.js',
                    capabilities: {
                        'browserName': 'PhantomJS'
                    }
                }
            }
        },

        run: {
            integration_server: {
                cmd: 'java',
                args: [
                    '-jar',
                    '-Dspring.profiles.active=test',
                    '-Dserver.port=9001',
                    grunt.file.expand('../build/libs/*.jar')
                ],
                options: {
                    wait: false,
                    ready: /Tomcat started on port\(s\): 9001 \(http\)/
                }
            }
        },

        jshint: {
            options: {
                reporter: require('jshint-stylish'),
                jshintrc: '.jshintrc'
            },
            all: [
                'Gruntfile.js', '<%= globalConfig.jsPath %>**/*.js',
                '<%= globalConfig.specsPath %>**/*.js',
                '<%= globalConfig.end2EndPath %>**/*.js'
            ]
        }
    });

    grunt.loadNpmTasks('grunt-browserify');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-karma');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-run');
    grunt.loadNpmTasks('grunt-protractor-runner');

    grunt.registerTask('clean', function () {
        grunt.file.delete('./build/resources/main/static');
    });

    grunt.registerTask('build', ['clean', 'browserify', 'less', 'copy']);

    grunt.registerTask('unitTest', ['jshint', 'karma:unit']);

    grunt.registerTask('end2end', ['jshint', 'karma:unit']);

    grunt.registerTask('karmaWatch', ['jshint', 'karma:watch']);

    grunt.registerTask('e2eLocal', [
        'protractor_webdriver:development',
        'protractor:development'
    ]);

    grunt.registerTask('e2eRemote', [
        'build',
        'run:integration_server',
        'protractor:build',
        'stop:integration_server'
    ]);
};