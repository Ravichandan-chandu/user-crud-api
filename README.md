# 🚀 User CRUD API – DevOps Final Project

This project is a comprehensive demonstration of the **end-to-end DevOps lifecycle**, developed as part of the **DSTI DevOps Course**.  
It showcases how a production-ready Spring Boot application can be built, tested, automated, containerized, provisioned using Infrastructure as Code, and deployed on Kubernetes.

The focus of this project is not only application development, but also **automation, reproducibility, and reliability** across the full DevOps toolchain.

---

## 📖 Table of Contents

- Features  
- Tech Stack  
- Infrastructure as Code  
- Containerization (Docker)  
- Kubernetes Orchestration  
- CI/CD Pipeline  
- Testing  
- Screenshots  
- Author & AI Disclosure  

---

## ✨ Features

- **Core Application**: Java Spring Boot REST API with full CRUD functionality  
- **Database**: Persistent MySQL storage  
- **Health Checks**: Dedicated endpoint to validate application and database connectivity  
- **CI/CD**: Automated build and test pipeline using GitHub Actions  
- **Infrastructure as Code**: Fully automated VM provisioning with Vagrant & Ansible  
- **Containerization**: Dockerized application for portability  
- **Orchestration**: Kubernetes deployment using Minikube  
- **Persistence**: Kubernetes Persistent Volumes (PV) and Persistent Volume Claims (PVC)  

---

## 🛠 Tech Stack

- **Backend**: Java 21, Spring Boot 3.4.3, Maven  
- **Database**: MySQL 8.0 (Production), H2 (Testing)  
- **Infrastructure**: Vagrant, Ansible  
- **Containerization**: Docker, Docker Compose  
- **Orchestration**: Kubernetes (Minikube)  
- **CI/CD**: GitHub Actions  

---

## 🏗 Infrastructure as Code (Vagrant & Ansible)

The application environment is provisioned inside a **Linux virtual machine** to ensure consistency across environments.

### Steps

```bash
cd iac
vagrant up
vagrant ssh




---

## 📦 Containerization (Docker Hub)

The application image is built and pushed to Docker Hub automatically via the CI/CD pipeline.
* **Docker Hub Repository**: [https://hub.docker.com/r/ravichandan/user-crud-api](https://hub.docker.com/r/ravichandan/user-crud-api)
* **Pull Command**: `docker pull ravichandan/user-crud-api:latest`

---

## ☸️ Kubernetes Orchestration (Minikube)

The application is deployed on a Kubernetes cluster with a dedicated persistence layer.

### Deployment Status
Below is the verification of the running pods and the assigned **NodePorts**:

```bash
$ kubectl get all
NAME                                   READY   STATUS    RESTARTS   AGE
pod/user-crud-api-6f88fc4568-ndvc8     1/1     Running   0          25m
pod/user-crud-mysql-747cd5fd5c-rzfq7   1/1     Running   1          14h

NAME                            TYPE       PORT(S)          AGE
service/user-crud-api-service   NodePort   8080:30080/TCP   19h
service/user-crud-mysql         NodePort   3306:30081/TCP   19h


Persistence (PVC)
To ensure data is not lost when pods restart, a Persistent Volume Claim is configured:

$ kubectl get pvc
NAME        STATUS   VOLUME                                     CAPACITY   ACCESS MODES   AGE
mysql-pvc   Bound    pvc-7c49a096-f3d8-468f-82c7-aa2fb13f94d4   1Gi        RWO            10m
🖼 Screenshots

All execution evidence is available in the screenshots/ directory, including:

Application running

CI pipeline execution

Docker container execution

Kubernetes pods, services, and PVC

VM provisioning with Vagrant & Ansible
Final Author Note
Author: Ravichandan Kodijuttu
Project Link: [Paste Your GitHub URL Here]