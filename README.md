# Shopping Cart API

Basket API is a Spring Boot application that provides functionality for managing shopping carts and applying promotions.

## Project Structure

The project is structured as follows:

- `src/main/java/com/example/basket`: Contains the Java source code for the application.
  - `controller`: Controllers responsible for handling HTTP requests.
  - `entity`: Entity classes representing data objects.
  - `exception`: Custom exception classes.
  - `request`: Request classes for API endpoints.
  - `response`: Response classes for API endpoints.
  - `service`: Service classes for business logic.
  - `Application.java`: The main application class.

- `src/test/java/com/example/basket`: Contains unit tests for the application.
  - `controller`: Controller test classes.
  - `service`: Service test classes.

- `pom.xml`: Maven configuration file for managing project dependencies and build settings.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) 11 or later
- Maven (for building and managing dependencies)

## Building the Project

To build the project, follow these steps:

1. Clone the repository to your local machine:

   ```bash
   git clone https://github.com/berkinozturk/shopping-cart-API.git

2. Build the Project
   
   ```bash
   mvn clean install
   
3. Running the Application
   
   ```bash
   mvn spring-boot:run

The application will start, and you can access the API at http://localhost:8080.


  
   
