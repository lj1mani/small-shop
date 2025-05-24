import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        ProductGUI productGUI = new ProductGUI();

        // Menu options for the user to choose from
        String[] options = {"Add product", "Show all products", "Update product", "Delete product", "Search product", "Check low stock", "Total cost"};

        // Infinite loop to keep showing the menu until user chooses "Exit"
        while (true) {

            // Show a dialog with options as buttons
            int choice = JOptionPane.showOptionDialog(
                    null,               // Parent component (null means center on screen)
                    null,                     // Message (none in this case)
                    "Shop menu",        // Title of the dialog
                    JOptionPane.DEFAULT_OPTION, // Type of options
                    JOptionPane.PLAIN_MESSAGE,  // No icon
                    null,                  // No custom icon
                    options,                    // Options displayed as buttons
                    options[0]                  // Default selected option
            );

            // If user closes the dialog
            if (choice == -1) {
                JOptionPane.showMessageDialog(null, "Goodbye!"); // Say goodbye
                break;
            }

            // Handle user's choice using switch statement
            switch (choice) {
                case 0:
                    productGUI.addProductWithGUI();
                    break;

                case 1:
                    productGUI.showAllProductWithGUI(); 
                    break;

                case 2:
                    productGUI.updateProductWithGUI();
                    break;

                case 3:
                    productGUI.deleteProductWithGUI();
                    break;

                case 4:
                    productGUI.searchProductWithGUI();
                    break;

                case 5:
                    productGUI.showLowStockProducts();
                    break;

                case 6:
                    productGUI.showTotalInventoryValue();
                    break;

                default:
                    break;
            }
        }

    }
}
