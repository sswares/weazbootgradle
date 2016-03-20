'use strict';

module.exports = function (grunt) {
    var mainPath = './src/main/';
    var testPath = './src/test/';

    var globalConfig = {
        buildDestination: './build/resources/main/static/',

        mainPath: mainPath,
        jsPath: mainPath + 'js/',
        partialsPath: mainPath + 'partials/',
        lessPath: mainPath + 'less/',
        assetsPath: mainPath + 'assets/',

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
            integration: {
                configFile: '<%= globalConfig.testResourcePath %>karma.integration.conf.js',
                singleRun: true,
                browsers: ['PhantomJS']
            }
        },

        run: {
            integration_server: {
                cmd: 'java',
                args: [
                    '-jar',
                    '-Dspring.profiles.active=test',
                    '-Dserver.port=9001',
                    grunt.file.expand('./build/libs/*.jar')
                ],
                options: {
                    wait: false,
                    ready: /Tomcat started on port\(s\): 9001 \(http\)/
                }
            }
        },

        jshint: {
            options: {
                strict: true,
                browserify: true,
                globals: {
                    "describe": false,
                    "xdescribe": false,
                    "ddescribe": false,
                    "it": false,
                    "xit": false,
                    "iit": false,
                    "beforeEach": false,
                    "afterEach": false,
                    "expect": false,
                    "pending": false,
                    "spyOn": false,
                    "angular": false,
                    "inject": false,
                    "jasmine": false
                }
            },
            all: ['Gruntfile.js', '<%= globalConfig.jsPath %>**/*.js', '<%= globalConfig.specsPath %>**/*.js']
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

    grunt.registerTask('clean', function () {
        grunt.file.delete('./build/resources/main/static');
    });

    grunt.registerTask('build', ['clean', 'browserify', 'less', 'copy']);

    grunt.registerTask('unitTest', ['karma:unit']);

    grunt.registerTask('integrationTest', ['build', 'run:integration_server', 'karma:integration', 'stop:integration_server']);
};