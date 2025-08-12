# Student Management System (Java Console App)

A comprehensive, console-based Student Management System built with Java to handle student registration, course advising, and data persistence. This application provides a secure, rule-based environment for managing academic data through a simple command-line interface.

## Key Features

* **Secure Admin Login:** Validates admin credentials from a `user.txt` file before granting access to the main panel.
* **Robust Student Management:**
    * Add new students with built-in duplicate ID prevention.
    * View a complete roster of all students, automatically sorted by ID.
    * Search for and view detailed profiles of individual students.
* **Automatic Password Generation:** Creates a unique and secure 8-character password for each new student using Java's `SecureRandom`.
* **Advanced Course Advising:** A rule-based system that allows students to enroll in courses while enforcing two key business rules:
    1.  A strict 14-credit limit per student.
    2.  Prevention of duplicate course registration.
* **Persistent File I/O:** All application data (users, students, courses) is reliably saved and managed in `.txt` files using Java's `RandomAccessFile`.

## Technology Stack

* **Language:** Java
* **Core APIs:** Java I/O (`RandomAccessFile`), Java Collections Framework

## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine.

### Prerequisites

* Java Development Kit (JDK) 8 or higher installed.

### How to Run

1.  **Clone the repository:**
    ```sh
    git clone [https://github.com/your-username/your-repository-name.git](https://github.com/your-username/your-repository-name.git)
    cd your-repository-name
    ```

2.  **Create the necessary data files** in the root directory of the project:
    * `user.txt`: Add a line for the admin credentials.
        ```
        admin,admin123
        ```
    * `courselist.txt`: Add a few sample courses.
        ```
        CSE115,3
        CSE115L,1
        ENG101,3
        MAT116,3
        ```

3.  **Compile and Run** the application from the terminal:
    * Navigate into the `src` folder (or wherever your `.java` files are).
    * Compile the Java files:
        ```sh
        javac Main.java Student.java
        ```
    * Run the main class:
        ```sh
        java Main
        ```
    * Alternatively, you can open the project in an IDE like IntelliJ IDEA or Eclipse and run the `Main.java` file.

## File Structure

* `user.txt`: Stores admin login credentials.
* `students.txt`: Stores all registered student details and their generated passwords. Created automatically.
* `courselist.txt`: Contains the list of all available courses and their corresponding credits.
* `student_courses.txt`: Stores the courses taken by each student. Created and updated automatically.
