package controller;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.AmbulanceCall;
import model.CertificationLevelenum;
import model.DatabaseManager;
import model.Employee;
import model.Session;
import model.TimeSheet;
import model.UserAuth;
import view.viewClass;
import view.CustomAlertDialog;
import view.Login_Register_View;

public class TTController {

	// private DatabaseManager db;
	private viewClass view;
	// used to handle the actual registration/sign in
	private UserAuth userAuth;
	// used to automatically sign a user out after 2 hours
	private Session session;
	private int employeeId;
	private DatabaseManager db;

	// date ranges for time sheet display
	private java.sql.Date sqlStartDate;
	private java.sql.Date sqlEndDate;
	private boolean useAutoDateRangesForTsDisplay = true;
	private Employee employee;

	public TTController(DatabaseManager db, viewClass view) {
		this.userAuth = new UserAuth(db);
		this.view = view;
		this.session = new Session(); // used for timing out user
		this.db = db;

		// was used to update after db was created db.addHoursWorkedColumn();

		displayUserLoginRegisterUI();

	}

	private void displayUserLoginRegisterUI() {
		view.showLoginRegisterView();
		// add action listeners to the buttons in loginRegisterView
		this.view.getSignInButton().addActionListener(e -> handleSignIn());
		this.view.getRegisterButton().addActionListener(e -> handleRegister());

	}

	private void handleModifyTimeSheet() {
		int selectedTimeSheetId = view.getSelectedTimeSheetId();
		if (selectedTimeSheetId != -1) {
			// fetch data for selected time sheet

			TimeSheet timeSheet = db.getTimeSheetById(selectedTimeSheetId);
			view.showModifyTimeSheetView(timeSheet);
		} else {
			System.out.println("Time sheet with id: " + selectedTimeSheetId + " was not found");
		}
		// listeners for when modifying a time sheet entry
		this.view.getSubmitTimeSheetButton().addActionListener(e -> handleUpdateTimeSheetToDb(selectedTimeSheetId));
		this.view.getCancelTimeSheetButton().addActionListener(e -> handleHomeViewSetupAndNavigate());

	}

	private void showCreateNewTimeSheetUI() {
		System.out.println("getting user interface for new time sheet");
		view.showNewTimeSheetUI();

		// action listeners for buttons
		this.view.getSubmitTimeSheetButton().addActionListener(e -> handleSubmitTimeSheetToDb());
		// default function for now may update later TODO
		this.view.getCancelTimeSheetButton().addActionListener(e -> handleHomeViewSetupAndNavigate());
	}

	private void handleSubmitTimeSheetToDb() {
		// Retrieve values from the view
		String employeeName = view.getSelectedEmployeeOnTimeSheet();
		Date shiftStartDate = view.getShiftStartDateOnTs();
		Date shiftEndDate = view.getShiftEndDateOnTs();
		LocalTime shiftStartTime = view.getShiftStartTimeOnTs();
		LocalTime shiftEndTime = view.getShiftEndTimeOnTs();
		String overtimeComment = view.getOvertimeCommentsOnTs();

		// Get employee ID using the employee name
		int employeeId = db.getEmployeeIdByName(employeeName);
		if (employeeId == -1) {
			System.out.println("Employee not found: " + employeeName);
			return;
		}

		// Create a TimeSheet object
		TimeSheet timeSheet = new TimeSheet(employeeName, employeeId, shiftStartDate, shiftEndDate, shiftStartTime,
				shiftEndTime, overtimeComment);
		System.out.println(timeSheet);

		// Insert the TimeSheet object into the database
		db.addTimeSheet(timeSheet);
		view.getSubmitTimeSheetButton().setEnabled(false);

		// Optionally, reset the view or navigate back to the home screen
		handleHomeViewSetupAndNavigate();
	}

	private void handleUpdateTimeSheetToDb(int timeSheetId) {
		// Retrieve values from the view
		String employeeName = view.getSelectedEmployeeOnTimeSheet();
		Date shiftStartDate = view.getShiftStartDateOnTs();
		Date shiftEndDate = view.getShiftEndDateOnTs();
		LocalTime shiftStartTime = view.getShiftStartTimeOnTs();
		LocalTime shiftEndTime = view.getShiftEndTimeOnTs();
		String overtimeComment = view.getOvertimeCommentsOnTs();

		// Get employee ID using the employee name
		int employeeId = db.getEmployeeIdByName(employeeName);
		if (employeeId == -1) {
			System.out.println("Employee not found: " + employeeName);
			return;
		}

		// Create a TimeSheet object
		TimeSheet timeSheet = new TimeSheet(employeeName, employeeId, shiftStartDate, shiftEndDate, shiftStartTime,
				shiftEndTime, overtimeComment);
		timeSheet.setTimeSheetId(timeSheetId); // Set the ID of the timesheet to be updated
		System.out.println(timeSheet);

		// Update the TimeSheet object in the database
		db.updateTimeSheet(timeSheet);
		view.getSubmitTimeSheetButton().setEnabled(false);

		// Optionally, reset the view or navigate back to the home screen
		handleHomeViewSetupAndNavigate();
	}

	private void handleSignIn() {
		String username = view.getUsernameSignIn();
		String password = view.getPasswordSignIn();

		// authenticate user
		boolean isAuthenticated = userAuth.authenticateUser(username, password);

		if (isAuthenticated) {
			employeeId = db.getEmployeeIdByUsername(username);
			employee = db.getEmployeeById(employeeId);

			if (employee == null) {
				// no entry in employees table, collect info
				showNewEmployeeInfoUI();
			} else {
				// entry exists, going to homescreen
				view.hideLoginRegisterView();
				handleHomeViewSetupAndNavigate();

			}
		} else {
			// handle auth failure
			CustomAlertDialog.showDialog(view, "Invalid username or password", "Please try again");
		}
	}

	private void handleRegister() {
		String username = view.getUsernameRegister();
		String password = view.getPasswordRegister();
		int pin = view.getPinRegister();
		// if registration = success, close login_register, open new employee info
		boolean isRegistered = userAuth.registerUser(username, password, pin);

		if (isRegistered) {
			employeeId = db.getEmployeeIdByUsername(username);
			showNewEmployeeInfoUI();

		}
	}

	private void showNewEmployeeInfoUI() {
		view.hideLoginRegisterView();
		view.showNewEmployeeInfoView();
		// listener for button in NewEmployeeInfoView
		System.out.println("Subtmit new employee info button listener set from controller");
		this.view.getSubmitEmployeeInfoButton().addActionListener(e -> handleNewEmployeeSubmit());

	}

	private void handleNewEmployeeSubmit() {
		String name = view.getEmployeeName();
		String isEmsCertified = view.getIsEmsCertified();
		CertificationLevelenum certificationLevel;
		String certificationNumber = null;
		Date expirationDate = null;

		if (isEmsCertified.equals("Yes")) {
			certificationLevel = view.getCertificationLevel();
			certificationNumber = view.getEmsCertificationNumber();
			expirationDate = new Date(view.getExpirationDate().getTime());
		} else {
			certificationLevel = CertificationLevelenum.DRIVER;
		}

		employee = new Employee(employeeId, name, certificationLevel, certificationNumber, expirationDate);

		boolean isStored = false;
		try {
			isStored = db.addEmployee(employee);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if (isStored) {
			System.out.print("Employee added to db");
			view.hideNewEmployeeInfoView();
			view.hideLoginRegisterView();
			handleHomeViewSetupAndNavigate();
		}

	}

	private void initiallyProvideDateRangeForTimeSheets() {
		// Calculate date range
		java.sql.Date[] recentDates = calculateRecentDatesForTimeSheets();
		sqlStartDate = recentDates[0];
		sqlEndDate = recentDates[1];

		// Debug statements
		System.out.println("Calculated Start Date: " + sqlStartDate);
		System.out.println("Calculated End Date: " + sqlEndDate);
	}
	private void updateDateRange() {
		//get date range from user
		Date newStartDate = view.getStartDateRangeForTs();
		Date newEndDate = view.getEndDateRangeForTs();
		//convert util date to sql date 
		java.sql.Date sqlNewStartDate = new java.sql.Date(newStartDate.getTime());
		java.sql.Date sqlNewEndDate = new java.sql.Date(newEndDate.getTime());
		
		//update instance variables
		sqlStartDate = sqlNewStartDate;
		sqlEndDate = sqlNewEndDate;
		
		useAutoDateRangesForTsDisplay = false;
		
		//update teh date range in the view 
		view.setStartDate(newStartDate);
		view.setEndDate(newEndDate);
		
		//refresh view with new date ragne
		handleHomeViewSetupAndNavigate();
	}

	private void handleHomeViewSetupAndNavigate() {

		if (useAutoDateRangesForTsDisplay) {
			initiallyProvideDateRangeForTimeSheets();
		}

		// Fetch time sheets within the date range
		List<TimeSheet> timeSheets = db.getTimeSheetsByDateRange(sqlStartDate, sqlEndDate);
		List<String> employeeNames = db.getAllEmployeeNames();

		// Debug statements
		System.out.println("Fetched " + timeSheets.size() + " time sheets from the database.");
		System.out.println("Fetched " + employeeNames.size() + " employee names from the database.");

		view.showHomeView(employee.getName());
		view.setEmployeeNameList(employeeNames);

		// Update the view with the fetched time sheets
		view.updateTimeSheetDisplay(timeSheets);
		view.setStartDate(new Date(sqlStartDate.getTime()));
		view.setEndDate(new Date(sqlEndDate.getTime()));
		
		this.view.getUpdateDateRangeButton().addActionListener(e -> updateDateRange());

		// listeners for timeSheetTab on HomeView
		this.view.getTimeSheetPanel().getAddButton().addActionListener(e -> showCreateNewTimeSheetUI());
		this.view.getTimeSheetPanel().getModifyButton().addActionListener(e -> handleModifyTimeSheet());
		this.view.getTimeSheetPanel().getDeleteTimeSheetButton().addActionListener(e -> handleDeleteTimeSheetFromDb());
		// -> handleDeleteTimeSheet());
		System.out.println("Listeners set for TimeSheetPanel");
	}

	private void handleDeleteTimeSheetFromDb() {
		int selectedTimeSheetId = view.getSelectedTimeSheetId();
		if (selectedTimeSheetId != -1) {
			// Delete the time sheet from the database
			db.deleteTimeSheet(selectedTimeSheetId);
			System.out.println("Time sheet with id: " + selectedTimeSheetId + " has been deleted.");

			// Optionally, reset the view or navigate back to the home screen
			handleHomeViewSetupAndNavigate();
		} else {
			System.out.println("Time sheet with id: " + selectedTimeSheetId + " was not found.");
		}
	}

	private java.sql.Date[] calculateRecentDatesForTimeSheets() {
		Calendar calendar = Calendar.getInstance();
		Date endDate = calendar.getTime();
		calendar.add(Calendar.MONTH, -1);
		Date startDate = calendar.getTime();

		// Convert java.util.Date to java.sql.Date
		java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
		java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());

		return new java.sql.Date[] { sqlStartDate, sqlEndDate };
	}

	//handleAmbulanceCalls
	public void deleteAmbulanceCall(AmbulanceCall call) {
		// TODO Auto-generated method stub
		
	}

}
