package view;

import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.TTController;
import model.CertificationLevelenum;
import model.ColorConstants;
import model.Employee;
import model.TimeSheet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class HomeView extends JPanel {

	private JLabel welcomeLabel;
	private JTabbedPane tabbedPane;
	private TimeSheetPanel timeSheetPanel;
	private DailyCallLogPanel dailyCallLogPanel;
	private List<String> employeeNames;
	private TTController controller;
	private JLabel clockLabel;
	private JLabel employeeInfoLabel;
	private Employee employee;
	private JButton updateInfoButton;
	private JPanel medicationSignOutPanel;
	private JPanel reportsPanel;

	// added as an update to simplify passing dates to other pages, original
	// architecture for TS page left in place, modified to set these
	private Date datePickerStartDate;
	private Date datePickerEndDate;

	public HomeView(Employee employee, TTController controller) {
		this.controller = controller;
		this.employee = employee;
		if (employeeNames == null) {
			System.out.println("employeeNames was null in HomeView, creating new list");
			employeeNames = controller.getEmployeeListFromDb();
		}

		// panel with BorderLayout
		setLayout(new BorderLayout());
		setBackground(ColorConstants.CHARCOAL);

		// Create a panel with a GridBagLayout for precise control
		JPanel welcomePanel = new JPanel(new GridBagLayout());
		welcomePanel.setBackground(ColorConstants.DARK_GRAY);
		welcomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		String employeeName = employee.getName();
		// Create a label for the welcome message
		welcomeLabel = new JLabel("Welcome, " + employeeName + "!", SwingConstants.LEFT);
		welcomeLabel.setForeground(ColorConstants.GOLD);
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1.0;
		welcomePanel.add(welcomeLabel, gbc);

		// Create a label for the clock
		clockLabel = new JLabel();
		clockLabel.setForeground(ColorConstants.ORANGE);
		clockLabel.setFont(new Font("Arial", Font.BOLD, 24));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1.0;
		welcomePanel.add(clockLabel, gbc);

		// Create a label for the employee info
		employeeInfoLabel = new JLabel();
		employeeInfoLabel.setForeground(ColorConstants.LIME_GREEN);
		employeeInfoLabel.setFont(new Font("Arial", Font.BOLD, 24));
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = 1.0;
		welcomePanel.add(employeeInfoLabel, gbc);
		
		updateInfoButton = new JButton();
		updateInfoButton.setBackground(ColorConstants.DEEP_BLUE);
		updateInfoButton.setForeground(ColorConstants.LIME_GREEN);
		updateInfoButton.setFont(new Font("Arial", Font.BOLD, 18));
		if(employee.getCertLevel() != CertificationLevelenum.DRIVER) {
			updateInfoButton.setText("Update Cert");
		} else {
			updateInfoButton.setText("Add Certification");
		}
		gbc.gridx = 3;
		gbc.gridy  = 0;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.EAST;
		welcomePanel.add(updateInfoButton, gbc);

		// Add the welcome panel to the NORTH region of the main panel
		add(welcomePanel, BorderLayout.NORTH);

		// Create the tabbed pane
		tabbedPane = new JTabbedPane();
		tabbedPane.setBackground(ColorConstants.DARK_GRAY);
		tabbedPane.setForeground(ColorConstants.LIME_GREEN);
		// Add tabs with standard components
		tabbedPane.addTab("Time Sheets", createTimeSheetsPanel(controller));
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
		
		updateInfoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showUiToUpdateEmployeeInformation(employee);
			}
		});

		initializeClock();
		setEmployeeInfo(this.employee);

		System.out.println("homeView created");
		revalidate();
		repaint();

	}

	// Methods to create panels for each tab
	private JPanel createTimeSheetsPanel(TTController controller) {
		System.out.println("creating time sheet panel");
		timeSheetPanel = new TimeSheetPanel(controller);
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
		medicationSignOutPanel = new MedicationSignOutPanel();
		medicationSignOutPanel.setBackground(ColorConstants.CHARCOAL);
		return medicationSignOutPanel;
	}

	private JPanel createReportsPanel() {
		reportsPanel = new ReportPanel(controller);
		reportsPanel.setBackground(ColorConstants.CHARCOAL);
		return reportsPanel;
	}
	
	//method for to quickly start a log from a TS
	public void switchToLogPanelandEnterLog(Date startDate, Date endDate) {
		tabbedPane.setSelectedComponent(dailyCallLogPanel);
		dailyCallLogPanel.addNewCallLogFromHotkeyButton(startDate, endDate);
	}

	// for elements on the header of the main page
	private void initializeClock() {
		Timer timer = new Timer(250, e -> {
			LocalTime now = LocalTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
			clockLabel.setText("Time: " + now.format(formatter));
		});
		timer.start();
	}

	public void setEmployeeInfo(Employee employee) {
		if (employee != null) {
			String level = employee.getCertLevel().toString();
			String infoText;

			if ("Driver".equalsIgnoreCase(level)) {
				infoText = "Level: Driver";
				employeeInfoLabel.setForeground(ColorConstants.ORANGE);
			} else {
				int daysUntilExpiration = employee.calculateDaysUntilExpiration();
				if (daysUntilExpiration >= 0) {
					infoText = "Days until certification expires: " + daysUntilExpiration;
					employeeInfoLabel.setForeground(ColorConstants.LIME_GREEN);
				} else {
					infoText = "Error calculating expiration date.";
					employeeInfoLabel.setForeground(ColorConstants.CRIMSON_RED);
				}
			}

			employeeInfoLabel.setText(infoText);
		} else {
			employeeInfoLabel.setText("Employee information not available.");
			employeeInfoLabel.setForeground(ColorConstants.CRIMSON_RED);
		}
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
		System.out.println("setting employeeList in homeView through setEmployeeNameList function");
		// pass name list to time sheet panel
		timeSheetPanel.setEmployeeNameList(employeeNames);
		timeSheetPanel.revalidate();
		timeSheetPanel.repaint();
		System.out.println("names passed to time sheet panel = " + employeeNames.size());
		if (dailyCallLogPanel != null) {
			dailyCallLogPanel.setCrewMemberList(employeeNames);
			System.out
					.println("crew list for daiyl logs updated w/ employee names through setEmplNameList in homeView");
		} else {
			System.out.println("dailyCallLogPanel was null when setEmployeeNameList was called");
		}

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
