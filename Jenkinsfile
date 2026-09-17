pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Backend Tests') {
            steps {
                dir('backend') {
                    sh 'mvn clean test'
                }
            }
        }

        stage('Frontend Build') {
            steps {
                dir('frontend') {
                    sh 'npm ci'
                    sh 'npm run build'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKERHUB_USERNAME',
                        passwordVariable: 'DOCKERHUB_TOKEN'
                    )
                ]) {
                    sh '''
                        docker build \
                          -t "$DOCKERHUB_USERNAME/leaveweb-backend:$BUILD_NUMBER" \
                          -t "$DOCKERHUB_USERNAME/leaveweb-backend:latest" \
                          ./backend

                        docker build \
                          -t "$DOCKERHUB_USERNAME/leaveweb-frontend:$BUILD_NUMBER" \
                          -t "$DOCKERHUB_USERNAME/leaveweb-frontend:latest" \
                          ./frontend
                    '''
                }
            }
        }

        stage('Login to Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKERHUB_USERNAME',
                        passwordVariable: 'DOCKERHUB_TOKEN'
                    )
                ]) {
                    sh '''
                        echo "$DOCKERHUB_TOKEN" | docker login \
                          --username "$DOCKERHUB_USERNAME" \
                          --password-stdin
                    '''
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKERHUB_USERNAME',
                        passwordVariable: 'DOCKERHUB_TOKEN'
                    )
                ]) {
                    sh '''
                        docker push "$DOCKERHUB_USERNAME/leaveweb-backend:$BUILD_NUMBER"
                        docker push "$DOCKERHUB_USERNAME/leaveweb-backend:latest"

                        docker push "$DOCKERHUB_USERNAME/leaveweb-frontend:$BUILD_NUMBER"
                        docker push "$DOCKERHUB_USERNAME/leaveweb-frontend:latest"
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'LeaveWeb CI pipeline completed successfully.'
        }

        failure {
            echo 'LeaveWeb CI pipeline failed.'
        }
    }
}