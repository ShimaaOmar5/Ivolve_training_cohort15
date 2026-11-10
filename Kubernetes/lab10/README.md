cat > Kubernetes/lab10/README.md <<'EOF'
# Lab 10: Kubernetes Node Isolation Using Taints and Labels

## Objective

Configure node isolation in a Kubernetes cluster using taints and labels. Apply a `NoSchedule` taint to a worker node to control pod scheduling, and assign a project label to identify the node.

## Environment

- **OS:** Ubuntu 24.04.5 LTS (WSL2)
- **Kubernetes Distribution:** Minikube v1.39.0
- **Kubernetes Version:** v1.37.0
- **Container Runtime:** containerd
- **Driver:** Docker
- **Cluster Nodes:** 1 Control Plane and 1 Worker
- **Worker Node:** minikube-m02

## Implementation

### 1. Verify Cluster Nodes

Check the cluster nodes and ensure that both the control-plane and worker nodes are in the `Ready` state.

```bash
kubectl get nodes -o wide
