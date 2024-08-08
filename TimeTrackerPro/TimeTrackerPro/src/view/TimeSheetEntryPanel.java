package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import model.ColorConstants;
import model.TimeFormatter;
import model.TimeSheet;
import model.ValidationListener;

public class TimeSheetEntryPanel extends JPanel {
    private JComboBox<String> employeeNameComboBox;
    private JDatePickerImpl shiftStartDatePicker;
    private JDatePickerImpl shiftEndDatePicker;
    private JFormattedTextField shiftStartTimePicker;
    private JFormattedTextField shiftEndTimePicker;
    private TitledTextField overtimeCommentField;
    private JButton submitButton;
    private JButton startTimeButton;
    private JButton endTimeButton;
    private JButton cancelButton;
    private List<String> employeeNames;

    public TimeSheetEntryPanel(List<String> employeeNames) {
    	
    	if (employeeNames == null) {
			employeeNames = new ArrayList<>();
		}
    	this.employeeNames = employeeNames;
    	
        setLayout(new GridBagLayout());
        setBackground(ColorConstants.CHARCOAL);
        
        System.out.println("Employee Names: " + employeeNames);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10,10,10,10); //spacing of columns 

        // Employee Name Dropdown
        employeeNameComboBox = new JComboBox<>(employeeNames.toArray(new String[0]));
        employeeNameComboBox.setBackground(ColorConstants.CHARCOAL);
        employeeNameComboBox.setForeground(ColorConstants.GOLD);
        addComponent("Employee Name", employeeNameComboBox, c, 0, 0);

        // Shift Start Date Picker
        DatePicker startDatePicker = new DatePicker();
        shiftStartDatePicker = startDatePicker.getDatePicker();
        shiftStartDatePicker.getJFormattedTextField().setBackground(ColorConstants.DARK_GRAY);
        shiftStartDatePicker.getJFormattedTextField().setForeground(ColorConstants.GOLD);
        addComponent("Shift Start Date", shiftStartDatePicker, c, 0, 1);

        // Shift End Date Picker
        DatePicker endDatePicker = new DatePicker();
        shiftEndDatePicker = endDatePicker.getDatePicker();
        shiftEndDatePicker.getJFormattedTextField().setBackground(ColorConstants.DARK_GRAY);
        shiftEndDatePicker.getJFormattedTextField().setForeground(ColorConstants.GOLD);
        addComponent("Shift End Date", shiftEndDatePicker, c, 0, 2);

     // Shift Start Time Field
        shiftStartTimePicker = new JFormattedTextField(new TimeFormatter());
        shiftStartTimePicker.setColumns(4);
        shiftStartTimePicker.setBackground(ColorConstants.DARK_GRAY);
        shiftStartTimePicker.setForeground(ColorConstants.GOLD);
        shiftStartTimePicker.setCaretColor(ColorConstants.GOLD);
        addComponent("Shift Start Time", shiftStartTimePicker, c, 0, 3);

        // Quick Set Button for Shift Start Time
        startTimeButton = new JButton("0800");
        startTimeButton.setBackground(ColorConstants.DEEP_BLUE);
        startTimeButton.setForeground(ColorConstants.LIME_GREEN);
        startTimeButton.addActionListener(e -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            shiftStartTimePicker.setValue(calendar.getTime());
        });
        addComponent("", startTimeButton, c, 1, 3);

     // Shift End Time Field
        shiftEndTimePicker = new JFormattedTextField(new TimeFormatter());
        shiftEndTimePicker.setColumns(4);
        shiftEndTimePicker.setBackground(ColorConstants.DARK_GRAY);
        shiftEndTimePicker.setForeground(ColorConstants.GOLD);
        shiftEndTimePicker.setCaretColor(ColorConstants.GOLD);

        addComponent("Shift End Time", shiftEndTimePicker, c, 0, 4);
        
        
        // Quick Set Button for Shift End Time
        endTimeButton = new JButton("0800");
        endTimeButton.setBackground(ColorConstants.DEEP_BLUE);
        endTimeButton.setForeground(ColorConstants.LIME_GREEN);
        endTimeButton.addActionListener(e -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            shiftEndTimePicker.setValue(calendar.getTime());
        });
        addComponent("", endTimeButton, c, 1, 4);

        // Overtime Comment Field
        overtimeCommentField = new TitledTextField("Overtime Comment", "", 20);
        addComponent("Reason for Overtime", overtimeCommentField, c, 0, 5);

        // Submit Button
        submitButton = new JButton("Submit");
        submitButton.setBackground(ColorConstants.DEEP_BLUE);
        submitButton.setForeground(ColorConstants.LIME_GREEN);
        submitButton.setEnabled(false);
        addComponent("", submitButton, c, 0, 6);

        // Cancel Button
        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(ColorConstants.CRIMSON_RED);
        cancelButton.setForeground(ColorConstants.WHITE);
        addComponent("", cancelButton, c, 1, 6);

        System.out.println("TimeSheetEntryPanel created");
        
        //document listeners for validation
        shiftStartDatePicker.getJFormattedTextField().getDocument().addDocumentListener(new ValidationListener(this::validateFields));
        shiftEndDatePicker.getJFormattedTextField().getDocument().addDocumentListener(new ValidationListener(this::validateFields));
        shiftStartTimePicker.getDocument().addDocumentListener(new ValidationListener(this::validateFields));
        shiftEndTimePicker.getDocument().addDocumentListener(new ValidationListener(this::validateFields));
        validateFields();
 }

    private void addComponent(String label, JComponent component, GridBagConstraints c, int row, int col) {
        c.gridx = col;
        c.gridy = row;
        if (!label.isEmpty()) {
            JLabel jLabel = new JLabel(label);
            jLabel.setForeground(ColorConstants.GOLD);
            add(jLabel, c);
        }
        c.gridy++;
        add(component, c);
    }
    
    public JButton getSubmitTimeSheetButton() {
    	return this.submitButton;
    }
    
    public JButton getCancelTimeSheetButton() {
    	return this.cancelButton;
    }
    
    public JComboBox<String> getEmployeeComboBox() {
    	return this.employeeNameComboBox;
    }
    
    public void setEmployeeNameList(List<String> employeeNames) {
    	this.employeeNames = employeeNames;
    	employeeNameComboBox.setModel(new DefaultComboBoxModel<>(employeeNames.toArray(new String[0])));
    	revalidate();
    	repaint();
    }
    //getters to provide data up the chain back to controller
    public String getSelectedEmployeeName() {
    	return (String) employeeNameComboBox.getSelectedItem();
    }
    public Date getShiftStartDate() {
    	return (Date) shiftStartDatePicker.getModel().getValue();
    }
    public Date getShiftEndDate() {
    	return (Date) shiftEndDatePicker.getModel().getValue();
    }
    public Time getShiftStartTime() {
        Date date = (Date) shiftStartTimePicker.getValue();
        return date != null ? new Time(date.getTime()) : null;
    }
    public Time getShiftEndTime() {
        Date date = (Date) shiftEndTimePicker.getValue();
        return date != null ? new Time(date.getTime()) : null;
    }
    public String getOverTimeComment() {
    	return overtimeCommentField.getText();
    }
 //setters for modifying a time sheet
    public void setSelectedEmployeeName(String employeeName) {
    	employeeNameComboBox.setSelectedItem(employeeName);
    }
    public void setShiftStartDate(Date shiftStartDate) {
        UtilDateModel model = (UtilDateModel) shiftStartDatePicker.getModel();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(shiftStartDate);
        model.setValue(calendar.getTime());
        model.setSelected(true);
    }

    public void setShiftEndDate(Date shiftEndDate) {
        UtilDateModel model = (UtilDateModel) shiftEndDatePicker.getModel();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(shiftEndDate);
        model.setValue(calendar.getTime());
        model.setSelected(true);
    }
    public void setShiftStartTime(Time shiftStartTime) {
    	shiftStartTimePicker.setValue(shiftStartTime);
    }
    public void setShiftEndTime(Time shiftEndTime) {
    	shiftEndTimePicker.setValue(shiftEndTime);
    }

    public void setOverTimeComment(String overtimeComment) {
    	overtimeCommentField.setText(overtimeComment);
    }
    private void validateFields() {
        Date shiftStartDate = getShiftStartDate();
        Date shiftEndDate = getShiftEndDate();
        Time shiftStartTime = getShiftStartTime();
        Time shiftEndTime = getShiftEndTime();

        boolean isValid = ValidationListener.validateFields(shiftStartDate, shiftEndDate, shiftStartTime, shiftEndTime);
        submitButton.setEnabled(isValid);

        if (!isValid) {
            showTemporaryDialog("Invalid entries detected. Please check the dates and times.", "Validation Error");
        }
    }

    private void showTemporaryDialog(String message, String title) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setSize(500, 150);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setBackground(ColorConstants.CHARCOAL);
        panel.setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setForeground(ColorConstants.GOLD);
        panel.add(messageLabel, BorderLayout.CENTER);

        dialog.add(panel);
        dialog.setVisible(true);

        // Close the dialog after 3 seconds
        Timer timer = new Timer(3000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
    }


    
}
