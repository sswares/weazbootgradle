properties([[$class: 'BuildDiscarderProperty',
                strategy: [$class: 'LogRotator', numToKeepStr: '10']]])

node {
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
    step([$class: 'ArtifactArchiver', artifacts: 'server-auth/build/lib/*.jar', fingerprint: true])
    step([$class: 'ArtifactArchiver', artifacts: 'server-main/build/lib/*.jar', fingerprint: true])
}