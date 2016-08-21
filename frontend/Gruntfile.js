'use strict';

module.exports = function (grunt) {
	require('load-grunt-tasks')(grunt);

	var appPath = 'app/';
	var testPath = 'test/';

	var globalConfig = {
		resourceDestination: '../server-main/src/main/resources/static/',
		buildDestination: '../server-main/build/resources/main/static/',

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
				dest: '<%= globalConfig.buildDestination %>'
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
			e2e: {
				args: ['node_modules/protractor/bin/protractor'],
				options: {
					wait: true
				}
			},
			e2e_ci: {
				args: ['node_modules/protractor/bin/protractor', 'protractor.ci.conf'],
				options: {
					wait: true
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

	grunt.registerTask('clean', function () {
		//noinspection JSUnresolvedFunction
		grunt.file.delete(globalConfig.buildDestination, {force: true});
	});

	grunt.registerTask('default', ['clean', 'browserify', 'less', 'copy']);

	grunt.registerTask('unitTest', ['jshint', 'eslint', 'karma:unit']);

	grunt.registerTask('karmaWatch', ['jshint', 'karma:watch']);

	grunt.registerTask('e2eTest', ['run:e2e']);

	grunt.registerTask('e2eTestCi', ['run:e2e_ci']);

	grunt.registerTask('e2eBuild', [
		'run:integration_main_server',
		'run:integration_auth_server',
		'run:integration_proxy_server',
		'run:e2e_ci',
		'stop:integration_main_server',
		'stop:integration_auth_server',
		'stop:integration_proxy_server'
	]);
};