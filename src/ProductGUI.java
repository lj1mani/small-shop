import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;
import java.awt.*;

// GUI class to handle adding, viewing, updating, and deleting products using dialogs
public class ProductGUI {

    // Method to add a new product via input dialogs
    public void addProductWithGUI() {
        // Ask for product name
        String name = JOptionPane.showInputDialog("Enter product name:");
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Product name cannot be empty.");
            return;
        }

        // Ask for quantity (must be non-negative integer)
        int quantity = 0;
        while (true) {
            String quantityStr = JOptionPane.showInputDialog("Enter quantity:");
            if (quantityStr == null) return;

            try {
                quantity = Integer.parseInt(quantityStr);
                if (quantity < 0) {
                    JOptionPane.showMessageDialog(null, "Quantity cannot be negative.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for quantity.");
            }
        }

        // Ask for price (must be non-negative double)
        double price = 0.0;
        while (true) {
            String priceStr = JOptionPane.showInputDialog("Enter price:");
            if (priceStr == null) return;

            try {
                price = Double.parseDouble(priceStr);
                if (price < 0) {
                    JOptionPane.showMessageDialog(null, "Price cannot be negative.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for price.");
            }
        }

        // Create product object with user input
        Product product = new Product(name, quantity, price);

        // Create database connection and add product
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();
        databaseManager.createTables();  // This method should check if the table exists before creating
        databaseManager.addProduct(product);

        JOptionPane.showMessageDialog(null, "Product added.");
    }

    // Method to display all products in a scrollable text area
    public void showAllProductWithGUI() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();

        List<Product> productList = databaseManager.getAllProducts();

        if (productList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Empty base");
            return;
        }

        // Define column names for the table
        String[] columns = {"ID", "Name", "Quantity", "Price"};

        // Create a table model and set column names
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        // Formatter for price to show 2 decimals
        DecimalFormat df = new DecimalFormat("0.00");

        // Add rows from product list to table model
        for (Product product : productList) {
            Object[] row = {
                    product.getId(),
                    product.getName(),
                    product.getQuantity(),
                    "$" + df.format(product.getPrice())
            };
            tableModel.addRow(row);
        }

        // Create JTable with model
        JTable table = new JTable(tableModel);
        table.setEnabled(false); // disable editing

        // Optional: set font and row height for better look
        table.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        table.setRowHeight(22);

        // Wrap in scroll pane with preferred size
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new java.awt.Dimension(450, 300));

        // Show in dialog
        JOptionPane.showMessageDialog(null, scrollPane, "All Products", JOptionPane.INFORMATION_MESSAGE);
    }
    // Method to update a product via scrollable dropdown
    public void updateProductWithGUI() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();

        // Load all products
        List<Product> productList = databaseManager.getAllProducts();
        if (productList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No products to update.");
            return;
        }

        // Build options
        String[] productOptions = new String[productList.size()];
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < productList.size(); i++) {
            Product p = productList.get(i);
            productOptions[i] = "ID: " + p.getId() + " | " + p.getName() + " | Qty: " + p.getQuantity() + " | $" + df.format(p.getPrice());
        }

        // ComboBox to select product
        JComboBox<String> comboBox = new JComboBox<>(productOptions);
        comboBox.setPreferredSize(new Dimension(400, 25));
        JScrollPane scrollPane = new JScrollPane(comboBox);
        scrollPane.setPreferredSize(new Dimension(420, 70));
        int option = JOptionPane.showConfirmDialog(null, scrollPane, "Select Product to Update", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;

        int selectedIndex = comboBox.getSelectedIndex();
        Product selectedProduct = productList.get(selectedIndex);

        // Input fields
        JTextField nameField = new JTextField(selectedProduct.getName(), 20);
        JTextField quantityField = new JTextField(String.valueOf(selectedProduct.getQuantity()), 5);
        JTextField priceField = new JTextField(df.format(selectedProduct.getPrice()), 7);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Product Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Price (e.g. 2.50):"));
        panel.add(priceField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Product Details", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        // Read inputs
        String newName = nameField.getText().trim();
        String quantityStr = quantityField.getText().trim();
        String priceStr = priceField.getText().trim().replace(",", "."); // fix locale

        // Input validation
        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Product name cannot be empty.");
            return;
        }

        int newQty;
        double newPrice;

        try {
            newQty = Integer.parseInt(quantityStr);
            if (newQty < 0) {
                JOptionPane.showMessageDialog(null, "Quantity cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid quantity format.");
            return;
        }

        try {
            newPrice = Double.parseDouble(priceStr);
            if (newPrice < 0) {
                JOptionPane.showMessageDialog(null, "Price cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid price format.");
            return;
        }

        // Update product
        selectedProduct.setName(newName);
        selectedProduct.setQuantity(newQty);
        selectedProduct.setPrice(newPrice);
        databaseManager.updateProduct(selectedProduct);

        JOptionPane.showMessageDialog(null, "Product updated successfully.");
    }


    // Method to delete a product via scrollable dropdown
    public void deleteProductWithGUI() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();

        // 1. Load products
        List<Product> productList = databaseManager.getAllProducts();
        if (productList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No products to delete.");
            return;
        }

        // 2. Create dropdown list
        DecimalFormat df = new DecimalFormat("0.00");
        String[] productOptions = new String[productList.size()];
        for (int i = 0; i < productList.size(); i++) {
            Product p = productList.get(i);
            productOptions[i] = "ID: " + p.getId() + " | " + p.getName() + " | Qty: " + p.getQuantity() + " | $" + df.format(p.getPrice());
        }

        JComboBox<String> comboBox = new JComboBox<>(productOptions);
        comboBox.setPreferredSize(new java.awt.Dimension(350, 25));
        JScrollPane scrollPane = new JScrollPane(comboBox);
        scrollPane.setPreferredSize(new java.awt.Dimension(380, 70));

        int option = JOptionPane.showConfirmDialog(null, scrollPane, "Select Product to Delete", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;

        // 3. Confirm delete
        int selectedIndex = comboBox.getSelectedIndex();
        Product selectedProduct = productList.get(selectedIndex);

        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete:\n" + selectedProduct.getName() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            databaseManager.deleteProduct(selectedProduct.getId());
            JOptionPane.showMessageDialog(null, "Product deleted successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "Deletion cancelled.");
        }
    }

    public void searchProductWithGUI() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();

        // 1. Load products
        List<Product> productList = databaseManager.getAllProducts();
        if (productList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No products to search.");
            return;
        }

        // 2. Ask for product name
        String productName = JOptionPane.showInputDialog("Search for product name:");
        if (productName == null || productName.trim().isEmpty()) {
            return;
        }

        // 3. Search for matching products (case-insensitive)
        boolean found = false;
        StringBuilder result = new StringBuilder();
        for (Product p : productList) {
            if (p.getName().equalsIgnoreCase(productName.trim())) {
                result.append("ID: ").append(p.getId())
                        .append("\nName: ").append(p.getName())
                        .append("\nQuantity: ").append(p.getQuantity())
                        .append("\nPrice: $").append(String.format("%.2f", p.getPrice()))
                        .append("\n\n");
                found = true;
            }
        }

        // 4. Show result or not found message
        if (found) {
            JOptionPane.showMessageDialog(null, result.toString(), "Search Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No product found with that name.");
        }
    }

    // Method to show products that are below the low stock threshold
    public void showLowStockProducts() {
        // Connect to the database
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();

        // 1. Load all products from the database
        List<Product> productList = databaseManager.getAllProducts();

        if (productList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No products.");
            return;
        }

        List<Product> lowStockList = new ArrayList<>(); // List to store low stock products


        int threshold = 5; // Define the minimum stock threshold

        // 2. Filter products that have quantity below the threshold
        for (Product p : productList) {
            if (p.getQuantity() < threshold) {
                lowStockList.add(p);
            }
        }

        // 3. If no low stock products found, show message and return
        if (lowStockList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No low stock products.");
            return;
        }

        // 4. Build a string to display the low stock product list
        StringBuilder result = new StringBuilder("Low Stock Products (Below " + threshold + "):\n\n");
        for (Product p : lowStockList) {
            result.append(p.getName())
                    .append(" | Quantity: ").append(p.getQuantity())
                    .append("\n");
        }

        // 5. Display the result in a scrollable
        JTextArea textArea = new JTextArea(result.toString());
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 15));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));

        // 6. Show the final message dialog with the low stock alert
        JOptionPane.showMessageDialog(null, scrollPane, "Low Stock Alert", JOptionPane.WARNING_MESSAGE);
    }


    public void showTotalInventoryValue(){
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();

        // 1. Load all products from the database
        List<Product> productList = databaseManager.getAllProducts();

        if (productList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No products.");
            return;
        }

        double totalCost = 0;

        for (Product p : productList){
            totalCost += p.getQuantity() * p.getPrice();
        }

        JOptionPane.showMessageDialog(null, "Total cost is: $" + totalCost);



    }

}
