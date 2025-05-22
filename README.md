## Small Shop Inventory System (Java + SQLite)

This is a simple **Java desktop application** for managing inventory in a small shop. The system uses **Swing** for the GUI and **SQLite** as a local database to store product data (name, quantity, and price).

## Features

- Add new products
- View all products
- Update product details
- Delete products
- Data stored locally using SQLite
- Search for a product by name and view its full details
- Shows low stock for product witch quantity is under 5

## Technologies Used

- Java (JDK 8+)
- Swing (for GUI)
- SQLite (for local database)
- JDBC (for database connection)

## Project Structure
- Product.java // Product data model
- ProductGUI.java // User interface for managing products
- DatabaseManager.java // Handles SQLite database operations

##How to Run

1. **Clone or download** this repository.
2. Open the project in your IDE (e.g. IntelliJ IDEA, Eclipse, NetBeans).
3. Make sure `sqlite-jdbc` is added to your project libraries.
   - You can download it here: https://bitbucket.org/xerial/sqlite-jdbc/downloads/
4. Run `ProductGUI.java` to open the application window.

## Database

The application creates a local SQLite database file named:
shop_inventory.db

## Author
- Đejhan Ljmani



