package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeView extends JFrame {

    private JLabel welcomeLabel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public HomeView(String employeeName) {
        // Set the window title
        setTitle("Time Tracker Pro - Home");

        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the size of the window
        setSize(800, 600);

        // Create a panel for the welcome message
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(new Color(50, 50, 50));
        welcomeLabel = new JLabel("Welcome, " + employeeName + "!");
        welcomeLabel.setForeground(new Color(255, 215, 0)); // Bright gold text
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel);

        // Create a navigation bar
        JPanel navBar = new JPanel();
        navBar.setBackground(new Color(50, 50, 50));
        navBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton timeSheetsButton = new JButton("Time Sheets");
        JButton callLogsButton = new JButton("Call Logs");
        JButton medicationSOButton = new JButton("Medication S/O");
        JButton reportsButton = new JButton("Reports");

        navBar.add(timeSheetsButton);
        navBar.add(callLogsButton);
        navBar.add(medicationSOButton);
        navBar.add(reportsButton);

        // Create a content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Add different panels to the content panel
        contentPanel.add(new JPanel(), "Time Sheets");
        contentPanel.add(new JPanel(), "Call Logs");
        contentPanel.add(new JPanel(), "Medication S/O");
        contentPanel.add(new JPanel(), "Reports");

        // Add action listeners to the buttons
        timeSheetsButton.addActionListener(e -> cardLayout.show(contentPanel, "Time Sheets"));
        callLogsButton.addActionListener(e -> cardLayout.show(contentPanel, "Call Logs"));
        medicationSOButton.addActionListener(e -> cardLayout.show(contentPanel, "Medication S/O"));
        reportsButton.addActionListener(e -> cardLayout.show(contentPanel, "Reports"));

        // Set layout and add components
        setLayout(new BorderLayout());
        add(welcomePanel, BorderLayout.NORTH);
        add(navBar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Center frame on the screen
        setLocationRelativeTo(null);
    }
}
