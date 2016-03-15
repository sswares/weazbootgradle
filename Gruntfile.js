'use strict';

module.exports = function (grunt) {
    var mainPath = './src/main/frontend/';
    var testPath = './src/test/frontend/';

    var globalConfig = {
        buildDestination: './build/resources/main/static/',
        buildTestDestination: './build/test/dist/',
        buildTemp: './build/tmp/',
        
        mainPath: mainPath,
        jsPath: mainPath + 'js/',
        partialsPath: mainPath + 'partials/',
        lessPath: mainPath + 'less/',
        assetsPath: mainPath + 'assets/',
        
        testPath: testPath,
        specPath: testPath + 'specs/'
    };

    var port = grunt.option('port') || '9001';
    grunt.initConfig({
        globalConfig: globalConfig,

        browserify: {
            options: {
                alias: {
                    'templates': '/js/templates.js'
                }
            },
            js: {
                src: ['<%= globalConfig.jsPath %>**/*.js', '<%= globalConfig.buildTemp %>js/templates.js'],
                dest: '<%= globalConfig.buildDestination %>js/app.js'
            },
            spec: {
                src: '<%= globalConfig.specPath %>**/*.js',
                dest: '<%= globalConfig.buildTestDestination %>js/app.js'
            }
        },

        html2js: {
            options: {
                base: '<%= globalConfig.mainPath %>',
                module: 'templates',
                amd: true,
                amdPrefixString: '\'use strict\'; module.exports =',
                amdSuffixString: ''
            },
            main: {
                src: ['<%= globalConfig.partialsPath %>**/*.html'],
                dest: '<%= globalConfig.buildTemp %>js/templates.js'
            }
        },

        watch: {
            scripts: {
                files: ['<%= globalConfig.mainPath %>**/*.js'],
                tasks: ['rebuild'],
                options: {
                    spawn: false
                }
            },

            html: {
                files: ['<%= globalConfig.mainPath %>**/*.html'],
                tasks: ['rebuild'],
                options: {
                    spawn: false
                }
            },

            css: {
                files: ['<%= globalConfig.lessPath %>**/*.less'],
                tasks: ['rebuild'],
                options: {
                    spawn: false
                }
            }
        },

        copy: {
            html: {
                expand: true,
                src: ['**/*.html'],
                cwd: '<%= globalConfig.mainPath %>',
                dest: '<%= globalConfig.buildDestination %>'
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
                configFile: '<%= globalConfig.testPath %>karma.conf.js'
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
    grunt.loadNpmTasks('grunt-html2js');

    grunt.registerTask('clean', function () {
        grunt.file.delete('./build/resources/main/static');
    });

    grunt.registerTask('rebuild', ['clean', 'html2js', 'browserify:js', 'less', 'copy']);
    grunt.registerTask('rebuild-test', ['clean', 'html2js', 'browserify', 'copy:fixtures']);

    grunt.registerTask('test', ['rebuild-test', 'karma', 'jshint']);
    grunt.registerTask('testSetup', ['rebuild-test']);
    grunt.registerTask('build', ['rebuild']);
};