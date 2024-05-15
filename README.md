 # Java Classroom and Lab Booking System - README

## Introduction

This Java application is a Classroom & Lab booking system built using JavaFX for the user interface and MySQL for database management. The system allows users to log in, view current bookings, book new rooms, and change passwords. There are two types of rooms available for booking: labs and classrooms.

![plot]([http://url/to/img.png](https://github.com/DhwaniBhavankarKarthikeyan/BookIT_Java-Classroom-Lab-Booking/blob/main/overview.png))

## Features

* User Authentication: Users can log in using their credentials stored in the MySQL database.
* Dashboard: Upon successful login, users are taken to a dashboard where they can navigate between their current bookings, profile, and room booking functionalities.
* View Current Bookings: Users can view their current lab and classroom bookings.
* Book Rooms: Users can check availability and book labs or classrooms for specified times.
* Change Password: Users can change their password directly from the application.

## Prerequisites
* Java Development Kit (JDK) 8 or higher
* JavaFX SDK
* MySQL Database

## Database Setup
1. Create a MySQL database named JavaProject.

2. Create a Credentials table to store user credentials:
     CREATE TABLE Credentials (
      uname VARCHAR(50) PRIMARY KEY,
      pwd VARCHAR(50)
  );
   
3. Create LabsCapacity and ClassroomCapacity tables to store room booking details:
     CREATE TABLE LabsCapacity (
      LabName VARCHAR(50) PRIMARY KEY,
      start_time DATETIME,
      end_time DATETIME,
      booked_by VARCHAR(50)
  );
  
  CREATE TABLE ClassroomCapacity (
      classroomid VARCHAR(50) PRIMARY KEY,
      start_time DATETIME,
      end_time DATETIME,
      booked_by VARCHAR(50)
  );

4. Populate the tables with initial data as needed.

## Configuration
* Update the database connection parameters in the Main class:
  private static final String DB_URL = "jdbc:mysql://localhost:3306/JavaProject";
  private static final String DB_USER = "root";
  private static final String DB_PASSWORD = "your_password";

* Replace your_password with the actual MySQL root password.

## Running the Application
1. Compile the Java application: javac -cp .:path/to/javafx-sdk/lib/* application/Main.java
2. Run the application: java -cp .:path/to/javafx-sdk/lib/* application.Main

## Class Overview

**Main Class**
* start: Initializes the login screen.
* isValidUser: Authenticates user credentials against the database.
* navigateToDashboard: Navigates to the main dashboard after successful login.
* showChangePasswordDialog: Displays a dialog for changing the user's password.
* changePassword: Updates the user's password in the database.
* createCurrentBookingsScene: Generates the scene for displaying current bookings.
* handleDeleteBooking: Handles the deletion of a booking.
* getBookingsForUserAndRoomType: Retrieves bookings for a specific user and room type.
* createBookingScene: Generates the scene for booking rooms.
* updateLabCapacity: Updates the lab capacity table with booking details.
* updateClassroomCapacity: Updates the classroom capacity table with booking details.
* getAvailableLabs: Retrieves available labs for a given time slot.
* getAvailableClassrooms: Retrieves available classrooms for a given time slot.
* createTimeComboBox: Creates a combo box for time selection.
* refreshTab: Refreshes the current bookings tab.
* showAlert: Displays alert messages.

**Custom Exception**
* InvalidTimeException: Custom exception thrown when the end time is before the start time during booking

## Notes
* Ensure that the JavaFX SDK is correctly set up in your environment.
* Modify the database connection details as per your local MySQL setup.
* The DB_PASSWORD should be kept secure and not hardcoded in a real-world application.

## License
This project is open-source and available under the MIT License. Feel free to modify and distribute it as needed.
