import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class LockerApp extends JFrame {
    private JTextField passcodeField;
    private JLabel titleLabel, statusLabel, logoLabel;
    private JButton enterButton, clearButton;
    private String savedPassword = "";

    public LockerApp() {
        setTitle("Locker Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350); // Adjusted size for better layout spacing
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240)); // Background color for the main frame

        // Title panel with logo and title
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titlePanel.setOpaque(false);

        // Logo and Title in vertical alignment
        JPanel logoTitlePanel = new JPanel();
        logoTitlePanel.setLayout(new BoxLayout(logoTitlePanel, BoxLayout.Y_AXIS));
        logoTitlePanel.setOpaque(false);

        // Logo
        try {
            BufferedImage logoImage = ImageIO.read(new File("image/rupp logo.png"));
            int logoWidth = 80; // Desired width (adjusted smaller)
            int logoHeight = 80; // Desired height (adjusted smaller)
            Image scaledLogoImage = logoImage.getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH);
            ImageIcon logoIcon = new ImageIcon(scaledLogoImage);
            logoLabel = new JLabel(logoIcon);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center alignment
            logoTitlePanel.add(logoLabel);
        } catch (Exception e) {
            System.out.println("Error loading logo image: " + e.getMessage());
        }

        // Title label
        titleLabel = new JLabel("Locker", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28)); // Reduced font size
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center alignment
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        logoTitlePanel.add(titleLabel);

        titlePanel.add(logoTitlePanel, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);

        // Center panel for form layout
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false); // Make center panel transparent
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Password input field
        passcodeField = new JTextField(15);
        passcodeField.setFont(new Font("Arial", Font.PLAIN, 20)); // Reduced font size
        passcodeField.setHorizontalAlignment(JTextField.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        centerPanel.add(passcodeField, gbc);

        // Status label
        statusLabel = new JLabel("Enter Password", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Reduced font size
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        centerPanel.add(statusLabel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0)); // Reduced gap between buttons
        buttonPanel.setOpaque(false); // Make button panel transparent

        // Clear button
        clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Reduced font size
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                passcodeField.setText("");
                statusLabel.setText("Enter Password");
            }
        });
        buttonPanel.add(clearButton);

        // Enter button
        enterButton = new JButton("Enter");
        enterButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Reduced font size
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String enteredPasscode = passcodeField.getText();

                // Validation: cannot be blank
                if (enteredPasscode.isEmpty()) {
                    statusLabel.setText("Password cannot be blank");
                    return;
                }

                // Validation: must be numeric
                if (!enteredPasscode.matches("\\d+")) {
                    statusLabel.setText("Password must be numeric");
                    return;
                }

                if (savedPassword.isEmpty()) {
                    // Set password mode
                    savedPassword = enteredPasscode;
                    statusLabel.setText("Password Set");
                } else {
                    // Locker opening mode
                    if (enteredPasscode.equals(savedPassword)) {
                        statusLabel.setText("Correct Password");
                    } else {
                        statusLabel.setText("Incorrect Password");
                    }
                }
            }
        });
        buttonPanel.add(enterButton);

        gbc.gridy = 2;
        gbc.gridwidth = 2;
        centerPanel.add(buttonPanel, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Centering the window on the screen
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LockerApp();
            }
        });
    }
}
