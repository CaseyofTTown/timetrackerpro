package controller;

import java.util.Date;

import model.CertificationLevelenum;
import model.DatabaseManager;
import model.Employee;
import model.Session;
import model.UserAuth;
import view.viewClass;
import view.CustomAlertDialog;
import view.Login_Register_View;

public class TTController {
	
	//private DatabaseManager db;
	private viewClass view;
	//used to handle the actual registration/sign in
	private UserAuth userAuth;
	//used to automatically sign a user out after 2 hours 
	private Session session;
	private int employeeId;
	private DatabaseManager db;
	
	
	public TTController(DatabaseManager db, viewClass view) {
		this.userAuth = new UserAuth(db);
		this.view = view;
		this.session = new Session(); //used for timing out user
		this.db = db;
		//add action listeners to the buttons in loginRegisterView
		this.view.getSignInButton().addActionListener(e -> handleSignIn());
		this.view.getRegisterButton().addActionListener(e -> handleRegister());
		
		//listener for button in NewEmployeeInfoView
		this.view.getSubmitEmployeeInfoButton().addActionListener(e -> handleNewEmployeeSubmit());
		
		
	}
	
	private void handleSignIn() {
		String username = view.getUsernameSignIn();
		String password = view.getPasswordSignIn();
		
		//authenticate user
		boolean isAuthenticated = userAuth.authenticateUser(username, password);
		
		if(isAuthenticated) {
			employeeId = db.getEmployeeIdByUsername(username);
			Employee employee = db.getEmployeeById(employeeId);
			
			if(employee == null) {
				//no entry in employees table, collect info
				view.hideLoginRegisterView();
				view.showNewEmployeeInfoView();
			} else {
				//entry exists, going to homescreen
				view.hideLoginRegisterView();
				view.showHomeView(employee.getName());
			}
		} else {
			//handle auth failure
			CustomAlertDialog.showDialog(view, "Invalid username or password", "Please try again");
		}
	}
	
	private void handleRegister() {
		String username = view.getUsernameRegister();
		String password = view.getPasswordRegister();
		int pin = view.getPinRegister();
		//if registration = success, close login_register, open new employee info
		boolean isRegistered = userAuth.registerUser(username, password, pin);
		
		if(isRegistered) {
			employeeId = db.getEmployeeIdByUsername(username);
			view.hideLoginRegisterView();
			view.showNewEmployeeInfoView();
			
		}
	}
	private void handleNewEmployeeSubmit() {
		String name = view.getEmployeeName();
		String isEmsCertified = view.getIsEmsCertified();
		CertificationLevelenum certificationLevel;
		String certificationNumber = null;
		Date expirationDate = null;
		
		if(isEmsCertified.equals("Yes")) {
			certificationLevel = view.getCertificationLevel();
			certificationNumber = view.getEmsCertificationNumber();
			expirationDate = new Date(view.getExpirationDate().getTime());
		} else {
			certificationLevel = CertificationLevelenum.DRIVER;
		}
		
		Employee employee = new Employee(employeeId, name, certificationLevel, certificationNumber, expirationDate);
		
		boolean isStored = db.addEmployee(employee);
		
		if (isStored) {
			view.hideNewEmployeeInfoView();
			view.showHomeView(employee.getName());
		}
		
		
		
	}

}
