
def call(Map config = [:]) {

    def imageName = config.get('imageName')
    def imageTag = config.get('imageTag', env.BUILD_NUMBER)
    def credentialsId = config.get(
        'credentialsId',
        'dockerhub-credentials'
    )

    if (!imageName) {
        error 'imageName is required'
    }

    def imageRef = "${imageName}:${imageTag}"

    try {

        stage('Build Image') {

            echo "Building Docker image: ${imageRef}"

            withEnv(["IMAGE_REF=${imageRef}"]) {
                sh '''
                    docker build -t "$IMAGE_REF" .
                    docker images "$IMAGE_REF"
                '''
            }
        }

        stage('Scan Image') {

            echo "Scanning ${imageRef} using Trivy..."

            withEnv(["IMAGE_REF=${imageRef}"]) {
                sh '''
                    trivy image \
                      --exit-code 0 \
                      --severity HIGH,CRITICAL \
                      "$IMAGE_REF"

                    trivy image \
                      --exit-code 0 \
                      --format json \
                      --output trivy-report.json \
                      "$IMAGE_REF"
                '''
            }

            archiveArtifacts(
                artifacts: '**/trivy-report.json',
                allowEmptyArchive: true
            )
        }

        stage('Push Image') {

            echo "Pushing ${imageRef} to Docker Hub..."

            withCredentials([
                usernamePassword(
                    credentialsId: credentialsId,
                    usernameVariable: 'DOCKERHUB_USERNAME',
                    passwordVariable: 'DOCKERHUB_PASSWORD'
                )
            ]) {

                withEnv(["IMAGE_REF=${imageRef}"]) {

                    sh '''
                        set -eu

                        trap 'docker logout >/dev/null 2>&1 || true' EXIT

                        printf '%s' "$DOCKERHUB_PASSWORD" |
                          docker login \
                            -u "$DOCKERHUB_USERNAME" \
                            --password-stdin

                        docker push "$IMAGE_REF"
                    '''
                }
            }
        }

    } finally {

        stage('Delete Image Locally') {

            echo "Removing local image: ${imageRef}"

            sh """
                docker image rm '${imageRef}' || true
            """
        }
    }

    env.BUILT_IMAGE = imageRef
}
