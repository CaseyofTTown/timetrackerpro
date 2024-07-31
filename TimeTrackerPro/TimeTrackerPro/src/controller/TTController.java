package controller;

import model.DatabaseManager;
import model.Session;
import model.UserAuth;
import view.viewClass;
import view.Login_Register_View;

public class TTController {
	
	//private DatabaseManager db;
	private viewClass view;
	//used to handle the actual registration/sign in
	private UserAuth userAuth;
	//used to automatically sign a user out after 2 hours 
	private Session session;
	
	
	public TTController(DatabaseManager db, viewClass view) {
		this.userAuth = new UserAuth(db);
		this.view = view;
		this.session = new Session(); //used for timing out user
		
		//add action listeners to the buttons in loginRegisterView
		this.view.getSignInButton().addActionListener(e -> handleSignIn());
		this.view.getRegisterButton().addActionListener(e -> handleRegister());
		
		
	}
	
	private void handleSignIn() {
		
	}
	
	private void handleRegister() {
		String username = view.getUsernameRegister();
		String password = view.getPasswordRegister();
		int pin = view.getPinRegister();
		//if registration = success, close login_register, open new employee info
		boolean isRegistered = userAuth.registerUser(username, password, pin);
		
		if(isRegistered) {
			view.dispose(); //close view
			
			//TODO: create and show new_employee_info_view
			
		}
	}

}
