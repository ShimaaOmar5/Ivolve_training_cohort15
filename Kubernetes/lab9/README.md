# Lab 9: Deploy a Kubernetes Cluster Using Kubeadm

## Objective

Deploy a Kubernetes cluster using kubeadm with one control-plane node and one worker node. Configure containerd as the container runtime, install Calico for pod networking, and verify cluster health.

## Environment

- Cloud provider: AWS EC2
- Operating system: Ubuntu Server 24.04.5 LTS
- Kubernetes version: v1.37.1
- Container runtime: containerd 2.2.1
- Container network interface (CNI): Calico v3.33.0
- Nodes: 1 control-plane node and 1 worker node
- Region: eu-central-1

## Implementation

### 1. Install and configure containerd

Install containerd on both nodes, configure the systemd cgroup driver, enable the required kernel modules and networking parameters, and disable swap.

Verify:

```bash
containerd --version
systemctl is-active containerd
```

### 2. Install Kubernetes tools

Install kubeadm, kubelet, and kubectl on both nodes. Enable kubelet and hold the package versions to avoid unintended upgrades.

Verify:

```bash
kubeadm version -o short
kubelet --version
kubectl version --client
systemctl is-active kubelet
```

### 3. Initialize the control plane

Initialize the control-plane node with kubeadm and configure kubectl to use the cluster administrator kubeconfig.

The cluster uses the pod CIDR `192.168.0.0/16`.

Verify:

```bash
kubectl cluster-info
kubectl get nodes -o wide
```

### 4. Install Calico

Install the Calico operator and custom resources to provide pod networking across the cluster.

Verify:

```bash
kubectl get pods -n calico-system
kubectl get tigerastatus
```

### 5. Join the worker node

Generate a kubeadm join command on the control-plane node and execute it on the worker node. Do not store join tokens or private credentials in this repository.

Verify:

```bash
kubectl get nodes -o wide
```

### 6. Verify cluster health

Both nodes reached the `Ready` state. The control-plane components, CoreDNS, Calico, and other displayed system pods were running.

Verify:

```bash
kubectl get nodes
kubectl get pods -A
kubectl cluster-info
```

### 7. Test an application

Deploy two NGINX replicas and expose them using a ClusterIP service. Run an in-cluster HTTP request from a test pod.

Verify:

```bash
kubectl get deployment,service,pods -o wide
kubectl exec dns-test -- wget -qO- -T 10 http://nginx-test
```

The HTTP request returned the NGINX welcome page. Fully qualified Kubernetes service names also resolved successfully in DNS tests.

## Results

- Both Kubernetes nodes are `Ready`.
- containerd and kubelet are active on both nodes.
- Calico pods are running.
- CoreDNS and other displayed cluster system pods are running.
- The NGINX deployment has two ready replicas.
- The in-cluster HTTP request succeeded.

## Security Notes

- SSH private keys, administrator kubeconfig credentials, and kubeadm join tokens must not be committed.
- Kubernetes API access should not be exposed publicly without appropriate security controls.
- AWS resources should be stopped or deleted when no longer needed to avoid ongoing charges.
