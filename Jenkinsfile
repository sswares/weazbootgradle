node {
    stage 'Build'

    if(isUnix()) {
        sh './gradlew clean build'
    } else{
        bat './gradle clean build'
    }
}