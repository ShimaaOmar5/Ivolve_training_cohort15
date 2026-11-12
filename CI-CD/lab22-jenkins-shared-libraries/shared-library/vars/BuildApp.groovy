
def call(Map config = [:]) {

    def pomFile = config.get('pomFile', 'pom.xml')

    stage('Unit Test') {
        echo 'Running Maven unit tests...'

        sh """
            mvn -B -f ${pomFile} clean test
        """

        echo 'Unit test phase completed.'
        echo 'Check Maven output for the number of tests actually executed.'
    }

    stage('Build App') {
        echo 'Building Spring Boot application...'

        sh """
            mvn -B -f ${pomFile} package -DskipTests
        """

        sh 'ls -lh target/*.jar'

        echo 'Application build completed.'
    }
}
