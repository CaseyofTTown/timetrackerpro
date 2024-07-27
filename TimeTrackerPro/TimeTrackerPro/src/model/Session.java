package model;

import java.time.Instant;

//class for GUI to know status of user (signed in/out)
public class Session {
	private String loggedInUser;
	private Instant lastActivity;
	
	public Session() {
		
	}
	
	public void login(String username) {
		loggedInUser = username;
		updateActivity();
	}
	
	public void logout() {
		loggedInUser = null;
		lastActivity = null;
		System.out.println("User logged out from Session");
	}
	
	public String getLoggedInUser() {
		return loggedInUser;
	}
	
	public boolean isLoggedIn() {
		return loggedInUser != null;
	}
	//call whenever a user performs an action to keep them signed in!
	public void updateActivity() {
		lastActivity = Instant.now();
		System.out.println("lastActivity updated");
	}
	public Instant getLastActivity() {
		return lastActivity;
	}

}
