package view;

import javax.swing.*;

import model.TimeSheet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

public class TimeSheetEntryPanel extends JPanel {
    private JComboBox<String> employeeNameComboBox;
    private JSpinner shiftStartDatePicker;
    private JSpinner shiftEndDatePicker;
    private JSpinner shiftStartTimePicker;
    private JSpinner shiftEndTimePicker;
    private TitledTextField overtimeCommentField;
    private JButton submitButton;

    public TimeSheetEntryPanel(List<String> employeeNames) {
        setLayout(new GridLayout(2, 1));

        // Employee Name Dropdown
        employeeNameComboBox = new JComboBox<>(employeeNames.toArray(new String[0]));

        // Date Pickers
        shiftStartDatePicker = new JSpinner(new SpinnerDateModel());
        shiftEndDatePicker = new JSpinner(new SpinnerDateModel());
        shiftStartTimePicker = new JSpinner(new SpinnerDateModel());
        shiftEndTimePicker = new JSpinner(new SpinnerDateModel());

        // Set date editors
        JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(shiftStartDatePicker, "yyyy-MM-dd");
        JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(shiftEndDatePicker, "yyyy-MM-dd");
        JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(shiftStartTimePicker, "HH:mm:ss");
        JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(shiftEndTimePicker, "HH:mm:ss");

        shiftStartDatePicker.setEditor(startDateEditor);
        shiftEndDatePicker.setEditor(endDateEditor);
        shiftStartTimePicker.setEditor(startTimeEditor);
        shiftEndTimePicker.setEditor(endTimeEditor);

        // Overtime Comment Field
        overtimeCommentField = new TitledTextField("Overtime Comment", "Enter comment", 20);

        // Submit Button
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle submission logic here
                String employeeName = (String) employeeNameComboBox.getSelectedItem();
                Date shiftStartDate = (Date) shiftStartDatePicker.getValue();
                Date shiftEndDate = (Date) shiftEndDatePicker.getValue();
                Date shiftStartTime = (Date) shiftStartTimePicker.getValue();
                Date shiftEndTime = (Date) shiftEndTimePicker.getValue();
                String overtimeComment = overtimeCommentField.getText();

                // Create TimeSheet object and handle database insertion
                TimeSheet timeSheet = new TimeSheet(employeeName, -1, shiftStartDate, shiftEndDate, shiftStartTime, shiftEndTime, overtimeComment);
                // Add your database insertion logic here
            }
        });

        // Add components to panel
        add(createFieldPanel("Employee Name", employeeNameComboBox));
        add(createFieldPanel("Shift Start Date", shiftStartDatePicker));
        add(createFieldPanel("Shift End Date", shiftEndDatePicker));
        add(createFieldPanel("Shift Start Time", shiftStartTimePicker));
        add(createFieldPanel("Shift End Time", shiftEndTimePicker));
        add(overtimeCommentField);
        add(submitButton);
    }

    private JPanel createFieldPanel(String label, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(label), BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }
}
