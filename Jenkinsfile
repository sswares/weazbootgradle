properties([[$class: 'BuildDiscarderProperty',
                strategy: [$class: 'LogRotator', numToKeepStr: '10']]])

node {
    blockOnDownstreamProjects()

    stage 'Clean Workspace'
    deleteDir()

    stage 'Checkout'
    checkout scm

    stage 'Build'

    if (isUnix()) {
        sh './gradlew clean check --console=plain --no-daemon --info --stacktrace'
    } else {
        bat 'gradlew clean check --console=plain --no-daemon --info --stacktrace'
    }

    stage 'Archive'
    archive 'server-auth/build/lib/*.jar'
    archive 'server-main/build/lib/*.jar'
}