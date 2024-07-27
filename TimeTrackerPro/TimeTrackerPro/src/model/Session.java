package model;

import java.time.Duration;
import java.time.Instant;

//class for GUI to know status of user (signed in/out)
public class Session {
	private String loggedInUser;
	private Instant lastActivity;
	private static final Duration SESSION_TIMEOUT = Duration.ofHours(2);

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

	/**
	 * checks to see if a user is logged in and auto logs out w/ isSessionExpired
	 * 
	 * @return
	 */
	public boolean isLoggedIn() {
		if (loggedInUser != null && !isSessionExpired()) {
			return true;
		} else {
			logout();
			return false;
		}
	}

	public boolean isSessionExpired() {
		return Duration.between(lastActivity, Instant.now()).compareTo(SESSION_TIMEOUT) > 0;
	}

	// call whenever a user performs an action to keep them signed in!
	public void updateActivity() {
		lastActivity = Instant.now();
		System.out.println("lastActivity updated");
	}

	public Instant getLastActivity() {
		return lastActivity;
	}

}
