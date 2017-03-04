properties([[$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '10']]])

node {
    ansiColor('xterm') {
        stage 'Clean Workspace'
        deleteDir()

        stage 'Checkout'
        checkout scm

        stage 'Build'

        if (isUnix()) {
            wrap([$class: 'Xvfb']) {
                sh './gradlew build --console=plain --no-daemon --info --stacktrace'
            }
        } else {
            bat 'gradlew build --console=plain --no-daemon --info --stacktrace'
        }

        stage 'Archive'
        step([$class: 'JUnitResultArchiver', testResults: '**/build/test-results/**/*.xml', allowEmptyResults: true])
        step([$class: 'JacocoPublisher'])
        step([$class: 'ArtifactArchiver', artifacts: '**/build/libs/*.jar*', fingerprint: true])
    }
}
