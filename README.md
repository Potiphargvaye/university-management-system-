# University Management System (Dashboard Application)

### 🎓 Academic Profile
* **Student Name:** Potiphar Gaye Vaye
* **Registration Number:** 24167/2024
* **Course Title:** Advanced Programming
* **Assigned Lecturer:** [Insert Lecturer's Name Here]

---

## 📌 Project Overview
This project is a comprehensive desktop **University Management System Dashboard** built using a structured Model-View-Controller (MVC) architecture. It is designed to bridge full-stack database management with a responsive graphical user interface (GUI) to handle essential university administrative modules securely.

The application communicates dynamically with a local MySQL server via JDBC (Java Database Connectivity), ensuring all user transactions are verified and synced with live database structures in real time.

---

## 🛠️ Core Architecture & Features

### 1. 🏢 Department Management
* Create and save new institutional departments.
* Live-tracking system displaying the total number of staff and academic lecturers assigned to each specific department using left-joins.
* Safe structural row removal with verification handling.

### 2. 📚 Course Catalog
* Interactive course structural allocation mapping codes, titles, and credit weights.
* **Dynamic Department Allocation:** Includes a synchronized drop-down selector mapping descriptive department titles straight to internal database ID keys.
* Real-time course tracking that dynamically calculates and updates active student enrollment metrics across columns.

### 3. 📝 Enrollment Manager
* Complete registration interface linking unique student registration codes directly to verified course structures.
* Custom, searchable data layout filtering through the system enrollment matrix automatically based on real-time text input keywords.

### 4. 🎛️ System Validation & Reliability
* **Data Integrity Checks:** Preventative input boundaries that flag and intercept empty text fields or duplicate items with clear warning dialog popups before hitting the server.
* **Robust Exception Handling:** Full `try-catch` implementations isolating SQL errors and providing diagnostic popups instead of application failures.

---

## 💻 Tech Stack
* **Frontend:** Java Swing (GUI Windowing Layout Components)
* **Backend:** Java Standard Edition (Advanced JDBC Layer)
* **Database Management:** MySQL (Relational Tables with Foreign Key Constraints)
* **Environment Tooling:** Eclipse IDE, Git Version Control

---

## 🚀 Database Setup & Execution
1. The full relational script required to construct the tables, columns, and key relations is available directly in the root file directory under **`university_db.sql`**.
2. Import the script contents inside your relational database controller manager (Laragon, XAMPP, or phpMyAdmin).
3. Ensure your local MySQL server is active, configure your local credentials inside `DatabaseConnection.java`, and run `DashboardFrame.java` to start the application.
