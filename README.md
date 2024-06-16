# Quiz Application

This is a Java-based quiz application using MySQL for data storage.

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- IntelliJ IDEA or Eclipse
- MySQL

## Installation

### 1. Clone the Repository

```sh
git clone https://github.com/odilmode/quiz
cd https://github.com/odilmode/quiz
```
### 2. Set Up the Database
Open MySQL Workbench (or any MySQL client).
Run the following SQL script to create the database and tables:

```angular2html
CREATE DATABASE myDB;
USE myDB;

CREATE TABLE users (
    users_id VARCHAR(255) NOT NULL,
    users_password VARCHAR(255) NOT NULL,
    users_score INT,
    users_percentage INT,
    user_type ENUM('admin', 'user') DEFAULT 'user',
    PRIMARY KEY (users_id)
);

CREATE TABLE questions (
    id INT AUTO_INCREMENT,
    question VARCHAR(255) NOT NULL,
    option1 VARCHAR(255) NOT NULL,
    option2 VARCHAR(255) NOT NULL,
    option3 VARCHAR(255) NOT NULL,
    option4 VARCHAR(255) NOT NULL,
    answer VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE user_scores (
    score_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(100),
    score INT,
    percentage INT,
    attempt_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (users_id, users_password, user_type) VALUES ('admin', 'java', 'admin');

```
### 3.Open the Project in IntelliJ IDEA or Eclipse
Open IntelliJ IDEA or Eclipse.
Go to File > Open and select the cloned repository folder.If you are using Eclipse make sure to clone the repository to our working place.
Ensure the project SDK is set to JDK 11 or higher (File > Project Structure).
If JDK is lower, download and set JDK to JDK 22 version
### 4.Configure Database Connection
   Open ConnectSQL.java.
   Ensure the database connection details are correct:
```
public class ConnectSQL {
    private static final String URL = "jdbc:mysql://localhost:3306/myDB";
    private static final String USER = "root";
    private static final String PASSWORD = "mydata123";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

```
### 5. Run the Application
Navigate to Main.java.
Right-click Main.java and select Run 'Main.main()'.
Using the Application
Login Page: Log in with the admin credentials (admin, java).
Admin Panel: Manage questions and users.
User Registration: New users can sign up through the sign-up page.

### 6. Simple In Use Example
https://youtu.be/CLghJdzgfog
