pipeline {
    agent any

    environment {
        IMAGE_NAME = "soccerapp"
        CONTAINER_NAME = "soccerapp-container"
        DOCKER_PORT = "8080"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/schhetri1208/soccer-app.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('Stop Old Container') {
            steps {
                sh """
                docker stop $CONTAINER_NAME || true
                docker rm $CONTAINER_NAME || true
                """
            }
        }

        stage('Run New Container') {
            steps {
                sh """
                docker run -d --name $CONTAINER_NAME -p $DOCKER_PORT:8080 $IMAGE_NAME
                """
            }
        }
    }
}
