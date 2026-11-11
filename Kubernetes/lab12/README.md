# Lab 12: Managing Configuration and Sensitive Data with ConfigMaps and Secrets

## Objective

Separate non-sensitive application configuration from sensitive database credentials using Kubernetes ConfigMaps and Secrets.

Both resources are created in the `ivolve` namespace.

## Environment

- Kubernetes: Minikube v1.39.0
- Kubernetes Version: v1.37.0
- Namespace: ivolve
- Container Runtime: containerd

## 1. Create the ConfigMap

A ConfigMap stores non-sensitive configuration data.

Resource name: `ivolve-cm`

Configuration values:

| Key | Value |
|---|---|
| DB_HOST | mysql-headless.ivolve.svc.cluster.local |
| DB_USER | ivolveuser |

The DB_HOST value assumes a future MySQL Headless Service named `mysql-headless` in the `ivolve` namespace. The MySQL StatefulSet and Service are not deployed in this lab.

Manifest: `configmap.yaml`

Apply:

`kubectl apply -f Kubernetes/lab12/configmap.yaml`

Verify:

`kubectl describe configmap ivolve-cm -n ivolve`

## 2. Create the Secret

A Kubernetes Secret stores sensitive data.

Resource name: `ivolve-secret`

Secret keys:

- MYSQL_ROOT_PASSWORD
- DB_PASSWORD

The values were Base64-encoded before being added to the YAML manifest under `data`.

Manifest: `secret.yaml`

Apply:

`kubectl apply -f Kubernetes/lab12/secret.yaml`

Verify:

`kubectl describe secret ivolve-secret -n ivolve`

The describe command confirms the keys and data sizes without displaying the password values.

## 3. Verify the Resources

Run:

- `kubectl get configmaps -n ivolve`
- `kubectl get secrets -n ivolve`
- `kubectl describe configmap ivolve-cm -n ivolve`
- `kubectl describe secret ivolve-secret -n ivolve`

Expected resources:

- ConfigMap `ivolve-cm` with 2 data entries.
- Secret `ivolve-secret` of type Opaque with 2 data entries.

## 4. Key Concepts

- **ConfigMap:** Stores non-sensitive configuration separately from application code.
- **Secret:** Stores sensitive values such as passwords and tokens.
- **Base64 Encoding:** Represents binary or text data as Base64. It is encoding, not encryption.
- **Opaque:** The default general-purpose Secret type.
- **Namespace:** Keeps the configuration resources scoped to the `ivolve` namespace.

## Notes

- Kubernetes resource names must follow lowercase DNS naming rules. Therefore, `ivolve-cm` and `ivolve-secret` are used instead of names containing uppercase letters.
- The database credentials used in this lab are demonstration values only.
- Never commit real passwords or production credentials to a public Git repository.
- The MySQL StatefulSet and Headless Service are outside the scope of this lab.
