pipeline {
    agent any

    triggers {
        pollSCM('* * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/capfer27/course3-jenkins-gs-spring-petclinic.git', branch: 'main'
            }
        }

        stage ('Build') {
            steps {
                sh './mvnw clean package'
            }

            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                // }
                // changed {
                    emailext attachLog: true, body: 'Please go to ${BUILD_URL} and verify the build.', 
                    compressLog: true, recipientProviders: [upstreamDevelopers(), requestor()],
                    to: 'test@jenkins.com' ,
                    subject: 'Job \'${JOB_NAME}\' ({BUILDER_NUMBER}) is waiting for input'
                }
            }
        }
    }

}