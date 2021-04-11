pipeline { 
  agent any
  stages {
    stage('Test') {
       steps {
                    sh "pwd"
                    sh 'ls -al'
                    sh 'chmod +x ./gradlew clean'
                }   
        }
        
        stage('Build release ') {
          steps {
                sh 'chmod +x ./gradlew assembleRelease'
            }
        }
  }
}
