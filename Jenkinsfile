pipeline {
    agent any
    tools{
        maven 'maven 3.9.9'
    }
    stages{
        stage('Build maven'){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/arinabilan/ArinaBilan_24326485-8']])
                bat 'mvn clean package'
            }
        }

        stage('Unit Tests') {
            steps {
                // Run Maven 'test' phase. It compiles the test sources and runs the unit tests
                bat 'mvn test' // Use 'bat' for Windows agents or 'sh' for Unix/Linux agents
            }
        }

        stage('Build docker image'){
            steps{
                script{
                    bat 'docker build -t aribakan/bank-service:latest .'
                }
            }
        }
        stage('Push image to Docker Hub'){
            steps{
                script{
                   withDockerRegistry(credentialsId: 'dhpswid') {
                        bat 'docker push aribakan/bank-service:latest'
                   }
                }
            }
        }
    }
}