Task Manager with Priority & Reminders

A secure, user-friendly task management application built with **Spring Boot**, **Thymeleaf**, and **Spring Security**. Designed for productivity with priority tracking, deadline reminders, and email recovery.

 **Your journey to organized tasks begins here.**

---

##  Design & Branding

This app uses a **bold and professional** color palette:
- **Navy Blue** (`#001F5B`) – Trust, structure, and clarity
- **Mustard Yellow** (`#FFDB00`) – Energy, emphasis, and action
- **Clean White** (`#FFFFFF`) – Simplicity and focus

Every interaction is designed to be **intuitive**, **accessible**, and **visually clean**.

---

## Features

| Feature | Description |
|--------|-------------|
|  **User Registration & Login** | Secure authentication with form validation |
|  **Password Recovery** | Reset via email with secure token (24-hour expiry) |
|  **Task Creation** | Add tasks with title, description, deadline, and priority |
|  **Priority Levels** | High, Medium, Low — visually highlighted |
|  **Deadline Tracking** | Set and update deadlines with datetime picker |
|  **Mark Complete/Incomplete** | Toggle task status instantly |
|  **Email Reminders** | Automated reminders sent 24 hours before deadline |
| **CRUD Operations** | Edit deadlines, mark complete, delete tasks |
|  **Responsive Design** | Works on desktop and mobile |

---

##  Tech Stack

| Layer | Technology |
|------|------------|
| **Backend** | Spring Boot 3, Java 17, Spring Security, Spring Data JPA |
| **Frontend** | Thymeleaf, HTML5, CSS3 |
| **Database** | H2 (in-memory, for dev), PostgreSQL-ready |
| **Email** | JavaMailSender (SMTP with Gmail support) |
| **Scheduling** | `@Scheduled` for background reminder jobs |
| **Security** | BCrypt password hashing, CSRF protection, role-based access |

---

Getting Started

### 1. Clone the Repository

git clone https://github.com/Mxolisi2285/tasktracker.git
cd tasktracker


### 2. Configure Application
Copy the example config:

cp  src/main/resources/application.properties

Edit application.properties with your settings:


# Database (H2 for dev)
spring.datasource.url=jdbc:h2:mem:taskdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Email (Gmail example)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your.email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Server
server.port=8080

Note: Use an App Password if using Gmail with 2FA. 

Run the Application

Using IDE:
Import as Maven project
Run TasktrackerApplication.java
Then visit:
http://localhost:8080


Email Reminder System
Runs every hour (for testing) via @Scheduled
Sends reminders for tasks due in the next 24 hours
Uses Thymeleaf email templates:
src/main/resources/templates/email/taskReminder.html
src/main/resources/templates/email/taskUrgentReminder.html

Testing the App
Register a new user
Log in
Create a task with a deadline in 2 minutes
Check email – you’ll receive a reminder
Mark task complete or delete it

Project Structure

src/
├── main/
│   ├── java/
│   │   └── za.ac.tasktracker.tasktracker/
│   │       ├── controller/
│   │       ├── model/
│   │       ├── repository/
│   │       ├── service/
│   │       └── config/
│   ├── resources/
│   │   ├── templates/
│   │   │   ├── login.html
│   │   │   ├── register.html
│   │   │   ├── dashboard.html
│   │   │   └── email/
│   │   ├── static/
│   │   │   └── css/
│   │   │       ├── login.css
│   │   │       └── dashboard.css
│   │   └── application.properties


Future Enhancements
Dark mode toggle
Task filtering by priority/status
Calendar view
Mobile PWA support
SMS reminders (Twilio)
Admin dashboard




