
def call(Map config = [:]) {

    def image = config.get('image')
    def manifest = config.get(
        'manifest',
        'k8s/deployment.yaml'
    )
    def namespace = config.get('namespace', 'ivolve')
    def deployment = config.get(
        'deployment',
        'demoapp-lab21'
    )

    if (!image) {
        error 'Kubernetes deployment image is required'
    }

    withEnv([
        "IMAGE_REF=${image}",
        "MANIFEST=${manifest}",
        "NAMESPACE=${namespace}",
        "DEPLOYMENT=${deployment}"
    ]) {

        stage('Edit Deployment File') {

            echo "Updating Kubernetes image to ${image}"

            sh '''
                set -eu

                test -f "$MANIFEST"

                sed -i -E \
                  "s|(^[[:space:]]*image:[[:space:]]*).*$|\\1${IMAGE_REF}|" \
                  "$MANIFEST"

                echo "Updated deployment manifest:"
                grep -n 'image:' "$MANIFEST"
            '''
        }

        stage('Deploy to Kubernetes') {

            sh '''
                set -eu

                kubectl apply \
                  -f "$MANIFEST" \
                  -n "$NAMESPACE"

                kubectl rollout status \
                  deployment/"$DEPLOYMENT" \
                  -n "$NAMESPACE" \
                  --timeout=180s

                echo "Deployment status:"
                kubectl get deployment "$DEPLOYMENT" \
                  -n "$NAMESPACE"

                echo "Pods:"
                kubectl get pods -n "$NAMESPACE"
            '''
        }
    }
}

