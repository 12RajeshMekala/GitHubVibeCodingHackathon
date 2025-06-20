


Application Features and Usage Guide
1. Dashboard Overview
When you launch the application, you'll land on the main dashboard that displays:

Statistic Cards: Three cards at the top showing:

Total number of unique inventory items
Number of items with low stock (quantity less than 10)
Number of items that are out of stock (quantity = 0)
Search and Filter: A search bar that allows you to instantly filter items by name, SKU, or category

Inventory Table: The main table displays all your inventory items with columns for:

Name
SKU
Category
Price
Quantity
Action buttons (Edit/Delete)
Visual Indicators:

Items with quantity < 10 (but not 0) have a yellow background
Items with quantity = 0 have a red background
Activity Log Panel: Shows the last 10 actions performed on inventory items

Category Distribution Chart: A pie chart showing how many items you have in each category

2. Adding New Items
Click the "Add Item" button above the inventory table
A modal form will appear with fields for:
Name (required)
SKU (required)
Category (required, dropdown selection)
Price (required, numeric)
Quantity (required, numeric)
Image URL (optional)
Fill in the details and click "Create"
The new item will appear in the inventory table and the activity log will show the action
3. Updating Items
Click the Edit (pencil) icon on any row in the inventory table
A modal form will appear pre-filled with the item's current details
Make your changes and click "Update"
The item will be updated in the table and the activity log will show the action
4. Deleting Items
Click the Delete (trash) icon on any row
A confirmation dialog will appear asking "Are you sure?"
Confirm the deletion by clicking "OK"
The item will be removed from the table and the activity log will show the action
5. Data Visualization
The pie chart automatically updates as you add, edit, or delete inventory items, showing the distribution of items across categories. This gives you a quick visual insight into your inventory composition.

6. Technical Implementation Details
Frontend: React with Material UI components

Responsive design that works on desktop and mobile
Real-time filtering and sorting in the inventory table
Modern UI with intuitive controls
Backend: Spring Boot with RESTful APIs

H2 in-memory database (for ease of setup)
Robust error handling and validation
Clean architecture with separation of concerns
Launching the Application
Method 1: Using Command Line
Start the backend server:
In a separate terminal window, start the frontend:
Method 2: Using Launch Scripts
Right-click on start-backend.cmd in File Explorer and select "Run as administrator"
Wait for about 30 seconds
Right-click on start-frontend.cmd in File Explorer and select "Run as administrator"
Method 3: All-in-One Launch
Right-click on launch-application.cmd in File Explorer and select "Run as administrator"

Accessing the Application
The backend API is available at: http://localhost:8080
The frontend web interface is available at: http://localhost:3000
API Documentation
The backend exposes the following REST endpoints:

GET /api/inventory/dashboard - Get dashboard statistics and data
GET /api/inventory - Get all inventory items
GET /api/inventory/{id} - Get an item by ID
GET /api/inventory/search?query={query} - Search for items
POST /api/inventory - Create a new item
PUT /api/inventory/{id} - Update an existing item
DELETE /api/inventory/{id} - Delete an item
Stretch Goals Implemented
We've already implemented several stretch goals:

Data Visualization: The pie chart showing item distribution by category
Activity Log: The panel showing recent actions taken
Image Support: The ability to add image URLs for items
The application is designed to be easily extendable for future enhancements such as:

Implementing batch delete using checkboxes
Adding more advanced charts and visualizations
Supporting actual image uploads instead of just URLs
Adding user authentication and multi-user support