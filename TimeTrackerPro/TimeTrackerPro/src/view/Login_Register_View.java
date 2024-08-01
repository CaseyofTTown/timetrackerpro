package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Login_Register_View extends JFrame {

	private JButton signInButton;
	private JButton registerButton;
	private TitledTextField usernameSignIn;
	private JPasswordField passwordSignIn;
	private TitledTextField usernameRegister;
	private JPasswordField passwordRegister;
	private JPasswordField passwordRegisterConfirm;
	private TitledTextField pinRegister;
	private JLabel passwordWarningLabel;

	public Login_Register_View() {
		// set the window title
		setTitle("Login");

		// set default close operation
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// create a panel with a BorderLayout
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(new Color(50, 50, 50));

		// add the panel to the frame
		add(panel);

		// create a panel for the welcome messages
		JPanel welcomePanel = new JPanel();
		welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
		welcomePanel.setBackground(new Color(50, 50, 50));

		// create a label for the welcome message
		JLabel welcomeLabel = new JLabel("Welcome to Time Tracker Pro!", SwingConstants.CENTER);
		welcomeLabel.setForeground(Color.GREEN);
		welcomePanel.add(welcomeLabel);

		// create a label for the sign-in message
		JLabel signInLabel = new JLabel("Please sign in to continue :)", SwingConstants.CENTER);
		signInLabel.setForeground(Color.GREEN);
		welcomePanel.add(signInLabel);

		// add the welcome panel to the NORTH region of the main panel
		panel.add(welcomePanel, BorderLayout.NORTH);

		// create a tabbed pane for sign-in and registration options
		JTabbedPane tabbedPane = new JTabbedPane();

		// create the sign-in panel
		JPanel signInPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new java.awt.Insets(5, 5, 5, 5); // Add padding
		signInPanel.add(new JLabel("Username:"), c);
		c.gridy++;

		// Use TitledTextField for username
		usernameSignIn = new TitledTextField("Username", "Enter your username", 20);
		signInPanel.add(usernameSignIn, c);
		c.gridy++;
		signInPanel.add(new JLabel("Password:"), c);
		c.gridy++;

		// initialize password field with an empty string
		passwordSignIn = new JPasswordField("", 20);
		signInPanel.add(passwordSignIn, c);
		c.gridy++;

		signInButton = new JButton("Sign In");
		signInButton.setEnabled(false); // Initially disabled
		signInPanel.add(signInButton, c);
		tabbedPane.addTab("Sign In", signInPanel);

		// create the registration panel
		JPanel registerPanel = new JPanel(new GridBagLayout());
		c.gridy = 0;
		registerPanel.add(new JLabel("Username:"), c);
		c.gridy++;

		// Use TitledTextField for username
		usernameRegister = new TitledTextField("Username", "Enter your username", 20);
		registerPanel.add(usernameRegister, c);
		c.gridy++;
		registerPanel.add(new JLabel("Password:"), c);
		c.gridy++;
		passwordRegister = new JPasswordField("", 20);
		registerPanel.add(passwordRegister, c);
		c.gridy++;
		registerPanel.add(new JLabel("Confirm Password:"), c);
		c.gridy++;
		passwordRegisterConfirm = new JPasswordField("", 20);
		registerPanel.add(passwordRegisterConfirm, c);
		c.gridy++;
		registerPanel.add(new JLabel("PIN (last 4 digits of SSN):"), c);
		c.gridy++;

		// Use TitledTextField for PIN
		pinRegister = new TitledTextField("PIN", "Enter your PIN", 20);
		registerPanel.add(pinRegister, c);
		c.gridy++;
		registerButton = new JButton("Register");
		registerButton.setEnabled(false); // Initially disabled
		registerPanel.add(registerButton, c);
		c.gridy++;

		// warning label for passwords
		passwordWarningLabel = new JLabel("Passwords do not match!");
		passwordWarningLabel.setForeground(Color.RED);
		passwordWarningLabel.setVisible(false);
		registerPanel.add(passwordWarningLabel, c);
		c.gridy++;

		registerPanel.add(new JLabel(" "), c); // padding
		tabbedPane.addTab("Register", registerPanel);

		// add the tabbed pane to the panel
		panel.add(tabbedPane, BorderLayout.CENTER);

		// pack the frame, which will take into account the preferred size of its
		// contents
		pack();

		// center the frame on the screen
		setLocationRelativeTo(null);

		// Add document listeners to enable/disable buttons based on input
		DocumentListener documentListener = new DocumentListener() {
			public void changedUpdate(DocumentEvent documentEvent) {
				checkSignInFields();
				checkRegisterFields();
			}

			public void insertUpdate(DocumentEvent documentEvent) {
				checkSignInFields();
				checkRegisterFields();
			}

			public void removeUpdate(DocumentEvent documentEvent) {
				checkSignInFields();
				checkRegisterFields();
			}
		};

		usernameSignIn.getTextField().getDocument().addDocumentListener(documentListener);
		passwordSignIn.getDocument().addDocumentListener(documentListener);
		usernameRegister.getTextField().getDocument().addDocumentListener(documentListener);
		passwordRegister.getDocument().addDocumentListener(documentListener);
		passwordRegisterConfirm.getDocument().addDocumentListener(documentListener);
		pinRegister.getTextField().getDocument().addDocumentListener(documentListener);
	}

	private void checkSignInFields() {
		boolean isSignInEnabled = !usernameSignIn.getText().trim().isEmpty() && passwordSignIn.getPassword().length > 0;
		signInButton.setEnabled(isSignInEnabled);
	}

	private void checkRegisterFields() {
		boolean passwordsMatch = new String(passwordRegister.getPassword())
				.equals(new String(passwordRegisterConfirm.getPassword()));
		boolean isRegisterEnabled = !usernameRegister.getText().trim().isEmpty()
				&& passwordRegister.getPassword().length > 0 && passwordRegisterConfirm.getPassword().length > 0
				&& pinRegister.getText().matches("\\d{4}") && passwordsMatch;
		registerButton.setEnabled(isRegisterEnabled);

		// Show or hide the password warning label
		passwordWarningLabel.setVisible(!passwordsMatch);
	}

	// Getters to provide buttons to view so it can to controller
	public JButton getSignInButton() {
		return signInButton;
	}

	public JButton getRegisterButton() {
		return registerButton;
	}

	public void addSignInButtonListener(ActionListener listenForSignInButton) {
		signInButton.addActionListener(listenForSignInButton);
	}

	public void addRegisterButtonListener(ActionListener listenForRegisterButton) {
		registerButton.addActionListener(listenForRegisterButton);
	}

	public String getUsernameSignIn() {
		return usernameSignIn.getText();
	}

	public String getPasswordSignIn() {
		return new String(passwordSignIn.getPassword());
	}

	public String getUsernameRegister() {
		return usernameRegister.getText();
	}

	public String getPasswordRegister() {
		return new String(passwordRegister.getPassword());
	}

	public int getPinRegister() {
		String pinText = pinRegister.getText();
		try {
			return Integer.parseInt(pinText);
		} catch (NumberFormatException e) {
			System.out.println("Invalid PIN format!");
			return -1;
		}
	}

}
