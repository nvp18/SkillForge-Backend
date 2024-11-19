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


### APIs
The application has RESTApi's through which we can communicate with the application. They are as follows: 

# API Endpoints

| API Endpoint | Request Type | Role |
|--------------|--------------|------|
| `/api/admin/getAllConcerns` | GET | ADMIN |
| `/api/admin/replyToConcern/{concernId}` | POST | ADMIN |
| `/api/admin/postAnnouncement/{courseId}` | POST | ADMIN |
| `/api/admin/getAllAnnouncements/{courseId}` | GET | ADMIN |
| `/api/admin/editAnnouncement/{announcementId}` | PUT | ADMIN |
| `/api/admin/deleteAnnouncement/{announcementId}` | DELETE | ADMIN |
| `/api/admin/getAnnouncement/{announcementId}` | GET | ADMIN |
| `/api/course/createCourse` | POST | ADMIN |
| `/api/course/updateCourse/{courseId}` | PUT | ADMIN |
| `/api/course/deleteCourse/{courseId}` | DELETE | ADMIN |
| `/api/course/getAllCourses` | GET | ADMIN |
| `/api/course/uploadCourseModule/{courseId}` | POST | ADMIN |
| `/api/course/getCourseModules/{courseId}` | GET | ADMIN, EMPLOYEE |
| `/api/course/getModuleContent/{moduleId}` | GET | ADMIN, EMPLOYEE |
| `/api/course/deleteCourseModule/{moduleId}` | DELETE | ADMIN |
| `/api/course/updateCourseModule/{moduleId}` | PUT | ADMIN |
| `/api/course/getCourseDetails/{courseID}` | GET | ADMIN, EMPLOYEE |
| `/api/course/assignCourseToEmployee/{courseId}/{employeeId}` | POST | ADMIN |
| `/api/course/deassignCourseToEmployee/{courseId}/{employeeId}` | DELETE | ADMIN |
| `/api/course/createQuiz/{courseId}` | POST | ADMIN |
| `/api/course/deleteQuiz/{quizId}` | DELETE | ADMIN |
| `/api/course/deleteQuestion/{questionId}` | DELETE | ADMIN |
| `/api/course/getQuiz/{courseId}` | GET | ADMIN, EMPLOYEE |
| `/api/course/getQuestions/{quizId}` | GET | ADMIN, EMPLOYEE |
| `/api/course/postDiscussion/{courseId}` | POST | ADMIN, EMPLOYEE |
| `/api/course/replyToDiscussion/{discussionId}` | POST | ADMIN, EMPLOYEE |
| `/api/course/getAllDiscussions/{courseId}` | GET | ADMIN, EMPLOYEE |
| `/api/course/deleteDiscussion/{discussionId}` | DELETE | ADMIN |
| `/api/course/getAllCoursesOfEmployee/{employeeId}` | GET | ADMIN |
| `/api/course/submitQuiz/{courseId}` | POST | ADMIN, EMPLOYEE |
| `/api/employee/getAllEmployeeCourses` | GET | EMPLOYEE |
| `/api/employee/getConcerns` | GET | EMPLOYEE |
| `/api/employee/raiseConcern` | POST | EMPLOYEE |
| `/api/employee/replyToConcern/{concernId}` | POST | EMPLOYEE |
| `/api/employee/getAllAnnouncements/{courseId}` | GET | EMPLOYEE |
| `/api/employee/getAnnouncement/{announcementId}` | GET | EMPLOYEE |
| `/api/employee/updateModuleCompleted/{moduleId}/{courseId}` | POST | EMPLOYEE |
| `/api/employee/getQuiz/{courseId}` | GET | EMPLOYEE |
| `/api/employee/startCourse/{courseId}` | POST | EMPLOYEE |
| `/api/employee/viewProgress/{employeeId}/{courseId}` | GET | ADMIN, EMPLOYEE |
| `/api/employee/getModuleStatus/{courseId}` | GET | EMPLOYEE |
| `/api/employee/getCourseStatus/{courseId}` | GET | EMPLOYEE |
| `/api/user/login` | POST | ADMIN, EMPLOYEE |
| `/api/user/createUser` | POST | ADMIN |
| `/api/user/viewProfile` | GET | ADMIN, EMPLOYEE |
| `/api/user/updateProfile` | PUT | ADMIN, EMPLOYEE |
| `/api/user/changePassword` | PUT | ADMIN, EMPLOYEE |
| `/api/user/getAllEmployees` | GET | ADMIN |

