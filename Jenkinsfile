node {
    stage 'Checkout'
    git url: 'https://github.com/themadweaz/weazbootgradle.git'

    stage 'Build'

    if(isUnix()) {
        sh './gradlew clean build'
    } else{
        bat './gradle clean build'
    }
}