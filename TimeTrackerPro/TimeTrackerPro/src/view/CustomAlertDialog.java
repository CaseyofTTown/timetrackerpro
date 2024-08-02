package view;

import javax.swing.*;

import model.ColorConstants;

import java.awt.*;

public class CustomAlertDialog {

    public static void showDialog(Component parent, String message, String title) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(parent), title, true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setBackground(ColorConstants.CHARCOAL);
        panel.setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setForeground(ColorConstants.GOLD);
        panel.add(messageLabel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.setBackground(ColorConstants.SLATE_GRAY);
        okButton.setForeground(ColorConstants.GOLD);
        okButton.addActionListener(e -> dialog.dispose());
        panel.add(okButton, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }
}
