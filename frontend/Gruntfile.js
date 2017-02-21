'use strict';

module.exports = function (grunt) {
  require('load-grunt-tasks')(grunt);

  const mainJavaAppArtifactLocation = '../server-main/build/libs/';
  const authJavaAppArtifactLocation = '../server-auth/build/libs/';
  const mainJavaAppPort = 9001;
  const authJavaAppPort = 9002;

  //noinspection JSUnresolvedFunction
  grunt.initConfig({

    availabletasks: {
      tasks: {}
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
      end2End: {
        exec: 'npm run e2e --silent',
      }
    },

  });
  grunt.loadNpmTasks('grunt-available-tasks');

  grunt.registerTask('e2eBuild', [
    'run:integration_main_server',
    'run:integration_auth_server',
    'run:end2End',
    'stop:integration_main_server',
    'stop:integration_auth_server',
  ]);

  grunt.registerTask('default', ['availabletasks']);
  grunt.registerTask('task', ['availabletasks']);
  grunt.registerTask('tasks', ['availabletasks']);
};
