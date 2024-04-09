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
    