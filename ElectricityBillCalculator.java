import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class ElectricityBillCalculator implements ActionListener {

    JFrame frame;
    JTextField meterNoField, nameField, addressField, unitsField;
    JComboBox<String> monthComboBox;
    Connection connection;

    // Define the unit price
    private static final double UNIT_PRICE = 5.50;

    public ElectricityBillCalculator() {
        frame = new JFrame("Calculate Electricity Bill");
        frame.setSize(500, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Center the frame
        frame.setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Calculate Electricity Bill");
        titleLabel.setBounds(150, 10, 200, 25);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(titleLabel);

        JLabel meterNoLabel = new JLabel("Meter No");
        meterNoLabel.setBounds(50, 50, 100, 25);
        frame.add(meterNoLabel);

        meterNoField = new JTextField();
        meterNoField.setBounds(200, 50, 200, 25);
        frame.add(meterNoField);

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(50, 90, 100, 25);
        frame.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(200, 90, 200, 25);
        frame.add(nameField);

        JLabel addressLabel = new JLabel("Address");
        addressLabel.setBounds(50, 130, 100, 25);
        frame.add(addressLabel);

        addressField = new JTextField();
        addressField.setBounds(200, 130, 200, 25);
        frame.add(addressField);

        JLabel unitsLabel = new JLabel("Units Consumed");
        unitsLabel.setBounds(50, 170, 100, 25);
        frame.add(unitsLabel);

        unitsField = new JTextField();
        unitsField.setBounds(200, 170, 200, 25);
        frame.add(unitsField);

        JLabel monthLabel = new JLabel("Month");
        monthLabel.setBounds(50, 210, 100, 25);
        frame.add(monthLabel);

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthComboBox = new JComboBox<>(months);
        monthComboBox.setBounds(200, 210, 200, 25);
        frame.add(monthComboBox);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(120, 250, 80, 25);
        submitButton.setActionCommand("Submit");
        submitButton.addActionListener(this);
        submitButton.setBackground(Color.GREEN); // Set background color
        submitButton.setForeground(Color.WHITE); // Set text (foreground) color
        frame.add(submitButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(220, 250, 80, 25);
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);
        cancelButton.setBackground(Color.RED); // Set background color
        cancelButton.setForeground(Color.WHITE); // Set text (foreground) color
        frame.add(cancelButton);

        // Connect to the database
        connectToDatabase();

        frame.setVisible(true);
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/electricity_bills";
            String user = "root";
            String password = "Satya2356";
            connection = DriverManager.getConnection(url, user, password);

            // Create the table if it doesn't exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS bill_information (" +
                                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                    "meter_no VARCHAR(50), " +
                                    "name VARCHAR(100), " +
                                    "address VARCHAR(255), " +
                                    "units_consumed INT, " +
                                    "month VARCHAR(50), " +
                                    "total_bill DOUBLE)";
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to connect to database.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Submit")) {
            String meterNo = meterNoField.getText();
            String name = nameField.getText();
            String address = addressField.getText();
            int unitsConsumed = Integer.parseInt(unitsField.getText());
            String month = (String) monthComboBox.getSelectedItem();

            // Calculate total bill
            double totalBill = unitsConsumed * UNIT_PRICE;

            try {
                // Insert data into the database
                String insertQuery = "INSERT INTO bill_information (meter_no, name, address, units_consumed, month, total_bill) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(insertQuery);
                pstmt.setString(1, meterNo);
                pstmt.setString(2, name);
                pstmt.setString(3, address);
                pstmt.setInt(4, unitsConsumed);
                pstmt.setString(5, month);
                pstmt.setDouble(6, totalBill);
                pstmt.executeUpdate();

                // Show success message
                JOptionPane.showMessageDialog(frame, "Data submitted successfully");

                // Clear fields after submission
                clearFields();

                // Fetch and display the inserted data
                fetchData();

            } catch (SQLException | NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        } else if (command.equals("Cancel")) {
            frame.dispose();
        }
    }

    private void clearFields() {
        meterNoField.setText("");
        nameField.setText("");
        addressField.setText("");
        unitsField.setText("");
        monthComboBox.setSelectedIndex(0);
    }

    private void fetchData() {
        try {
            String selectQuery = "SELECT * FROM bill_information";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Meter No: " + rs.getString("meter_no"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("Units Consumed: " + rs.getInt("units_consumed"));
                System.out.println("Month: " + rs.getString("month"));
                System.out.println("Total Bill: " + rs.getDouble("total_bill"));
                System.out.println("=================================");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ElectricityBillCalculator();
    }
}
