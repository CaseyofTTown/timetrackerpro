package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
	private JComboBox shiftStartTimePicker;
	private JComboBox shiftEndTimePicker;
	private TitledTextField overtimeCommentField;
	private JButton submitButton;
	private JButton startTimeButton;
	private JButton endTimeButton;
	private JButton cancelButton;
	private JLabel warningLabel;

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
		c.insets = new Insets(10, 10, 10, 10); // spacing of columns

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

		// Shift Start Time Picker
        shiftStartTimePicker = createTimeComboBox();
        addComponent("Shift Start Time", shiftStartTimePicker, c, 0, 3);

     // Quick Set Button for Shift Start Time
        startTimeButton = new JButton("0800");
        startTimeButton.setBackground(ColorConstants.DEEP_BLUE);
        startTimeButton.setForeground(ColorConstants.LIME_GREEN);
        startTimeButton.addActionListener(e -> {
            String startTime = "08:00";
            shiftStartTimePicker.setSelectedItem(startTime);
        });
        addComponent("", startTimeButton, c, 1, 3);

		 // Shift End Time Picker
        shiftEndTimePicker = createTimeComboBox();
        addComponent("Shift End Time", shiftEndTimePicker, c, 0, 4);
     // Quick Set Button for Shift End Time
        endTimeButton = new JButton("0800");
        endTimeButton.setBackground(ColorConstants.DEEP_BLUE);
        endTimeButton.setForeground(ColorConstants.LIME_GREEN);
        endTimeButton.addActionListener(e -> {
            String endTime = "08:00";
            shiftEndTimePicker.setSelectedItem(endTime);
        });
        addComponent("", endTimeButton, c, 1, 4);

		// Overtime Comment Field
		overtimeCommentField = new TitledTextField("Overtime Comment", "", 20);
		addComponent("Reason for Overtime", overtimeCommentField, c, 0, 5);

		// Warning Label
		warningLabel = new JLabel("Invalid entries detected. Please check all fields.");
		warningLabel.setForeground(ColorConstants.CRIMSON_RED);
		warningLabel.setVisible(false);
		addComponent("", warningLabel, c, 0, 6);

		// Submit Button
		submitButton = new JButton("Submit");
		submitButton.setBackground(ColorConstants.DEEP_BLUE);
		submitButton.setForeground(ColorConstants.LIME_GREEN);
		submitButton.setEnabled(false);
		addComponent("", submitButton, c, 0, 7);

		// Cancel Button
		cancelButton = new JButton("Cancel");
		cancelButton.setBackground(ColorConstants.CRIMSON_RED);
		cancelButton.setForeground(ColorConstants.WHITE);
		addComponent("", cancelButton, c, 1, 7);

		System.out.println("TimeSheetEntryPanel created");

		// document listeners for validation
		shiftStartDatePicker.getJFormattedTextField().getDocument()
				.addDocumentListener(new ValidationListener(this::validateFields));
		shiftEndDatePicker.getJFormattedTextField().getDocument()
				.addDocumentListener(new ValidationListener(this::validateFields));

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
	private JComboBox<String> createTimeComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 5) { // 5-minute intervals
                comboBox.addItem(String.format("%02d:%02d", hour, minute));
            }
        }
        comboBox.setBackground(ColorConstants.DARK_GRAY);
        comboBox.setForeground(ColorConstants.GOLD);
        return comboBox;
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

	// getters to provide data up the chain back to controller
	public String getSelectedEmployeeName() {
		return (String) employeeNameComboBox.getSelectedItem();
	}

	public Date getShiftStartDate() {
		return (Date) shiftStartDatePicker.getModel().getValue();
	}

	public Date getShiftEndDate() {
		return (Date) shiftEndDatePicker.getModel().getValue();
	}

	 public LocalTime getShiftStartTime() {
	        String timeString = (String) shiftStartTimePicker.getSelectedItem();
	        return LocalTime.parse(timeString);
	    }

	 public LocalTime getShiftEndTime() {
	        String timeString = (String) shiftEndTimePicker.getSelectedItem();
	        return LocalTime.parse(timeString);
	    }

	public String getOverTimeComment() {
		return overtimeCommentField.getText();
	}

	// setters for modifying a time sheet
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

	 public void setShiftStartTime(LocalTime shiftStartTime) {
	        shiftStartTimePicker.setSelectedItem(shiftStartTime.format(DateTimeFormatter.ofPattern("HH:mm")));
	    }

	 public void setShiftEndTime(LocalTime shiftEndTime) {
	        shiftEndTimePicker.setSelectedItem(shiftEndTime.format(DateTimeFormatter.ofPattern("HH:mm")));
	    }

	public void setOverTimeComment(String overtimeComment) {
		overtimeCommentField.setText(overtimeComment);
	}

	private boolean validateFields() {
		Date shiftStartDate = getShiftStartDate();
		Date shiftEndDate = getShiftEndDate();
		LocalTime shiftStartTime = getShiftStartTime();
		LocalTime shiftEndTime = getShiftEndTime();

		boolean allFieldsEntered = shiftStartDate != null && shiftEndDate != null && shiftStartTime != null
				&& shiftEndTime != null;
		boolean isValid = allFieldsEntered
				&& ValidationListener.validateFields(shiftStartDate, shiftEndDate, shiftStartTime, shiftEndTime);

		if (allFieldsEntered) {
			submitButton.setEnabled(isValid);
			warningLabel.setVisible(!isValid);
		} else {
			submitButton.setEnabled(false);
			warningLabel.setVisible(false);
		}

		return isValid;
	}

}
