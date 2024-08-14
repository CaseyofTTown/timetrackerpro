package view;

import javax.swing.*;
import java.util.List;
import org.jdatepicker.impl.JDatePickerImpl;
import model.ColorConstants;
import model.TimeSheet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TimeSheetPanel extends JPanel {

	private JButton addButton;
	private JButton modifyButton;
	private JButton deleteTimeSheetButton;
	private JButton updateTimeSheetDisplayDateRanges;
	private TimeSheetDisplay timeSheetDisplay;
	private JDatePickerImpl startDatePicker;
	private JDatePickerImpl endDatePicker;
	private TimeSheetEntryPanel timeSheetEntryPanel;
	private List<String> employeeNames;
	private JSplitPane splitPane;

	public TimeSheetPanel() {

		if (employeeNames == null) {
			employeeNames = new ArrayList<>();
		}

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
		
		//update button
		updateTimeSheetDisplayDateRanges = new JButton("Update Date Range");
		updateTimeSheetDisplayDateRanges.setBackground(ColorConstants.SLATE_GRAY);
		updateTimeSheetDisplayDateRanges.setForeground(ColorConstants.GOLD);
		updateTimeSheetDisplayDateRanges.setFont(new Font("Arial", Font.BOLD, 14));

		datePanel.add(startDatePicker);
		datePanel.add(endDatePicker);
		datePanel.add(updateTimeSheetDisplayDateRanges);

		add(datePanel, BorderLayout.NORTH);

		// TimeSheetDisplay Component
		timeSheetDisplay = new TimeSheetDisplay();
		timeSheetDisplay.setBackground(ColorConstants.DARK_GRAY);

		JScrollPane scrollPane = new JScrollPane(timeSheetDisplay);
		scrollPane.getViewport().setBackground(ColorConstants.DARK_GRAY); // Set the viewport background
		scrollPane.setBackground(ColorConstants.DARK_GRAY);

		// TimeSheetEntryPanel (initially hidden)
		timeSheetEntryPanel = new TimeSheetEntryPanel(employeeNames);
		timeSheetEntryPanel.setVisible(false);

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, timeSheetEntryPanel);
		splitPane.setResizeWeight(0.8); // Adjust size weight
		splitPane.setBackground(ColorConstants.DARK_GRAY);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(300);
		add(splitPane, BorderLayout.CENTER);


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

		// Add the MouseListener to the TimeSheetDisplay to handle deselection instead of getSelectionModel()
		timeSheetDisplay.addMouseListener(new MouseAdapter() {
		    private int selectedRow = -1;

		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int currentSelectedRow = timeSheetDisplay.rowAtPoint(e.getPoint());
		        System.out.println("Mouse clicked on row: " + currentSelectedRow);

		        if (currentSelectedRow == selectedRow) {
		            // Deselect the row if it is already selected
		            System.out.println("same row selected, calling clearSelection()");
		            timeSheetDisplay.clearSelection();
		            selectedRow = -1;
		        } else {
		            selectedRow = currentSelectedRow;
		        }

		        boolean isSelected = selectedRow != -1;
		        modifyButton.setEnabled(isSelected);
		        deleteTimeSheetButton.setEnabled(isSelected);
		        System.out.println("Final selectedRow: " + selectedRow);
		    }
		});


		System.out.println("TimeSheetPanel created");
		revalidate();
		repaint();

		// added for debugging graphics on TimeSheetDisplay
		SwingUtilities.invokeLater(() -> {
			Graphics g = timeSheetDisplay.getGraphics();
			if (g != null) {
				System.out.println("calling graphics on timesheetDisplay from panel class");
				try {
					timeSheetDisplay.paintComponent(g);
					System.out.println("timeSheetDisplay.paint()");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} else {
				System.out.println("timeSheetDisplay graphics were null");
			}
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
	//getters for date fields for time sheet display range
	public Date getStartDate() {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(startDatePicker.getModel().getYear(), startDatePicker.getModel().getMonth(),
	            startDatePicker.getModel().getDay());
	    return calendar.getTime();
	}

	public Date getEndDate() {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(endDatePicker.getModel().getYear(), endDatePicker.getModel().getMonth(),
	            endDatePicker.getModel().getDay());
	    return calendar.getTime();
	}
	//getter for update date range button
	public JButton getUpdateDateRangeButton() {
	    return updateTimeSheetDisplayDateRanges;
	}



	public JDatePickerImpl getStartdatePicker() {
		return startDatePicker;
	}

	public JDatePickerImpl getEndDatePicker() {
		return endDatePicker;
	}

	public int getSelectedTimeSheetId() {
		return timeSheetDisplay.getSelectedTimeSheetId();
	}

	public void setStartDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		startDatePicker.getModel().setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		startDatePicker.getModel().setSelected(true);
	}

	public void setEndDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		endDatePicker.getModel().setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		endDatePicker.getModel().setSelected(true);
	}

	public void setEmployeeNameList(List<String> employeeNames) {
		this.employeeNames = employeeNames;
		this.timeSheetEntryPanel.setEmployeeNameList(employeeNames);
	}

	public void showAddNewTimeSheetPanel() {
		splitPane.setDividerLocation(0.5);
		timeSheetEntryPanel.setVisible(true);
	}

	// getters for TimeSheetEntryPanel to allow view.controller to create a new TS
	public String getSelectedEmployeeName() {
		return timeSheetEntryPanel.getSelectedEmployeeName();
	}
	public Date getShiftStartDate() {
		return timeSheetEntryPanel.getShiftStartDate();
	}
	public Date getShiftEndDate() {
		return timeSheetEntryPanel.getShiftEndDate();
	}
	public LocalTime getShiftStartTime() {
		return timeSheetEntryPanel.getShiftStartTime();
	}
	public LocalTime getShiftEndTime() {
		return timeSheetEntryPanel.getShiftEndTime();
	}
	public String getOvertimeComment() {
		return timeSheetEntryPanel.getOverTimeComment();
	}
	public JButton getSubmitTimeSheetButton() {
		return timeSheetEntryPanel.getSubmitTimeSheetButton();
	}
	public JButton getCancelTimeSheetButton() {
		return timeSheetEntryPanel.getCancelTimeSheetButton();
	}
	public JPanel getTimeSheetEntryPanel() {
		return timeSheetEntryPanel;
	}
	
	//getters/setters to update timeSheetDisplay
	public void addAllTimeSheetsToDisplay(List<TimeSheet> timeSheets) {
		timeSheetDisplay.addAllTimeSheetsToDisplay(timeSheets);
	}
	
	//setters to modify a time sheet
	public void setEmployeeNameOnModTs(String employeeName) {
		timeSheetEntryPanel.setSelectedEmployeeName(employeeName);
	}
	public void setShiftStartDateOnModTs(Date shiftStartDate) {
		timeSheetEntryPanel.setShiftStartDate(shiftStartDate);
	}
	public void setShiftEndDateOnModTs(Date shiftEndDate) {
		timeSheetEntryPanel.setShiftEndDate(shiftEndDate);
	}
	public void setShiftStartTimeOnModTs(LocalTime shiftStartTime) {
		timeSheetEntryPanel.setShiftStartTime(shiftStartTime);
	}
	public void setShiftEndTimeOnModTs(LocalTime shiftEndTime) {
		timeSheetEntryPanel.setShiftEndTime(shiftEndTime);
	}
	public void setOverTimeCommentOnModTs(String overtimeComment) {
		timeSheetEntryPanel.setOverTimeComment(overtimeComment);
	}
	

}