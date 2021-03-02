pipeline {
    agent any
    stages {
        stage ('Build Backend'){
            steps {
                sh 'mvn clean package -DskipTest=true'
            }
        }

        stage ('Unit Tests'){
            steps {
                sh 'mvn test'
            }
        }

        stage ('Sonar Analysis'){
            environment {
                scannerHome = tool 'SONNAR_SCANNER'
            }
            steps {
                withSonarQubeEnv('SONAR_LOCAL'){
                    sh "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=DeployBack -Dsonar.host.url=http://localhost:9000  -Dsonar.login=603076630ad248b86c47cbe6d4c6cda9a95e29e7 -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/.mvn/**,**/src/test/**,**/model/**,**Application.java"
                }
            }
        }

        stage ('Quality Gate'){
            steps {
                sleep(30)
                timeout(time: 1, unit:'MINUTES'){
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage ('Deploy Backend'){
            steps {
                deploy adapters: [tomcat8(credentialsId: 'tomcat_login', path: '', url: 'http://localhost:8080/')], contextPath: 'tasks-backend', war: 'target/tasks-backend.war'
            }
        }

        stage ('API Test'){
            steps {
                dir('api-test'){
                    git credentialsId: 'github_login', url: 'https://github.com/PaulaOliveiraM/tasks-api-test'
                    sh 'mvn test'
                }
            }
        }

        stage ('Deploy Frontend'){
            steps {
                dir ('frontend'){
                    git credentialsId: 'github_login', url: 'https://github.com/PaulaOliveiraM/tasks-frontend'
                    sh 'mvn clean package'
                    deploy adapters: [tomcat8(credentialsId: 'tomcat_login', path: '', url: 'http://localhost:8080/')], contextPath: 'tasks', war: 'target/tasks.war'
                }
            }
        }

        stage ('Functional Test'){
            steps {
                dir('functional-test'){
                    git credentialsId: 'github_login', url: 'https://github.com/PaulaOliveiraM/tasks-functional-tests'
                    sh 'mvn test'
                }
            }
        }

        stage('Deploy Prod') {
            steps {
                sh 'docker-compose build'
                sh 'docker-compose up -d'
            }

        }

        stage ('Health Check'){
            steps {
                sleep(20)
                dir('functional-test'){
                    sh 'mvn verify -Dskip.surefire.tests'
                }
            }
        }

    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml, api-test/target/surefire-reports/*.xml, functional-test/target/surefire-reports/*.xml, functional-test/target/failsafe-reports/*.xml'
        }

        unsuccessful {
            emailext attachLog: true, body: 'See the attached log below.', subject: 'Build $BUILD_NUMBER has failed', to: 'paula.oliveira.m@gmail.com'
        }

        fixed {
            emailext attachLog: true, body: 'See the attached log below.', subject: 'Build is fine!', to: 'paula.oliveira.m@gmail.com'
        }
    }
}
