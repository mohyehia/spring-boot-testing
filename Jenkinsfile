pipeline {
    agent any

    tools {
        maven 'Maven 3.9.8'
    }

    environment {
        BUILD_VERSION = readMavenPom().getVersion()
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/mohyehia/spring-boot-testing'
            }
        }
        stage('Unit Testing') {
            steps {
                sh 'mvn clean test'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(credentialsId: 'sonarQube-token', installationName: 'SonarQube') {
                    sh "mvn verify sonar:sonar -DskipTests=true -Dsonar.projectKey=spring-boot-testing -Dsonar.projectName='spring-boot-testing'"
                }
            }
        }
//        stage('Quality Gate') {
//            steps {
//                waitForQualityGate abortPipeline: true
//            }
//        }
        stage('Package') {
            steps {
                sh 'mvn package -Dmaven.test.skip'
            }
        }
        stage('Publish test results') {
            steps {
                junit "**/target/surefire-reports/*.xml"
            }
        }
        stage('Docker Build') {
            steps {
                sh "mvn spring-boot:build-image -DskipTests"
            }
        }
        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'dockerHubUsername', passwordVariable: 'dockerHubPassword')]) {
                    sh "docker login -u ${env.dockerHubUsername} -p ${env.dockerHubPassword}"
                    sh "docker push mohyehia99/spring-boot-testing:${BUILD_VERSION}"
                }
            }
        }
    }
}
