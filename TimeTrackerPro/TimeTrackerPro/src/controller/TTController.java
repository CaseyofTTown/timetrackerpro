package controller;

import model.DatabaseManager;
import view.viewClass;
import view.Login_Register_View;

public class TTController {
	
	private DatabaseManager db;
	private viewClass view;
	
	public TTController(DatabaseManager db, viewClass view) {
		this.db = db;
		this.view = view;
		
		//add action listeners to the buttons in loginRegisterView
		this.view.getSignInButton().addActionListener(e -> handleSignIn());
		this.view.getRegisterButton().addActionListener(e -> handleRegister());
		
		
	}
	
	private void handleSignIn() {
		
	}
	
	private void handleRegister() {
		
	}

}
