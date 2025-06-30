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

        stage("Build and start docker compose services") {
          environment {
                KEYCLOAK_SECRET = credentials('MT_API_KEYCLOAK_SECRET')
                SPRING_DATASOURCE_PASSWORD = credentials('MT_FIREBIRD_PASSWORD')
          }
          steps {
              sh '''
                 cd jenkins
                 docker compose stop
                 docker stop clematis-money-tracker-api || true && docker rm clematis-money-tracker-api || true
                 docker stop clematis-money-tracker-api-demo || true && docker rm clematis-money-tracker-api-demo || true
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
