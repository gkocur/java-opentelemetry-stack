# Prerequisites
1. [Docker](https://www.docker.com) (to setup kind cluster). 
1. [Kind](https://kind.sigs.k8s.io)
1. [Helm](https://helm.sh)

# Setup
1. Create a kind cluster:
    ```bash
    kind create cluster --name otel --config kind.yaml
    ```
1. Build the docker image:
    ```bash
    docker buildx build --platform linux/amd64 --load -t dice:1.0 apps/dice
    docker buildx build --platform linux/amd64 --load -t users:1.0 apps/users
    ```
1.  Load the image to the kind cluster:
    ```bash
    kind load docker-image dice:1.0 -n otel
    kind load docker-image users:1.0 -n otel
    ```

# Deployment
1. Deploy the applications on the kind cluster:
    ```bash
    kubectl apply -f k8s/dice-deployment.yaml
    kubectl apply -f k8s/dice-service.yaml
    kubectl apply -f k8s/users-deployment.yaml
    kubectl apply -f k8s/users-service.yaml
    ```
1. Check the deployment:
    ```bash
    kubeclt get pods
    kubectl get services
    ```
1. Forward the `dice` app port to the localhost:
    ```bash
    kubectl port-forward svc/dice 8080
    ```
1. Try to access this app using curl or the browser:
    ```bash
    curl http://localhost:8080/rolldice
    ```
    