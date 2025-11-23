pipeline {

    agent any
    environment {
        REMOTE_HOST = "192.168.1.118"
        REMOTE_USER = "anton"
        SSH_DEST = "${REMOTE_USER}@${REMOTE_HOST}"
        REMOTE_APP_DIR = "/home/anton/deploy/mt/api"
    }

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

        stage ('Dependency-Check') {
            steps {
                dependencyCheck additionalArguments: '''
                    -o "./"
                    -s "./"
                    -f "ALL"
                    --prettyPrint''', nvdCredentialsId: 'NVD_API_Key', odcInstallation: 'Dependency Checker'
                dependencyCheckPublisher pattern: 'dependency-check-report.xml'
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    sh "exit 1"
                }
            }
        }

        stage('Publish tests') {
            steps {
                recordCoverage(tools: [[parser: 'JACOCO']],
                        id: 'jacoco', name: 'JaCoCo Coverage',
                        sourceCodeRetention: 'EVERY_BUILD',
                        qualityGates: [
                                [threshold: 60.0, metric: 'LINE', baseline: 'PROJECT', unstable: true],
                                [threshold: 60.0, metric: 'BRANCH', baseline: 'PROJECT', unstable: true]])
            }
        }

        stage('Build docker image') {
            steps {
                script {
                   // Using secret file
                   withCredentials([
                      file(credentialsId: 'keycloak_certificate', variable: 'SSL_CERT'),
                   ]) {
                      sh """
                         cp "$SSL_CERT" "${WORKSPACE}/jenkins/keycloak.pem"
                         docker build -t clematis.mt.api .
                      """
                   }
                }
            }
        }

        stage('Export Docker Images') {
          steps {
            sh '''
              mkdir -p docker_export
              docker save clematis.mt.api > docker_export/clematis.mt.api.tar
            '''
          }
        }

        stage('Transfer Files to Yoda') {
          steps {
            sshagent (credentials: ['yoda-anton-key']) {
              sh '''
                [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                scp -o StrictHostKeyChecking=no docker_export/*.tar "${SSH_DEST}:${REMOTE_APP_DIR}/"
                scp -o StrictHostKeyChecking=no "jenkins/docker-compose.yml" "${SSH_DEST}:${REMOTE_APP_DIR}/"
                scp -o StrictHostKeyChecking=no "jenkins/demo.env" "${SSH_DEST}:${REMOTE_APP_DIR}/"
                scp -o StrictHostKeyChecking=no "jenkins/uat.env" "${SSH_DEST}:${REMOTE_APP_DIR}/"
                scp -o StrictHostKeyChecking=no "jenkins/money_tracker_server.env" "${SSH_DEST}:${REMOTE_APP_DIR}/"
               '''
            }
          }
        }

        stage('Deploy on Yoda') {
          environment {
            KEYCLOAK_SECRET = credentials('MT_API_KEYCLOAK_SECRET')
            SPRING_DATASOURCE_PASSWORD = credentials('MT_FIREBIRD_PASSWORD')
          }
          steps {
            sshagent (credentials: ['yoda-anton-key']) {
                sh """
                  ssh ${SSH_DEST} '
                    cd ${REMOTE_APP_DIR} && \
                    docker rm -f rm -f clematis-money-tracker-db clematis-money-tracker-db-demo clematis-money-tracker-api clematis-money-tracker-api-demo 2>/dev/null || true && \
                    export KEYCLOAK_SECRET="${KEYCLOAK_SECRET}" && \
                    export SPRING_DATASOURCE_PASSWORD="${SPRING_DATASOURCE_PASSWORD}" && \
                    docker load < clematis.mt.api.tar && \
                    docker compose -f docker-compose.yml build --build-arg KEYCLOAK_SECRET="${KEYCLOAK_SECRET}" --build-arg SPRING_DATASOURCE_PASSWORD="${SPRING_DATASOURCE_PASSWORD}" && \
                    docker compose -f docker-compose.yml up -d money-tracker-db money-tracker-db-demo && \
                    docker compose -f docker-compose.yml up -d --no-deps --build money-tracker-api money-tracker-api-demo
                  '
                """
            }
          }
        }


    }

    post {
        always {
            junit '**/build/**/test-results/test/*.xml'
            sh '''
               rm -rf docker_export
               rm -rf "${WORKSPACE}/jenkins/keycloak.pem"
            '''
        }
    }
}
