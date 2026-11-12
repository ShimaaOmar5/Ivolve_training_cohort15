# Lab 20: Jenkins Role-Based Authorization (RBAC)

## Objective
Implement Role-Based Access Control (RBAC) in Jenkins to manage user permissions and restrict access based on assigned roles.

## Environment
- **CI/CD Tool:** Jenkins
- **Jenkins URL:** http://localhost:8080/
- **Plugin:** Role-based Authorization Strategy

## Implementation

### 1. Install the Plugin
Installed the **Role-based Authorization Strategy** plugin from Jenkins Plugin Manager.

### 2. Create Jenkins Users
Created two Jenkins users:
- `user1`: Administrator account.
- `user2`: Read-only account.

### 3. Enable Role-Based Authorization
Navigated to **Manage Jenkins → Security → Authorization** and selected **Role-Based Strategy**.

### 4. Configure Global Roles

Created the following roles:

| Role | Permissions | Description |
|---|---|---|
| admin | Overall: Administer | Full Jenkins administrative access |
| readonly | Overall: Read, Job: Read, View: Read | View-only access to Jenkins jobs and dashboards |

### 5. Assign Roles

Assigned roles to the Jenkins users:

| User | Assigned Role |
|---|---|
| user1 | admin |
| user2 | readonly |

### 6. Verify Permissions

**Administrator – user1**
- Can access Manage Jenkins.
- Has full administrative privileges.
- Can manage Jenkins configurations and jobs.

**Read-only – user2**
- Can access Jenkins dashboards.
- Can view jobs and their information.
- Cannot configure, build, or administer Jenkins.

## Screenshots

| Screenshot | Description |
|---|---|
| 01-jenkins-users.png | Created Jenkins users |
| 02-global-roles.png | Configured admin and readonly roles |
| 03-role-assignments.png | Assigned roles to users |
| 04-user1-admin-access.png | Verified administrator permissions |
| 05-user2-readonly-access.png | Verified read-only restrictions |

## Result
Successfully implemented Jenkins Role-Based Authorization using the Role-based Authorization Strategy plugin. Two users were configured with different permission levels, demonstrating access control and the principle of least privilege.

## Key Concepts Learned
- Jenkins User Management
- Role-Based Access Control (RBAC)
- Global Roles and Permissions
- Access Restriction and Least Privilege
- Authorization Strategy Configuration
