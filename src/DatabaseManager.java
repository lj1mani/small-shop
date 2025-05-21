
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:shop_inventory.db";
    private Connection connection;

    // Connect to SQLite database
    public void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            //JOptionPane.showMessageDialog(null, "Connected to database!");
            System.out.println("Connected to database!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection failed!");
            //JOptionPane.showMessageDialog(null, "Connection failed!");
        }
    }

    private Connection connect1() {
        Connection conn = null;
        try {
            // Use your actual database path here
            String url = "jdbc:sqlite:shop_inventory.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection failed.");
        }
        return conn;
    }

    // Create products table if it doesn't exist
    public void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "price REAL NOT NULL);";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tables created or already exist.");
            //JOptionPane.showMessageDialog(null, "Tables created or already exist.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a new product to the database
    public void addProduct(Product product) {
        String sql = "INSERT INTO products(name, quantity, price) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getQuantity());
            pstmt.setDouble(3, product.getPrice());
            pstmt.executeUpdate();
            System.out.println("Product added.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all products from the database
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    // Update product details
    public void updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, quantity = ?, price = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getQuantity());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getId());
            pstmt.executeUpdate();
            System.out.println("Product updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete product by ID
    public void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Product deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
