import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PasswordGenerator extends JFrame implements ActionListener, DocumentListener {
    private static final SecureRandom secureRandom = new SecureRandom();
    private final JTextField passwordField;
    private final JCheckBox uppercaseCheckBox;
    private final JCheckBox lowercaseCheckBox;
    private final JCheckBox numbersCheckBox;
    private final JCheckBox specialCharsCheckBox;
    private final JButton generateButton;
    private final JTextField lengthTextField;

    PasswordGenerator() {
        setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Password Generator");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Password Length:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        lengthTextField = new JTextField(5);
        lengthTextField.getDocument().addDocumentListener(this);
        inputPanel.add(lengthTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        uppercaseCheckBox = new JCheckBox("Include Uppercase Letters");
        inputPanel.add(uppercaseCheckBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        lowercaseCheckBox = new JCheckBox("Include Lowercase Letters");
        inputPanel.add(lowercaseCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        numbersCheckBox = new JCheckBox("Include Numbers");
        inputPanel.add(numbersCheckBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        specialCharsCheckBox = new JCheckBox("Include Special Characters");
        inputPanel.add(specialCharsCheckBox, gbc);

        JPanel resultPanel = new JPanel(new FlowLayout());
        passwordField = new JTextField(20);
        passwordField.setEditable(false);
        resultPanel.add(passwordField);

        generateButton = new JButton("Generate Password");
        generateButton.addActionListener(this);
        generateButton.setEnabled(false); // Disable the generate button initially

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(generateButton);

        add(titleLabel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generateButton) {
            // Validate checkbox selection
            if (!uppercaseCheckBox.isSelected() && !lowercaseCheckBox.isSelected() && !numbersCheckBox.isSelected()
                    && !specialCharsCheckBox.isSelected()) {
                JOptionPane.showMessageDialog(this, "Please select at least one option for the password.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get the specified password length (if provided)
            int length;
            try {
                length = Integer.parseInt(lengthTextField.getText());
                if (length <= 0) {
                    JOptionPane.showMessageDialog(this, "Password length must be a positive integer.", "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid password length.", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean uppercase = uppercaseCheckBox.isSelected();
            boolean lowercase = lowercaseCheckBox.isSelected();
            boolean numbers = numbersCheckBox.isSelected();
            boolean specialChars = specialCharsCheckBox.isSelected();

            // Generate the password
            String password = generatePassword(length, uppercase, lowercase, numbers, specialChars);

            // Show a pop-up message indicating successful password generation
            JOptionPane.showMessageDialog(this, "Password successfully generated!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            passwordField.setText(password);
        }
    }

    private String generatePassword(int length, boolean uppercase, boolean lowercase, boolean numbers,
            boolean specialChars) {
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String numberChars = "0123456789";
        String specialCharsStr = "!@#$%^&*()_+-=[]{}|;:,.<>?";

        StringBuilder allowedChars = new StringBuilder();
        if (uppercase)
            allowedChars.append(uppercaseChars);
        if (lowercase)
            allowedChars.append(lowercaseChars);
        if (numbers)
            allowedChars.append(numberChars);
        if (specialChars)
            allowedChars.append(specialCharsStr);

        StringBuilder password = new StringBuilder();

        // Generate the password with at least one character from each selected category
        while (password.length() < length) {
            password.append(allowedChars.charAt(secureRandom.nextInt(allowedChars.length())));
        }

        return password.toString();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // Not used
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateCheckBoxes();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateCheckBoxes();
    }

    private void updateCheckBoxes() {
        // Automatically uncheck all checkboxes when the user clears the password length
        if (lengthTextField.getText().trim().isEmpty()) {
            uppercaseCheckBox.setSelected(false);
            lowercaseCheckBox.setSelected(false);
            numbersCheckBox.setSelected(false);
            specialCharsCheckBox.setSelected(false);
            generateButton.setEnabled(false);
        } else {
            generateButton.setEnabled(true);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new PasswordGenerator();
        });
    }
}
