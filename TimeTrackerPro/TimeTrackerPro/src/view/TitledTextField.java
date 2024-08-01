package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class TitledTextField extends JPanel {
    private JTextField textField;

    public TitledTextField(String title, String hintText, int columns) {
        setLayout(new BorderLayout());
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleColor(Color.GREEN); // Set the title color to match the theme
        setBorder(border);

        textField = new JTextField(hintText, columns);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(hintText)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(hintText);
                    textField.setForeground(Color.GRAY);
                }
            }
        });

        add(textField, BorderLayout.CENTER);
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
