pipeline {
    agent any

    environment {
        IMAGE_NAME = "soccerapp"
        CONTAINER_NAME = "soccerapp-container"
        DOCKER_PORT = "8080"
        APP_HOST = "ec2-user@13.59.232.250"
        KEY_PATH = "~/.ssh/id_rsa" // Jenkins EC2 private key to access app EC2
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Build JAR') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Copy Artifacts to App EC2') {
            steps {
                sh '''
                    echo "Copying JAR and Dockerfile to App EC2..."
                    scp -i $KEY_PATH target/*.jar $APP_HOST:/home/ec2-user/app.jar
                    scp -i $KEY_PATH Dockerfile $APP_HOST:/home/ec2-user/Dockerfile
                '''
            }
        }

        stage('Remote Docker Build & Deploy') {
            steps {
                sh '''
                    echo "Deploying on remote App EC2..."
                    ssh -i $KEY_PATH $APP_HOST << 'ENDSSH'
                        docker stop soccerapp-container || true
                        docker rm soccerapp-container || true
                        docker build -t soccerapp -f Dockerfile .
                        docker run -d --name soccerapp-container -p 8080:8080 soccerapp
                    ENDSSH
                '''
            }
        }
    }

    post {
        success {
            echo 'Successfully deployed'
        }
        failure {
            echo 'Deployment failed.'
        }
    }
}