# Lab 11: Namespace Management and Resource Quota Enforcement

## Objective

Create a Kubernetes namespace to isolate application resources and enforce resource limits using a ResourceQuota. Set a maximum limit of three Pods and verify that Kubernetes prevents the creation of additional Pods once the limit is reached.

## Environment

- **OS:** Ubuntu 24.04.5 LTS (WSL2)
- **Kubernetes Distribution:** Minikube v1.39.0
- **Kubernetes Version:** v1.37.0
- **Container Runtime:** containerd
- **Driver:** Docker
- **Cluster Nodes:** 1 Control Plane and 1 Worker
- **Namespace:** ivolve

## Implementation

### 1. Create a Kubernetes Namespace

Create a namespace named `ivolve` to isolate application resources.

Command: `kubectl create namespace ivolve`

Verification: `kubectl get namespaces`

### 2. Create a ResourceQuota

Create a ResourceQuota named `ivolve-quota` with a maximum limit of three Pods.

Command: `kubectl create quota ivolve-quota --hard=pods=3 -n ivolve`

Verification: `kubectl describe quota ivolve-quota -n ivolve`

Expected configuration:

- Namespace: ivolve
- Resource: pods
- Used: 0
- Hard limit: 3

### 3. Create Three Pods

Create three NGINX Pods in the `ivolve` namespace.

- `kubectl run pod1 --image=nginx --restart=Never -n ivolve`
- `kubectl run pod2 --image=nginx --restart=Never -n ivolve`
- `kubectl run pod3 --image=nginx --restart=Never -n ivolve`

Verify the Pods using `kubectl get pods -n ivolve`.

Check quota usage using `kubectl describe quota ivolve-quota -n ivolve`.

Expected quota usage: 3/3 Pods.

### 4. Test ResourceQuota Enforcement

Attempt to create a fourth Pod.

Command: `kubectl run pod4 --image=nginx --restart=Never -n ivolve`

Kubernetes rejects the request with a Forbidden error.

Observed error:

`exceeded quota: ivolve-quota, requested: pods=1, used: pods=3, limited: pods=3`

## Results

- Successfully created the `ivolve` namespace.
- Created the `ivolve-quota` ResourceQuota with a maximum of three Pods.
- Successfully created three NGINX Pods.
- Verified that quota usage reached 3/3.
- Confirmed that Kubernetes rejected the fourth Pod because the configured quota limit was reached.

## Key Concepts

- **Namespace:** Provides logical isolation for Kubernetes resources.
- **ResourceQuota:** Restricts resource consumption within a namespace.
- **Hard Limit:** Defines the maximum allowed quantity of a resource.
- **Quota Enforcement:** Kubernetes rejects requests that would exceed the configured quota.
