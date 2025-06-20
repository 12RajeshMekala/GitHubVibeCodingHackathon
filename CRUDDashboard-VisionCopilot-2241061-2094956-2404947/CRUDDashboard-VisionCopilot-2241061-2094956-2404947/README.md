# CRUDDashboard-VisionCopilot-2241061-2094956-2404947
# Team Name: VisionCopilot

## Project Apex: Intelligent Inventory Hub

A modern inventory management solution with a beautiful dashboard that provides real-time insights into your stock levels.

## Project Overview

Project Apex is a full-stack application built with:
- **Frontend**: React.js with Material UI
- **Backend**: Spring Boot (Java) with JPA/Hibernate
- **Database**: H2 Database (in-memory for demonstration)

The application provides a responsive and intuitive dashboard for store managers to manage their inventory with ease.

## Features

### Core Features
- **Dashboard overview** with key metrics (total items, low stock, out of stock)
- **Real-time inventory table** with sorting and filtering capabilities
- **Full CRUD operations** for inventory items
- **Search functionality** to quickly find items by name, SKU, or category
- **Visual indicators** for low and out-of-stock items

### Additional Features
- **Activity logging** to track recent changes to inventory
- **Data visualization** with a category distribution chart
- **Responsive design** that works on desktop and mobile devices

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven
- Node.js and npm
- Git

### Backend Setup

1. Navigate to the backend directory:
```
cd backend
```

2. Build the project using Maven:
```
mvn clean install
```

3. Run the Spring Boot application:
```
mvn spring-boot:run
```

The backend will be available at http://localhost:8080. You can access the H2 console at http://localhost:8080/h2-console (use jdbc:h2:mem:inventorydb as the JDBC URL, sa as the username, and password as the password).

### Frontend Setup

1. Navigate to the frontend directory:
```
cd frontend
```

2. Install dependencies:
```
npm install
```

3. Start the development server:
```
npm start
```

The frontend will be available at http://localhost:3000.

## API Endpoints

| Method | URL                         | Description                   |
|--------|----------------------------|-------------------------------|
| GET    | /api/inventory/dashboard    | Get dashboard statistics      |
| GET    | /api/inventory              | Get all inventory items       |
| GET    | /api/inventory/{id}         | Get item by ID                |
| GET    | /api/inventory/search?query={query} | Search inventory items |
| POST   | /api/inventory              | Create a new item             |
| PUT    | /api/inventory/{id}         | Update an existing item       |
| DELETE | /api/inventory/{id}         | Delete an item                |

## Default Data

The application is preloaded with sample data for demonstration purposes. The default credentials for accessing the system:

- **Username**: admin@inventoryhub.com
- **Password**: password

## Future Enhancements

- User authentication and multi-user support
- Inventory history and reporting
- Barcode scanning capabilities
- Email notifications for low stock items
- Export functionality (CSV, Excel)