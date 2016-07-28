'use strict';

module.exports = function (grunt) {
    var mainPath = './src/main/';
    var testPath = './src/test/';

    var globalConfig = {
        resourceDestination: '../server-main/src/main/resources/static/',
        buildDestination: '../server-main/build/resources/main/static/',

        mainPath: mainPath,
        jsPath: mainPath + 'js/',
        partialsPath: mainPath + 'partials/',
        lessPath: mainPath + 'less/',
        assetsPath: mainPath + 'assets/',

        specsPath: testPath + 'specs/',
        end2EndPath: testPath + 'end2end/',
        testResourcePath: testPath + 'resources/'
    };

    var execSync = require('child_process').execSync;
    var stdout;

    if (process.platform === "win32") {
        stdout = execSync('for %a in ("%path:;=";"%") do @echo %~a').toString();
        if (stdout) {
            var pathItems = stdout.split("\r\n");
            for (var i = 0; i < pathItems.length; i++) {
                if (pathItems[i].indexOf('nodejs') > -1 && pathItems[i].indexOf('frontend') === -1) {
                    globalConfig.localNode = pathItems[i] + 'node.exe';
                    break;
                }
            }
        }

        if (!globalConfig.localNode) {
            globalConfig.localNode = grunt.file.expand("./nodejs/*windows*/bin/node.exe")[0];
        }

        globalConfig.scriptSuffix = '.cmd';
    } else {
        stdout = execSync('which node').toString().replace(/(\r\n|\n|\r)/gm, "");

        if (stdout.indexOf('frontend') === -1 && stdout.indexOf('node') > -1) {
            globalConfig.localNode = stdout;
        }

        if (!globalConfig.localNode) {
            globalConfig.localNode = grunt.file.expand("./nodejs/*/bin/node")[0];
        }

        globalConfig.scriptSuffix = '';
    }

    grunt.log.write("Local node set as: " + globalConfig.localNode);

    grunt.initConfig({
        globalConfig: globalConfig,

        browserify: {
            js: {
                src: ['<%= globalConfig.jsPath %>**/*.js'],
                dest: '<%= globalConfig.resourceDestination %>js/app.js'
            }
        },

        watch: {
            options: {
                spawn: false,
                livereload: true,
                keepAlive: true,
                debounceDelay: 100
            },
            scripts: {
                files: ['<%= globalConfig.mainPath %>**/*.js'],
                tasks: ['browserify', 'copy']
            },

            html: {
                files: ['<%= globalConfig.mainPath %>**/*.html'],
                tasks: ['copy']
            },

            less: {
                files: ['<%= globalConfig.lessPath %>**/*.less'],
                tasks: ['less', 'copy']
            }
        },

        copy: {
            htmlForResources: {
                expand: true,
                src: ['**/*.html'],
                cwd: '<%= globalConfig.partialsPath %>',
                dest: '<%= globalConfig.resourceDestination %>partials'
            },
            assetsForResources: {
                expand: true,
                cwd: '<%= globalConfig.assetsPath %>',
                src: ['**/*.jpeg', '**/*.png', '**/*.eot', '**/*.ttf', '**/*.otf', '**/*.woff', '**/*.svg', '**/*.ico'],
                dest: '<%= globalConfig.resourceDestination %>assets'
            },
            // This is here because there is currently no support for hotswapping resources in IDEA.
            // Will be removed if/when https://youtrack.jetbrains.com/issue/IDEA-151817 is completed.
            // Devtools will overwrite this when you rebuild, but it should be up-to-date with the current watched version.
            everythingToBuild: {
                expand: true,
                src: ['**/*'],
                cwd: '<%= globalConfig.resourceDestination %>',
                dest: '<%= globalConfig.buildDestination %>'
            }
        },

        less: {
            development: {
                options: {
                    paths: ["<%= globalConfig.lessPath %>"]
                },
                files: {
                    "<%= globalConfig.resourceDestination %>css/app.css": "<%= globalConfig.lessPath %>app.less"
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
            options: {
                nodeBin: '<%= globalConfig.localNode %>'
            },
            development: {
                options: {
                    configFile: '<%= globalConfig.testResourcePath %>protractor.conf.js'
                }
            },
            build: {
                options: {
                    configFile: '<%= globalConfig.testResourcePath %>protractor.ci.conf.js'
                }
            }
        },

        run: {
            integration_main_server: {
                cmd: 'java',
                args: [
                    '-jar',
                    '-Dspring.profiles.active=test',
                    grunt.file.expand('../server-main/build/libs/*.jar')[0]
                ],
                options: {
                    wait: false,
                    ready: /Tomcat started on port\(s\): 9001 \(http\)/
                }
            },
            integration_auth_server: {
                cmd: 'java',
                args: [
                    '-jar',
                    '-Dspring.profiles.active=test',
                    grunt.file.expand('../server-auth/build/libs/*.jar')[0]
                ],
                options: {
                    wait: false,
                    ready: /Tomcat started on port\(s\): 9002 \(http\)/
                }
            },
            integration_proxy_server: {
                args: ['./proxy/integrationProxyStart.js'],
                options: {
                    wait: false,
                    ready: /Reverse proxy listening on port: 9000/
                }
            },
            dev_proxy_server: {
                args: ['./proxy/devProxyStart.js'],
                options: {
                    wait: true
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
        grunt.file.delete(globalConfig.buildDestination, {force: true});
    });

    grunt.registerTask('build', ['clean', 'browserify', 'less', 'copy']);

    grunt.registerTask('unitTest', ['jshint', 'karma:unit']);

    grunt.registerTask('karmaWatch', ['jshint', 'karma:watch']);

    grunt.registerTask('e2eBuild', [
        'run:integration_main_server',
        'run:integration_auth_server',
        'run:integration_proxy_server',
        'protractor:build',
        'stop:integration_main_server',
        'stop:integration_auth_server',
        'stop:integration_proxy_server'
    ]);
};