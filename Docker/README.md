# Lab 5: Docker Image Security Scanning with Trivy

## Overview

This lab demonstrates how to scan a Docker image for security vulnerabilities using **Trivy** and generate reports to review the scan results.

## Objectives

- Build a Docker image for the sample Node.js application.
- Scan the image for known security vulnerabilities using Trivy.
- Understand vulnerability severity levels.
- Generate HTML and JSON security reports.
- Review scan results and identify potential security risks.

## Project Structure

```text
Docker/lab-five/
├── Dockerfile
├── app.js
├── package.json
├── html.tpl
├── trivy-report.html
└── trivy-report.json
```

*Note: The report filenames above describe the expected outputs; use the actual filenames present in the project.*

## Prerequisites

- Docker installed and running.
- Trivy installed and available in the terminal.
- Git, if you want to commit and push the lab to GitHub.

Verify the tools:

```bash
docker --version
trivy --version
```

## Step 1: Build the Docker Image

Navigate to the lab directory:

```bash
cd Docker/lab-five
```

Build the image:

```bash
docker build -t trivy-lab:v1 .
```

Verify that the image exists:

```bash
docker images
```

## Step 2: Scan the Image with Trivy

Run a vulnerability scan:

```bash
trivy image trivy-lab:v1
```

Trivy checks the image for known vulnerabilities in operating-system packages and supported application dependencies.

Review the reported severity levels:

- **CRITICAL** — vulnerabilities requiring urgent investigation.
- **HIGH** — serious vulnerabilities that should be prioritized.
- **MEDIUM** — vulnerabilities that may require remediation.
- **LOW** — lower-severity findings.
- **UNKNOWN** — findings whose severity is not classified.

The findings depend on the image contents and the vulnerability database available at scan time.

## Step 3: Generate a JSON Report

Generate a machine-readable report:

```bash
trivy image --format json --output trivy-report.json trivy-lab:v1
```

The JSON report can be used for further analysis, automation, or integration with security tools.

## Step 4: Generate an HTML Report

If `html.tpl` is the provided Trivy HTML template, generate a readable report with:

```bash
trivy image \
  --format template \
  --template "@html.tpl" \
  --output trivy-report.html \
  trivy-lab:v1
```

Open `trivy-report.html` in a web browser to review the scan results.

## Step 5: Review the Results

Check the reports for:

- Total vulnerabilities detected.
- Severity distribution.
- Affected packages and installed versions.
- Available fixed versions, when provided.
- Recommended package or base-image updates.

Prioritize remediation based on severity, exploitability, and the application's exposure. Rebuild and rescan the image after making security improvements.

## Expected Results

By completing this lab, you should be able to:

- Build and identify a Docker image.
- Run a Trivy image vulnerability scan.
- Interpret vulnerability severity levels.
- Generate and inspect HTML and JSON reports.
- Understand how image scanning supports container security.

**Note:** Vulnerability counts vary with the image version and Trivy's vulnerability database. Do not assume that a scan has zero vulnerabilities unless the actual report confirms it.

## References

- [Trivy Documentation](https://trivy.dev/)
- [Trivy Container Image Scanning](https://trivy.dev/docs/latest/guide/target/container_image/)
- [Trivy Reporting](https://trivy.dev/docs/latest/configuration/reporting/)
