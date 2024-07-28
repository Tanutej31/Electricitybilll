import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class MeterInformation implements ActionListener {

    JFrame window;
    JTextField usernameField, meterNoField, billTypeField, daysField;
    JComboBox<String> meterLocationComboBox;
    Connection connection;

    public MeterInformation(String username, int meterNo) {
        // Initialize GUI components
        window = new JFrame("Meter Information");
        window.setIconImage(Toolkit.getDefaultToolkit().getImage("C:/Users/suren/OneDrive/Pictures/Screenshots/icon_java2.png"));
        window.getContentPane().setBackground(Color.white);
        window.setSize(600, 400);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setResizable(false);
        window.setLocationRelativeTo(null);

        // User icon
        ImageIcon originalIcon = new ImageIcon("C:/Users/suren/OneDrive/Pictures/Screenshots/icon_java3.png");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(100, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        JLabel userIcon = new JLabel(resizedIcon);
        userIcon.setBounds(20, 30, 100, 200);
        window.add(userIcon);

        // Username label and text field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(200, 30, 100, 25);
        window.add(usernameLabel);
        usernameField = new JTextField(username); // Set the passed username
        usernameField.setBounds(320, 30, 200, 25);
        window.add(usernameField);

        // Meter No label and text field
        JLabel meterNoLabel = new JLabel("Meter No");
        meterNoLabel.setBounds(200, 70, 100, 25);
        window.add(meterNoLabel);
        meterNoField = new JTextField(String.valueOf(meterNo)); // Set the passed meter number
        meterNoField.setBounds(320, 70, 200, 25);
        window.add(meterNoField);

        // Meter Location label and combo box
        JLabel meterLocationLabel = new JLabel("Meter Location");
        meterLocationLabel.setBounds(200, 110, 100, 25);
        window.add(meterLocationLabel);
        meterLocationComboBox = new JComboBox<>(new String[]{"Outside", "Inside"});
        meterLocationComboBox.setBounds(320, 110, 200, 25);
        window.add(meterLocationComboBox);

        // Bill Type label and text field
        JLabel billTypeLabel = new JLabel("Bill Type");
        billTypeLabel.setBounds(200, 150, 100, 25);
        window.add(billTypeLabel);
        billTypeField = new JTextField("Normal");
        billTypeField.setBounds(320, 150, 200, 25);
        window.add(billTypeField);

        // Days label and text field
        JLabel daysLabel = new JLabel("Days");
        daysLabel.setBounds(200, 190, 100, 25);
        window.add(daysLabel);
        daysField = new JTextField();
        daysField.setBounds(320, 190, 200, 25);
        window.add(daysField);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(200, 230, 80, 25);
        submitButton.setActionCommand("Submit");
        submitButton.addActionListener(this);
        submitButton.setBackground(new Color(0, 128, 0)); // Green background
        submitButton.setForeground(Color.white); // White text
        window.add(submitButton);

        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(300, 230, 80, 25);
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);
        cancelButton.setBackground(new Color(255, 0, 0)); // Red background
        cancelButton.setForeground(Color.white); // White text
        window.add(cancelButton);

        // Connect to the database
        try {
            String url = "jdbc:mysql://localhost:3306/electricity_bills";
            String user = "root";
            String password = "Satya2356";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(window, "Failed to connect to database.");
        }

        window.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Submit")) {
            try {
                // Get values from fields
                String username = usernameField.getText();
                String meterNo = meterNoField.getText();
                String meterLocation = (String) meterLocationComboBox.getSelectedItem();
                String billType = billTypeField.getText();
                int days = Integer.parseInt(daysField.getText());

                // Insert data into the database
                String insertQuery = "INSERT INTO meter_information (username, meter_no, meter_location, bill_type, days) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(insertQuery);
                pstmt.setString(1, username);
                pstmt.setString(2, meterNo);
                pstmt.setString(3, meterLocation);
                pstmt.setString(4, billType);
                pstmt.setInt(5, days);
                pstmt.executeUpdate();

                // Show success message
                JOptionPane.showMessageDialog(window, "Data submitted successfully");

                // Clear fields after submission
                clearFields();

                // Open ElectricityBillCalculator page
                new ElectricityBillCalculator();

            } catch (SQLException | NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(window, "Error: " + ex.getMessage());
            }
        } else if (e.getActionCommand().equals("Cancel")) {
            window.dispose();
        }
    }

    // Method to clear input fields
    private void clearFields() {
        usernameField.setText("");
        meterNoField.setText("");
        meterLocationComboBox.setSelectedIndex(0);
        billTypeField.setText("Normal");
        daysField.setText("");
    }

    public static void main(String[] args) {
        // Run the application
        SwingUtilities.invokeLater(() -> new MeterInformation("", 0));
    }
}
