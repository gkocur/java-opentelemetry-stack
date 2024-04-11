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

# Install monitoring tools
1. Install necessary tools on the cluster (kube-prometheus-stack, opentelemetry-operator, loki, tempo)
    ```bash
    ./install-tools.sh
    ```
    
# Add opentelemetry collector
1. Apply the collector manifest:
    ```bash
    kubectl apply -f opentelemetry/collector.yaml
    ```
1. Wait for the collector
    ```bash
    kubectl -n otel get pods -w
    ```

# Add instrumentation
1. Apply the instrumentation manifest
    ```bash
    kubectl apply -f opentelemetry/instrumentation.yaml
    ```

# Annotate the pods
1. Add the annotation to the pod template
1. Apply the deployment manifest:
    ```bash
    kubectl apply -f k8s/dice-deployment.yaml
    kubectl apply -f k8s/users-deployment.yaml
    ```

# Write metrics to prometheus
1. Edit collector to add prometheus remote write exporter and add this exporter to the pipeline
1. Apply changed collector
    ```bash
    kubectl apply -f opentelemetry/collector.yaml
    ```
1. Check if the collector pod was restarted
    ```bash
    kubectl -n otel get pods
    ```
1. Forward the prometheus port to the localhost:
    ```bash
    kubectl -n monitoring port-forward svc/kube-prometheus-stack-prometheus 9090
    ```
1. Open http://localhost:9090 in browser, try to get metrics from `dice` service

