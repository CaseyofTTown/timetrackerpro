package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import model.ColorConstants;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class TitledPasswordField extends JPanel {
    private JPasswordField passwordField;

    public TitledPasswordField(String title, String hintText, int columns) {
        setLayout(new BorderLayout());
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleColor(ColorConstants.GOLD);
        setBorder(border);

        passwordField = new JPasswordField(hintText, columns);
        passwordField.setForeground(ColorConstants.LIGHT_GRAY);
        passwordField.setBackground(ColorConstants.DARK_GRAY); // Darker background
        passwordField.setCaretColor(ColorConstants.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorConstants.SLATE_GRAY), // Slightly lighter outer border
                BorderFactory.createEmptyBorder(5, 5, 5, 5) // Inner padding
        ));

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(passwordField.getPassword()).equals(hintText)) {
                    passwordField.setText("");
                    passwordField.setForeground(ColorConstants.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText(hintText);
                    passwordField.setForeground(ColorConstants.LIGHT_GRAY);
                }
            }
        });

        add(passwordField, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Custom background color
        g2d.setColor(ColorConstants.CHARCOAL); // Darker background behind the border
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    public String getText() {
        return new String(passwordField.getPassword());
    }

    public void setText(String text) {
        passwordField.setText(text);
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }
}
