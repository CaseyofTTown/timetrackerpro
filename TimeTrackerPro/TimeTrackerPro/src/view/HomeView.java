package view;

import javax.swing.*;

import model.ColorConstants;

import java.awt.*;
import java.util.Date;

public class HomeView extends JFrame {

    private JLabel welcomeLabel;
    private JTabbedPane tabbedPane;
    private TimeSheetPanel timeSheetPanel;

    public HomeView(String employeeName) {
        // Set the window title
        setTitle("Time Tracker Pro - Home");

        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel with a BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorConstants.CHARCOAL);

        // Add the panel to the frame
        add(panel);

        // Create a panel for the welcome message
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(ColorConstants.DARK_GRAY);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Create a label for the welcome message
        welcomeLabel = new JLabel("Welcome, " + employeeName + "!", SwingConstants.CENTER);
        welcomeLabel.setForeground(ColorConstants.GOLD);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel);

        // Add the welcome panel to the NORTH region of the main panel
        panel.add(welcomePanel, BorderLayout.NORTH);

        // Create the tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(ColorConstants.DARK_GRAY);
        tabbedPane.setForeground(ColorConstants.LIME_GREEN);

        // Add tabs with standard components
        tabbedPane.addTab("Time Sheets", createTimeSheetsPanel());
        tabbedPane.addTab("Call Logs", createCallLogsPanel());
        tabbedPane.addTab("Medication S/O", createMedicationSOPanel());
        tabbedPane.addTab("Reports", createReportsPanel());

        // Add the tabbed pane to the panel
        panel.add(tabbedPane, BorderLayout.CENTER);

        // Pack the frame, which will take into account the preferred size of its contents
       // pack();

        // Center the frame on the screen
        setLocationRelativeTo(null);
        
       
    }

    // Methods to create panels for each tab
    private JPanel createTimeSheetsPanel() {
        timeSheetPanel = new TimeSheetPanel();
        timeSheetPanel.setBackground(ColorConstants.CHARCOAL);
        return timeSheetPanel;
    }

    private JPanel createCallLogsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(ColorConstants.CHARCOAL);
        // Add components specific to Call Logs
        return panel;
    }

    private JPanel createMedicationSOPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(ColorConstants.CHARCOAL);
        // Add components specific to Medication S/O
        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(ColorConstants.CHARCOAL);
        // Add components specific to Reports
        return panel;
    }
    //methods to set the start and end dates on timesheetview
    public void setStartDate(Date date) {
    	timeSheetPanel.setStartDate(date);
    }
    public void setEndDate(Date date) {
    	timeSheetPanel.setEndDate(date);
    }
}
