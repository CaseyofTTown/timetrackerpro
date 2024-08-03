package view;

import javax.swing.*;
import org.jdatepicker.impl.JDatePickerImpl;
import model.ColorConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimeSheetPanel extends JPanel {

    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteTimeSheetButton;
    private TimeSheetDisplay timeSheetDisplay;
    private JDatePickerImpl startDatePicker;
    private JDatePickerImpl endDatePicker;

    public TimeSheetPanel() {
        setLayout(new BorderLayout());
        setBackground(ColorConstants.CHARCOAL);

        // Date Range selection
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        datePanel.setBackground(ColorConstants.DARK_GRAY);
        datePanel.setForeground(ColorConstants.GOLD);

        JLabel dateRangeLabel = new JLabel("Select Date Range: ");
        dateRangeLabel.setForeground(ColorConstants.GOLD);
        dateRangeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        datePanel.add(dateRangeLabel);

        DatePicker startDatePickerComponent = new DatePicker();
        startDatePicker = startDatePickerComponent.getDatePicker();
        DatePicker endDatePickerComponent = new DatePicker();
        endDatePicker = endDatePickerComponent.getDatePicker();
        
        
        datePanel.add(startDatePicker);
        datePanel.add(endDatePicker);

        add(datePanel, BorderLayout.NORTH);

        // TimeSheetDisplay Component
        timeSheetDisplay = new TimeSheetDisplay();
        add(new JScrollPane(timeSheetDisplay), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(ColorConstants.DARK_GRAY);

        addButton = new JButton("Add New Time Sheet");
        modifyButton = new JButton("Modify Time Sheet");
        deleteTimeSheetButton = new JButton("Delete Time Sheet");

        styleButton(addButton);
        styleButton(modifyButton);
        styleButton(deleteTimeSheetButton);

        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteTimeSheetButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // disable buttons if time sheet not selected
        modifyButton.setEnabled(false);
        deleteTimeSheetButton.setEnabled(false);

        // Add action listeners for buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle add new time sheet action
            }
        });

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle modify time sheet action
            }
        });

        deleteTimeSheetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // handle delete time sheet action
            }
        });

        timeSheetDisplay.getSelectionModel().addListSelectionListener(e -> {
            boolean isSelected = timeSheetDisplay.getSelectedRow() != -1;
            modifyButton.setEnabled(isSelected);
            deleteTimeSheetButton.setEnabled(isSelected);
        });
    }

    private void styleButton(JButton button) {
        button.setBackground(ColorConstants.DEEP_BLUE);
        button.setForeground(ColorConstants.LIME_GREEN);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    // Getters for buttons and display
    public JButton getAddButton() {
        return addButton;
    }

    public JButton getModifyButton() {
        return modifyButton;
    }

    public JButton getDeleteTimeSheetButton() {
        return deleteTimeSheetButton;
    }

    public TimeSheetDisplay getTimeSheetDisplay() {
        return timeSheetDisplay;
    }

    public JDatePickerImpl getStartdatePicker() {
        return startDatePicker;
    }

    public JDatePickerImpl getEndDatePicker() {
        return endDatePicker;
    }

    public int getSelectedTimeSheetId() {
        int selectedRow = timeSheetDisplay.getSelectedRow();
        if (selectedRow != -1) {
            System.out.println("getting time sheet from row: " + selectedRow);
            return (int) timeSheetDisplay.getValueAt(selectedRow, 0);
        }
        return -1;
    }
}
