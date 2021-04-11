pipeline { 
  agent any
  stages {
    stage('Test') {
       steps {
                    sh "pwd"
                    sh 'ls -al'
                    sh './gradlew clean'
                }   
        }
        
        stage('Build release ') {
          steps {
                sh './gradlew assembleRelease'
            }
        }
  }
}
