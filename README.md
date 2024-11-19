# SkillForge Backend

## Overview

SkillForge is a Spring Boot designed to streamline role-based course management. The platform supports two primary user roles:

- **Admin**: Manages courses, users, modules, discussions, announcements, quizzes, and employee concerns.
- **Employee**: Interacts with assigned courses, participates in discussions, attempts quizzes, and communicates concerns.

## Features

### Admin Features

- **User Management**:
  - Create, view, update, and delete users.
  - Assign and deassign courses to users.
  
- **Course Management**:
  - Create, view, update, and delete courses.
  
- **Module Management**:
  - Add, update, and delete modules within courses.
  - View course content structure.
  
- **Discussion Management**:
  - Create, update, and delete discussions.
  - Monitor and manage discussion threads.
  
- **Announcement Management**:
  - Post announcements for courses.
  - Manage existing announcements.
  
- **Quiz Management**:
  - Create quizzes with questions.
  - Update and delete quizzes.
  
- **Concern Management**:
  - View and reply to employee concerns.

### Employee Features

- **Dashboard**:
  - View all assigned courses.
  
- **Course Interaction**:
  - Start and progress through assigned courses.
  - Read and mark modules as completed.
  
- **Discussion Participation**:
  - View and participate in course discussions.
  
- **Quizzes**:
  - Attempt quizzes assigned to their courses.
  
- **Announcements**:
  - View course-specific announcements.
  
- **Concerns**:
  - Post concerns to the Admin.
  - Reply to Admin responses on their concerns.

## Project Setup

### Prerequisites

Ensure the following are installed:

- **Java** (v17 or later)
- **Maven** 

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/SkillForge-Frontend.git
   
2. Navigate to the project repository:
    ```bash
    cd SkillForge-Backend

3. Install Dependencies:
    ```bash 
    mvn clean install


### Running the Application

1. Start the Development Server:
    ```bash
    mvn spring-boot:start

2. To build the project for Production:
    ```bash
     mvn clean install

3. To run the test cases
    ```bash 
    mvn test

4. To generate test report
    ```bash 
    mvn verify


### Routing
The application uses React Router for navigation. Key routes include:

- /login: User login page.
- /dashboard: Dashboard for both Admins and Employees.
- /admin/create-course: Admin creates a new course.
- /course/:courseId: Course-specific details for both Admins and Employees.
- /course/:courseId/modules: List of course modules.
- /course/:courseId/quiz: Quiz management.
- /course/:courseId/announcements: Course announcements.
- /course/:courseId/discussions: Discussion threads.
- /concerns: Manage and respond to concerns.
