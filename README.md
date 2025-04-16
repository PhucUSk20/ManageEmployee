
# 🤖 MANAGE EMPLOYEE – Worker Management, Project Management, Attendance & Messaging System

A mobile and web-based system designed to streamline workforce management within a company. It integrates employee and project tracking, QR code-based attendance, and real-time messaging, enhancing productivity and collaboration between workers and supervisors.

---

## 📌 Features

### Supervisor
- View the employee list, see detailed information of each employee, and generate an ID code for new employees (this ID will be given to employees as a key to create an account).
- Generate QR codes for attendance (QR code is generated based on a Private Key and the current date. You can also define a valid location for scanning — the employee must be within a 50-meter radius of the location).
- Directly access the database to check attendance history.
- View the list of ongoing projects, create new projects, and assign employees to projects.
- View a management calendar showing the deadlines of all current projects.
- Manage employee leave applications.
- Grant allowance permissions and create new types of allowances.
- View task lists, create new tasks for projects, and assign them to employees (employees must belong to the project).

### Employee
- Update personal information and profile picture.
- View assigned tasks, get task details, and update task progress.
- Scan QR codes for attendance.
- View attendance history in a calendar overview.
- Use a management calendar showing task deadlines.
- Chat with other project members.
- Submit leave applications, view leave history, and check their approval status.
- View list of available allowances.
- View the progress of projects they are participating in.
## 📂 Table of Contents

- [1. Supervisor Interface](#1-supervisor-interface)  
- [2. Employee Interface](#2-employee-interface)  
- [3. Technologies Used](#3-technologies-used)  

---

## 1. Supervisor Interface

### 1.1 Worker Details Fragment  
![Worker Details](https://i.imgur.com/o4aOS4N.png)  
![Worker Details 2](https://i.imgur.com/8lEaw4i.png)  
![Worker Details 3](https://i.imgur.com/shtSHiH.png)

### 1.2 Generate QR Code Fragment  
![QR Code](https://i.imgur.com/Mw4L6LX.png)  
![QR Code 2](https://i.imgur.com/F2gkKN1.png)  
![QR Code 3](https://i.imgur.com/SJfc6hR.png)

### 1.3 Attendance Database Fragment  
![Attendance](https://i.imgur.com/2HQtcqd.png)

### 1.4 Project Details Fragment  
![Project Details](https://i.imgur.com/GiVU0cC.png)  
![Project Details 2](https://i.imgur.com/wQTKUBU.png)

### 1.5 Calendar Fragment  
![Calendar](https://i.imgur.com/cT0h6iz.png)

### 1.6 Leave Management Fragment  
![Leave 1](https://i.imgur.com/n7nZdoo.png)  
![Leave 2](https://i.imgur.com/3QUJCmD.png)

### 1.7 Allowance Management Fragment  
![Allowance 1](https://i.imgur.com/Q8J6FqQ.png)  
![Allowance 2](https://i.imgur.com/eJt0LVQ.png)

### 1.8 Task Fragment  
![Task 1](https://i.imgur.com/PQQ1vS8.png)  
![Task 2](https://i.imgur.com/xvaeuQG.png)

---

## 2. Employee Interface

### 2.1 Update Info Fragment  
![Update Info](https://i.imgur.com/AvIXyZh.png)

### 2.2 My Tasks Fragment  
![My Task](https://i.imgur.com/up9DKHI.png)

### 2.3 QR Code Scanner Fragment  
![Scanner](https://i.imgur.com/9s4YMjG.png)

### 2.4 Attendance History Fragment  
![Attendance History](https://i.imgur.com/REuf0Te.png)

### 2.5 Calendar Fragment  
![Calendar](https://i.imgur.com/bsuOWmN.png)

### 2.6 Chat Fragment  
![Chat 1](https://i.imgur.com/mh88U8i.png)  
![Chat 2](https://i.imgur.com/LbxPXFV.png)  
![Chat 3](https://i.imgur.com/5Nxksiq.png)

### 2.7 Apply for Leave Fragment  
![Leave](https://i.imgur.com/plSuZcr.png)

### 2.8 Allowance Fragment  
![Allowance](https://i.imgur.com/0O2Gap0.png)

### 2.9 Current Project Fragment  
![Current Project](https://i.imgur.com/jeOOAsO.png)

---

## ✅ Technologies Used

- **Mobile Development**: Android (Java)  
- **Database & Backend**: Firebase Realtime Database, Firebase Authentication  
- **Communication**: Firebase Cloud Messaging (FCM)  
- **QR Code**: ZXing (QR Code Generator/Scanner)  
- **Realtime Features**: Firebase & Socket  
- **Calendar Visualization**: Custom GitHub-style Calendar  
- **UI/UX**: Material Design, Custom Icons


