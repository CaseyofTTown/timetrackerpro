package view;

import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.TTController;
import model.ColorConstants;
import model.TimeSheet;

import java.awt.*;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class HomeView extends JPanel {

	private JLabel welcomeLabel;
	private JTabbedPane tabbedPane;
	private TimeSheetPanel timeSheetPanel;
	private DailyCallLogPanel dailyCallLogPanel;
	private List<String> employeeNames;
	private TTController controller;

	// added as an update to simplify passing dates to other pages, original
	// architecture for TS page left in place, modified to set these
	private Date datePickerStartDate;
	private Date datePickerEndDate;

	public HomeView(String employeeName, TTController controller) {

		this.controller = controller;
		if (employeeNames == null) {
			employeeNames = new ArrayList<>();
		}

		// panel with BorderLayout
		setLayout(new BorderLayout());
		setBackground(ColorConstants.CHARCOAL);

		// Create a panel with a BorderLayout
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(ColorConstants.CHARCOAL);

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
		add(welcomePanel, BorderLayout.NORTH);

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
		add(tabbedPane, BorderLayout.CENTER);

		// Add a ChangeListener to the tabbedPane
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (tabbedPane.getSelectedIndex() == 1) { // Assuming "Call Logs" is the second tab (index 1)
					// TODO Fetch data when "Call Logs" tab is selected
				}
			}
		});

		System.out.println("homeView created");
		revalidate();
		repaint();

	}

	// Methods to create panels for each tab
	private JPanel createTimeSheetsPanel() {
		System.out.println("creating time sheet panel");
		timeSheetPanel = new TimeSheetPanel();
		timeSheetPanel.setBackground(ColorConstants.CHARCOAL);
		return timeSheetPanel;
	}

	private JPanel createCallLogsPanel() {
		dailyCallLogPanel = new DailyCallLogPanel(controller);
		dailyCallLogPanel.setBackground(ColorConstants.CHARCOAL);
		dailyCallLogPanel.setCrewMemberList(employeeNames);
		return dailyCallLogPanel;
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

	// methods to set the start and end dates on timesheetview and this class so it
	// can be passed to daily log panel
	public void setStartDate(Date date) {
		System.out.println();
		System.out.println("Start date variable set in homeView class");
		if (dailyCallLogPanel != null) {
			dailyCallLogPanel.setStartDate(date);
		} else {
			System.out.println("unable to set start date on dailyCallLogPanel, was null");
		}
		if (timeSheetPanel != null) {
			timeSheetPanel.setStartDate(date);
		} else {
			System.out.println("unable to set start date on dailyCallLogPanel, was null");
		}
	}

	public void setEndDate(Date date) {
		System.out.println("Start date variable set in homeView class");
		if (dailyCallLogPanel != null) {
			dailyCallLogPanel.setEndDate(date);
		} else {
			System.out.println("unable to set start date on dailyCallLogPanel, was null");
		}
		if (timeSheetPanel != null) {
			timeSheetPanel.setEndDate(date);
		} else {
			System.out.println("unable to set start date on dailyCallLogPanel, was null");
		}
	}

	// getters/setters to update timeSheetDisplay
	public void addAllTimeSheetsToDisplay(List<TimeSheet> timeSheets) {
		timeSheetPanel.addAllTimeSheetsToDisplay(timeSheets);
	}

	public void setEmployeeNameList(List<String> employeeNames) {
		this.employeeNames = employeeNames;
		// pass name list to time sheet panel
		timeSheetPanel.setEmployeeNameList(employeeNames);
		timeSheetPanel.revalidate();
		timeSheetPanel.repaint();
		System.out.println("names passed to time sheet panel = " + employeeNames.size());
	}

	public TimeSheetPanel getTimeSheetPanel() {
		return this.timeSheetPanel;
	}

	// getters for date range above time sheet display
	public JButton getUpdateDateRangeButton() {
		return timeSheetPanel.getUpdateDateRangeButton();
	}

	public Date getStartDateRangeForTs() {
		return timeSheetPanel.getStartDate();
	}

	public Date getEndDateRangeForTs() {
		return timeSheetPanel.getEndDate();
	}

	public void showAddNewTimeSheetUI() {
		timeSheetPanel.showAddNewTimeSheetPanel();
	}

	// getters to provide fields for a new time sheet entry on time sheet panel
	public String getSelectedEmployeeName() {
		return timeSheetPanel.getSelectedEmployeeName();
	}

	public Date getShiftStartDate() {
		return timeSheetPanel.getShiftStartDate();
	}

	public Date getShiftEndDate() {
		return timeSheetPanel.getShiftEndDate();
	}

	public LocalTime getShiftStartTime() {
		return timeSheetPanel.getShiftStartTime();
	}

	public LocalTime getShiftEndTime() {
		return timeSheetPanel.getShiftEndTime();
	}

	public String getOvertimeComment() {
		return timeSheetPanel.getOvertimeComment();
	}

	public JButton getTimeSheetSubmitButton() {
		return timeSheetPanel.getSubmitTimeSheetButton();
	}

	public JButton getCancelTimeSheetSubmissionButton() {
		return timeSheetPanel.getCancelTimeSheetButton();
	}

	public int getSelectedTimeSheetId() {
		return timeSheetPanel.getSelectedTimeSheetId();
	}

	// setters for addnewTimeSheet to allow modifying existing one
	public void setEmployeeNameOnTS(String employeeName) {
		timeSheetPanel.setEmployeeNameOnModTs(employeeName);
	}

	public void setShiftStartDateOnModTs(Date shiftStartDate) {
		timeSheetPanel.setShiftStartDateOnModTs(shiftStartDate);
	}

	public void setShiftEndDateOnModTs(Date shiftEndDate) {
		timeSheetPanel.setShiftEndDateOnModTs(shiftEndDate);
	}

	public void setShiftStartTimeOnModTs(LocalTime shiftStartTime) {
		timeSheetPanel.setShiftStartTimeOnModTs(shiftStartTime);
	}

	public void setShiftEndTimeOnModTs(LocalTime shiftEndTime) {
		timeSheetPanel.setShiftEndTimeOnModTs(shiftEndTime);
	}

	public void setOverTimeCommentOnModTs(String overtimeComment) {
		timeSheetPanel.setOverTimeCommentOnModTs(overtimeComment);
	}
}
