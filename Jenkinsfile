pipeline {
    agent any

    tools {
        maven 'Maven 3.9.4'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/mohyehia/spring-boot-testing'
            }
        }
        stage('SonarQube Analysis') {
            withSonarQubeEnv(credentialsId: 'sonarQube-token') {
                sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=spring-boot-testing -Dsonar.projectName='spring-boot-testing'"
            }
        }
        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }
        stage('Docker Build') {
            steps {
                sh 'docker build -t mohyehia99/spring-boot-testing:0.0.2 .'
            }
        }
        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUsername')]) {
                    sh "docker login -u ${env.dockerHubUsername} -p ${env.dockerHubPassword}"
                    sh 'docker push mohyehia99/spring-boot-testing:0.0.2'
                }
            }
        }
    }
}


sqp_5dec7d1536fb3ce1806fed69bb4378ce1983fed4

stage('SonarQube Analysis') {
    def mvn = tool 'Default Maven';
    withSonarQubeEnv() {
        sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=spring-boot-testing -Dsonar.projectName='spring-boot-testing'"
    }
}