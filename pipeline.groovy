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
                // sh 'false' // true

            }

            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                // }
                // changed {
                    // emailext  subject: 'Job \'${JOB_NAME}\' (${BUILD_NUMBER}) is waiting for input',
                    emailext  subject: "Job \'${JOB_NAME}\' (build ${BUILD_NUMBER}) ${currentBuild.result}",
                    body: "Please go to ${BUILD_URL} and verify the build.",
                    attachLog: true,
                    compressLog: true, 
                    to: "test15@jenkins.com",
                    recipientProviders: [upstreamDevelopers(), requestor()]
                }
            }
        }
    }

}