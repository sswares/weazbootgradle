'use strict';

module.exports = function (grunt) {
	require('load-grunt-tasks')(grunt);

	var appPath = 'app/';
	var testPath = 'test/';
	var mainJavaAppArtifactLocation = '../server-main/build/libs/';
	var authJavaAppArtifactLocation = '../server-auth/build/libs/';
	var proxyPort = 9000;
	var mainJavaAppPort = 9001;
	var authJavaAppPort = 9002;

	var globalConfig = {
		appBuildPath: 'build/',

		resourceDestination: '../server-main/src/main/resources/static/',
		javaMainAppBuildDestination: '../server-main/build/resources/main/static/',

		appPath: appPath,
		jsPath: appPath + 'js/',
		partialsPath: appPath + 'partials/',
		lessPath: appPath + 'less/',
		assetsPath: appPath + 'assets/',

		unitPath: testPath + 'test/unit/',
		e2ePath: testPath + 'e2e/'
	};

	//noinspection JSUnresolvedFunction
	grunt.initConfig({
		globalConfig: globalConfig,

		availabletasks: {
			tasks: {}
		},

		browserify: {
			js: {
				src: ['<%= globalConfig.jsPath %>**/*.js'],
				dest: '<%= globalConfig.resourceDestination %>js/app.js',
				options: {
					browserifyOptions: {
						transform: [require('browserify-istanbul')],
						debug: true
					}
				}
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
				files: ['<%= globalConfig.appPath %>**/*.js'],
				tasks: ['browserify', 'copy']
			},

			html: {
				files: ['<%= globalConfig.appPath %>**/*.html'],
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
				dest: '<%= globalConfig.javaMainAppBuildDestination %>'
			}
		},

		less: {
			development: {
				options: {
					paths: ['<%= globalConfig.lessPath %>']
				},
				files: {
					'<%= globalConfig.resourceDestination %>css/app.css': '<%= globalConfig.lessPath %>app.less'
				}
			}
		},

		karma: {
			unit: {
				configFile: 'karma.conf.js',
				singleRun: true,
				browsers: ['PhantomJS']
			},
			watch: {
				configFile: 'karma.conf.js',
				singleRun: false,
				autoWatch: true,
				browsers: ['PhantomJS']
			}
		},

		protractor_coverage: {
			options: {
				keepAlive: true,
				noColor: false,
				collectorPort: 3001,
				coverageDir: '<%= globalConfig.appBuildPath %>test-results/protractor/coverage',
				args: {
					baseUrl: 'http://localhost:' + proxyPort
				}
			},
			local: {
				options: {
					configFile: 'protractor.conf.js'
				}
			},
			ci: {
				options: {
					configFile: 'protractor.ci.conf'
				}
			}
		},

		makeReport: {
			src: '<%= globalConfig.appBuildPath %>test-results/protractor/coverage/*.json',
			options: {
				type: 'lcov',
				dir: '<%= globalConfig.appBuildPath %>test-results/protractor/coverage/',
				print: 'detail'
			}
		},

		run: {
			integration_main_server: {
				cmd: 'java',
				args: [
					'-jar',
					'-Dspring.profiles.active=test',
					grunt.file.expand(mainJavaAppArtifactLocation + '*.jar')[0]
				],
				options: {
					wait: false,
					ready: new RegExp('Tomcat started on port\\(s\\): ' + mainJavaAppPort + ' \\(http\\)')
				}
			},
			integration_auth_server: {
				cmd: 'java',
				args: [
					'-jar',
					'-Dspring.profiles.active=test',
					grunt.file.expand(authJavaAppArtifactLocation + '*.jar')[0]
				],
				options: {
					wait: false,
					ready: new RegExp('Tomcat started on port\\(s\\): ' + authJavaAppPort + ' \\(http\\)')
				}
			},
			integration_proxy_server: {
				args: ['./proxy/integrationProxyStart.js'],
				options: {
					wait: false,
					ready: new RegExp('Reverse proxy listening on port: ' + proxyPort)
				}
			},
			dev_proxy_server: {
				args: ['./proxy/devProxyStart.js'],
				options: {
					wait: true
				}
			}
		},

		eslint: {
			options: {
				configFile: '.eslintrc'
			},
			all: [
				'Gruntfile.js', '<%= globalConfig.jsPath %>**/*.js',
				'<%= globalConfig.unitPath %>**/*.js',
				'<%= globalConfig.e2ePath %>**/*.js'
			]
		}
	});
	grunt.loadNpmTasks('gruntify-eslint');
	grunt.loadNpmTasks('grunt-protractor-coverage');
	grunt.loadNpmTasks('grunt-available-tasks');
	grunt.loadNpmTasks('grunt-istanbul');

	grunt.registerTask('clean', function () {
		//noinspection JSUnresolvedFunction
		grunt.file.delete(globalConfig.javaMainAppBuildDestination, {force: true});
		//noinspection JSUnresolvedFunction
		grunt.file.delete(globalConfig.appBuildPath, {force: true});

	});

	grunt.registerTask('default', ['clean', 'browserify', 'less', 'copy']);

	grunt.registerTask('unitTest', ['eslint', 'karma:unit']);

	grunt.registerTask('karmaWatch', ['karma:watch']);

	grunt.registerTask('e2eTest', ['protractor_coverage:local']);

	grunt.registerTask('e2eBuild', [
		'run:integration_main_server',
		'run:integration_auth_server',
		'run:integration_proxy_server',
		'protractor_coverage:ci',
		'stop:integration_main_server',
		'stop:integration_auth_server',
		'stop:integration_proxy_server',
		'makeReport'
	]);

	grunt.registerTask('task', ['availabletasks']);
	grunt.registerTask('tasks', ['availabletasks']);
};