node {
    gitlabCommitStatus("Commit Build") {
        stage('Clone') {
            echo "1.Git Clone Stage"
            git credentialsId: 'gitlab', url: 'http://baiduyun.com:20080/root/springboot-sample-demo.git'
        }
        stage('Package') {
            echo "2.Maven Package Stage"
            sh "/usr/local/maven/apache-maven-3.6.3/bin/mvn clean package"
        }
        stage('Archive') {
            echo "3.Archive Artifacts Stage"
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
        stage('Build') {
            echo "4.Build Docker Image Stage"
            sh "cd docker/manual && sh manual.sh 1.0.0-SNAPSHOT jenkins"
        }
    }
}