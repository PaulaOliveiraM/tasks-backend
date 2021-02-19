pipeline {
    agent any
    stages {
        stage ('Build Backend'){
            steps {
                sh 'mvn clear package -DskipTest=true'
            }
        }
    }
}