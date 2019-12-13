kubectl -n kong-test apply -f configmap.yaml
docker build -t configmap-reload .
docker tag configmap-reload pog-dev-registry.cloudzcp.io/zcp-estimate/configmap-reload:0.0.1
docker push pog-dev-registry.cloudzcp.io/zcp-estimate/configmap-reload:0.0.1
kubectl -n kong-test delete -f deploy.yaml
kubectl -n kong-test apply -f deploy.yaml
kubectl -n kong-test get all