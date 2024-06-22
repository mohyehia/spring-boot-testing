pipeline {
    agent any

    tools {
        maven 'Maven 3.9.8'
    }

    environment {
        BUILD_VERSION = readMavenPom().getVersion()
        DOCKER_IMAGE = 'mohyehia99/spring-boot-testing'
    }

    stages {

        stage('Clean Workspace') {
            steps {
                script {
                    cleanWs()
                }
            }
        }

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/mohyehia/spring-boot-testing'
            }
        }

//        stage('Unit Testing') {
//            steps {
//                sh 'mvn clean test'
//            }
//        }
//
//        stage('SonarQube Analysis') {
//            steps {
//                withSonarQubeEnv(credentialsId: 'sonarQube-token', installationName: 'SonarQube') {
//                    sh "mvn verify sonar:sonar -DskipTests=true -Dsonar.projectKey=spring-boot-testing -Dsonar.projectName='spring-boot-testing'"
//                }
//                waitForQualityGate abortPipeline: true
//            }
//        }
//
//        stage('Package') {
//            steps {
//                sh 'mvn package -Dmaven.test.skip'
//            }
//        }
//
//        stage('Publish test results') {
//            steps {
//                junit "**/target/surefire-reports/*.xml"
//            }
//        }
//
//        stage('OWASP Dependency Check') {
//            steps{
//                dependencyCheck additionalArguments: '''
//                    -o './\'
//                    -s './\'
//                    -f 'ALL'
//                    --prettyPrint''', odcInstallation: 'OWASP Dependency Check'
//                dependencyCheckPublisher pattern: 'dependency-check-report.xml'
//            }
//        }
//
//        stage('Trivy FS Scan') {
//            steps {
//                sh 'trivy fs .'
//            }
//        }
//
//        stage('Docker Build') {
//            steps {
//                sh "mvn spring-boot:build-image -DskipTests"
//            }
//        }
//
//        stage('Trivy Image Scan') {
//            steps {
//                sh 'trivy image mohyehia99/spring-boot-testing:${BUILD_VERSION}'
//            }
//        }
//
//        stage('Docker Push') {
//            steps {
//                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'dockerHubUsername', passwordVariable: 'dockerHubPassword')]) {
//                    sh "docker login -u ${env.dockerHubUsername} -p ${env.dockerHubPassword}"
//                    sh "docker push mohyehia99/spring-boot-testing:${BUILD_VERSION}"
//                }
//            }
//        }

        stage('Update K8s Manifests') {
            steps {
                sh "cat k8s/manifest.yml"
                sh "sed -i 's|${DOCKER_IMAGE}.*|${DOCKER_IMAGE}:${BUILD_VERSION}|g' k8s/manifest.yml"
                sh "cat k8s/manifest.yml"
            }
        }

        stage('Push manifests to github') {
            steps{
                sh """
                    git config --global user.email "mohammedyehia99@gmail.com"
                    git config --global user.name "mohyehia"
                    git add k8s/manifest.yml
                    git commit -m "Update manifest.yml file"
                """
                withCredentials([usernamePassword(credentialsId: 'github-token', passwordVariable: 'gitPassword', usernameVariable: 'gitUsername')]) {
                    sh "git push https://${env.gitUsername}:${env.gitPassword}@github.com/mohyehia/spring-boot-testing.git main"
                }
            }
        }
    }
}
