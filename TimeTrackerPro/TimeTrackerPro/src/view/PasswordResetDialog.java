package view;
import javax.swing.*;

import controller.TTController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordResetDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField pinField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton submitButton;
    private JButton cancelButton;
    private boolean isSubmitted;
    private TTController controller;

    public PasswordResetDialog(Frame parent, TTController controller) {
        super(parent, "Reset Password", true);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);

        usernameField = new JTextField(20);
        pinField = new JPasswordField(20);
        newPasswordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);

        add(new JLabel("Username:"), c);
        c.gridy++;
        add(usernameField, c);
        c.gridy++;
        add(new JLabel("PIN:"), c);
        c.gridy++;
        add(pinField, c);
        c.gridy++;
        add(new JLabel("New Password:"), c);
        c.gridy++;
        add(newPasswordField, c);
        c.gridy++;
        add(new JLabel("Confirm Password:"), c);
        c.gridy++;
        add(confirmPasswordField, c);
        c.gridy++;

        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, c);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (new String(newPasswordField.getPassword()).equals(new String(confirmPasswordField.getPassword()))) {
                    isSubmitted = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(PasswordResetDialog.this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isSubmitted = false;
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(parent);
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public int getPin() {
        return Integer.parseInt(new String(pinField.getPassword()));
    }

    public String getNewPassword() {
        return new String(newPasswordField.getPassword());
    }
}
