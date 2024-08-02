package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import model.ColorConstants;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class TitledTextField extends JPanel {
    private JTextField textField;

    public TitledTextField(String title, String hintText, int columns) {
        setLayout(new BorderLayout());
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleColor(ColorConstants.GOLD);
        setBorder(border);

        textField = new JTextField(hintText, columns);
        textField.setForeground(ColorConstants.LIGHT_GRAY);
        textField.setBackground(ColorConstants.DARK_GRAY); // Darker background
        textField.setCaretColor(ColorConstants.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorConstants.SLATE_GRAY), // Slightly lighter outer border
                BorderFactory.createEmptyBorder(5, 5, 5, 5) // Inner padding
        ));

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(hintText)) {
                    textField.setText("");
                    textField.setForeground(ColorConstants.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(hintText);
                    textField.setForeground(ColorConstants.LIGHT_GRAY);
                }
            }
        });

        add(textField, BorderLayout.CENTER);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	Graphics2D g2d = (Graphics2D) g;
    	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	//Custom background color
    	g2d.setColor(ColorConstants.CHARCOAL); //background behind border
    	
    	g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
    }

    public JTextField getTextField() {
        return textField;
    }
}
