#!/bin/bash

helm repo add open-telemetry https://open-telemetry.github.io/opentelemetry-helm-charts
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana https://grafana.github.io/helm-charts

# install kube-prometheus-stack with remote write receiver enabled
helm -n monitoring upgrade --install --create-namespace kube-prometheus-stack prometheus-community/kube-prometheus-stack \
  -f kube-prometheus-stack.yaml --version 57.2.0

helm -n otel upgrade --install --create-namespace  opentelemetry-operator open-telemetry/opentelemetry-operator \
  -f otel-operator.yaml --version 0.53.0
 
helm -n monitoring upgrade --install --create-namespace loki grafana/loki -f loki.yaml --version 5.47.2

helm -n monitoring upgrade --install --create-namespace tempo grafana/tempo \
  --set tempoQuery.enabled=true --version 1.7.2