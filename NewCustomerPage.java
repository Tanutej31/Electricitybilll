import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class NewCustomerPage implements ActionListener {

    JFrame window = new JFrame("New Customer");
    JTextField nameField, meterNoField, addressField, cityField, stateField, emailField, phoneField;

    // Define the customer variable
    String customer = "customer_table";

    // Colors
    Color lightBlue = new Color(173, 216, 230); // Light blue color
    Color lightGreen = new Color(144, 238, 144); // Light green color
    Color lightRed = new Color(255, 182, 193); // Light red color

    public NewCustomerPage() {
        window.setIconImage(Toolkit.getDefaultToolkit().getImage("C:/Users/suren/OneDrive/Pictures/Screenshots/icon_Profile.png"));
        window.getContentPane().setBackground(Color.white);
        window.setSize(600, 400);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        ImageIcon originalIcon = new ImageIcon("C:/Users/suren/OneDrive/Pictures/Screenshots/icon_Profile.png");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(120, 240, Image.SCALE_SMOOTH); // Increased size
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        JLabel userIcon = new JLabel(resizedIcon);
        userIcon.setBounds(20, 30, 120, 240); // Adjusted bounds
        window.add(userIcon);

        JLabel nameLabel = new JLabel("Customer Name");
        nameLabel.setBounds(200, 30, 100, 25);
        window.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(320, 30, 200, 25);
        window.add(nameField);

        JLabel meterNoLabel = new JLabel("Meter No");
        meterNoLabel.setBounds(200, 70, 100, 25);
        window.add(meterNoLabel);

        meterNoField = new JTextField();
        meterNoField.setBounds(320, 70, 200, 25);
        window.add(meterNoField);

        JLabel addressLabel = new JLabel("Address");
        addressLabel.setBounds(200, 110, 100, 25);
        window.add(addressLabel);

        addressField = new JTextField();
        addressField.setBounds(320, 110, 200, 25);
        window.add(addressField);

        JLabel cityLabel = new JLabel("City");
        cityLabel.setBounds(200, 150, 100, 25);
        window.add(cityLabel);

        cityField = new JTextField();
        cityField.setBounds(320, 150, 200, 25);
        window.add(cityField);

        JLabel stateLabel = new JLabel("State");
        stateLabel.setBounds(200, 190, 100, 25);
        window.add(stateLabel);

        stateField = new JTextField();
        stateField.setBounds(320, 190, 200, 25);
        window.add(stateField);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(200, 230, 100, 25);
        window.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(320, 230, 200, 25);
        window.add(emailField);

        JLabel phoneLabel = new JLabel("Phone Number");
        phoneLabel.setBounds(200, 270, 100, 25);
        window.add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(320, 270, 200, 25);
        window.add(phoneField);

        JButton nextButton = new JButton("Next");
        nextButton.setBounds(200, 320, 80, 25);
        nextButton.setActionCommand("Next");
        nextButton.addActionListener(this);
        nextButton.setBackground(lightGreen); // Light green background color
        window.add(nextButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(300, 320, 80, 25);
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);
        cancelButton.setBackground(lightRed); // Light red background color
        window.add(cancelButton);

        window.revalidate();
        window.repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Next")) {
            int meterNo = Integer.parseInt(meterNoField.getText());
            String name = nameField.getText();
            String address = addressField.getText();
            String city = cityField.getText();
            String state = stateField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            try {
                String url = "jdbc:mysql://localhost:3306/electricity_bills";
                String user = "root";
                String pass = "Satya2356";
                Connection con = DriverManager.getConnection(url, user, pass);
                if (con != null) System.out.println("Connection Successful");
                Statement st = con.createStatement();
                
                // Create table if not exists
                String statement = "CREATE TABLE IF NOT EXISTS " + customer + " (MeterNo INTEGER PRIMARY KEY, name VARCHAR(40), address VARCHAR(40), city VARCHAR(40), state VARCHAR(40), email VARCHAR(40), phone VARCHAR(20))";
                st.execute(statement);
                
                // Insert data
                PreparedStatement ps = con.prepareStatement("INSERT INTO " + customer + " (MeterNo, name, address, city, state, email, phone) VALUES (?, ?, ?, ?, ?, ?, ?)");
                ps.setInt(1, meterNo);
                ps.setString(2, name);
                ps.setString(3, address);
                ps.setString(4, city);
                ps.setString(5, state);
                ps.setString(6, email);
                ps.setString(7, phone);
                ps.executeUpdate(); // Make sure to execute the prepared statement
                
                // Verify insertion
                PreparedStatement verifyStmt = con.prepareStatement("SELECT * FROM " + customer + " WHERE MeterNo = ?");
                verifyStmt.setInt(1, meterNo);
                ResultSet rs = verifyStmt.executeQuery();
                if (rs.next()) {
                    System.out.println("Data inserted successfully:");
                    System.out.println("MeterNo: " + rs.getInt("MeterNo"));
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Address: " + rs.getString("address"));
                    System.out.println("City: " + rs.getString("city"));
                    System.out.println("State: " + rs.getString("state"));
                    System.out.println("Email: " + rs.getString("email"));
                    System.out.println("Phone: " + rs.getString("phone"));

                    // Pass data to MeterInformation and open the next window
                    new MeterInformation(name, meterNo);
                    window.dispose(); // Close the current window

                } else {
                    System.out.println("Data insertion failed.");
                }
            } catch (SQLException error) {
                System.out.println(error);
            }
        }
        if (e.getActionCommand().equals("Cancel")) {
            window.dispose();
        }
    }

    public static void main(String[] args) {
        new NewCustomerPage();
    }
}
