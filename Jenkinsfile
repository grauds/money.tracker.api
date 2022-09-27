node {
    pipeline {

        agent any

        stages {

            stage("Verify tooling") {
                steps {
                    sh '''
              cd jenkins
              docker version
              docker info
              docker compose version
              curl --version
              jq --version
              docker compose ps
            '''
                }
            }

            stage('Get code') {
                steps {
                    // Get some code from a GitHub repository
                    git 'https://github.com/grauds/money.tracker.api.git'
                    sh 'chmod +x gradlew'
                }
            }

            stage('Gradle build') {
                steps {
                    sh './gradlew clean build'
                }
            }

            stage('Build docker image') {
                steps {
                    sh 'docker build -t clematis.mt.api .'
                }
            }

            stage('Update database from the cloud') {
                steps {
                    sh 'wget "https://onedrive.live.com/download?cid=5EFC5BD533A5D0E5&resid=5EFC5BD533A5D0E5%21113569&authkey=AC8hUo3cfK4ayvw" -O /home/firebird/db/mt.fdb'
                }
            }

            stage("Build and start docker compose services") {
                environment {
                    KEYCLOAK_SECRET = credentials('KEYCLOAK_SECRET')
                    SPRING_DATASOURCE_PASSWORD = credentials('SPRING_DATASOURCE_PASSWORD')
                }
                steps {
                    sh '''
                 cd jenkins
                 docker compose stop
                 docker compose build --build-arg KEYCLOAK_SECRET='$KEYCLOAK_SECRET' --build-arg SPRING_DATASOURCE_PASSWORD='$SPRING_DATASOURCE_PASSWORD'
                 docker compose up -d 
              '''
                }
            }
        }

        post {
            always {
                junit '**/build/**/test-results/test/*.xml'
            }
        }
    }
}

