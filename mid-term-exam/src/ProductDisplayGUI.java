import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.*;

public class ProductDisplayGUI {

    private static JFrame frame;
    private static JTable table;
    private static DefaultTableModel tableModel;

    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:51828;databaseName=E6ProductExam;encrypt=false;trustServerCertificate=true";
        String username = "sa";
        String password = "Demo@123";

        // Create the main frame
        frame = new JFrame("Product Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // Center the frame on screen

        // Panel for search and table view
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Search by Name:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchProducts(searchField.getText()));
        inputPanel.add(searchLabel);
        inputPanel.add(searchField);
        inputPanel.add(searchButton);
        searchPanel.add(inputPanel, BorderLayout.NORTH);

        // Table view panel
        table = new JTable();
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setRowHeight(25);
        JScrollPane tableScrollPane = new JScrollPane(table);
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Add main panel to the frame
        frame.add(mainPanel);

        // Initialize table model
        tableModel = new DefaultTableModel();
        table.setModel(tableModel);
        tableModel.setColumnIdentifiers(new String[]{"ID", "Name", "Price per Unit", "Active for Sell"});

        // JDBC Connection and display data in table
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            displayAllProducts(statement);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Show the frame
        frame.setVisible(true);
    }

    private static void displayAllProducts(Statement statement) throws SQLException {
        String query = "SELECT * FROM Product";
        ResultSet resultSet = statement.executeQuery(query);

        // Clear existing rows from table model
        tableModel.setRowCount(0);

        // Populate table model with data
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            double pricePerUnit = resultSet.getDouble("price_per_unit");
            boolean activeForSell = resultSet.getBoolean("active_for_sell");

            tableModel.addRow(new Object[]{id, name, pricePerUnit, activeForSell});
        }
    }

    private static void searchProducts(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        if (searchText.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }
}
