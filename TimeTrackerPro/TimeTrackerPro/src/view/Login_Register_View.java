package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
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

import model.ColorConstants;

public class Login_Register_View extends JFrame {

	private JButton signInButton;
	private JButton registerButton;
	private TitledTextField usernameSignIn;
	private TitledPasswordField passwordSignIn;
	private TitledTextField usernameRegister;
	private TitledPasswordField passwordRegister;
	private TitledPasswordField passwordRegisterConfirm;
	private TitledTextField pinRegister;
	private JLabel passwordWarningLabel;
	private JButton resetPassword;

	public Login_Register_View() {
		// set the window title
		setTitle("Login");

		// set default close operation
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// create a panel with a BorderLayout
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(ColorConstants.CHARCOAL);

		// add the panel to the frame
		add(panel);

		// create a panel for the welcome messages
		JPanel welcomePanel = new JPanel();
		welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
		welcomePanel.setBackground(ColorConstants.DARK_GRAY);

		// create a label for the welcome message
		JLabel welcomeLabel = new JLabel("Welcome to Time Tracker Pro!", SwingConstants.CENTER);
		welcomeLabel.setForeground(ColorConstants.GOLD);
		welcomePanel.add(welcomeLabel);

		// create a label for the sign-in message
		JLabel signInLabel = new JLabel("Please sign in to continue :)", SwingConstants.CENTER);
		signInLabel.setForeground(ColorConstants.GOLD);
		welcomePanel.add(signInLabel);

		// add the welcome panel to the NORTH region of the main panel
		panel.add(welcomePanel, BorderLayout.NORTH);

		// create a tabbed pane for sign-in and registration options
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setBackground(ColorConstants.DARK_GRAY);
		tabbedPane.setForeground(ColorConstants.LIME_GREEN);

		// create the sign-in panel
		JPanel signInPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new java.awt.Insets(5, 5, 5, 5); // Add padding
		signInPanel.setBackground(ColorConstants.CHARCOAL);

		// Use TitledTextField for username
		usernameSignIn = new TitledTextField("Username", "Enter your username", 20);
		signInPanel.add(usernameSignIn, c);
		c.gridy++;

		// initialize password field with an empty string
		passwordSignIn = new TitledPasswordField("Password", "", 20);
		signInPanel.add(passwordSignIn, c);
		c.gridy++;

		signInButton = new JButton("Sign In");
		signInButton.setBackground(ColorConstants.SLATE_GRAY);
		signInButton.setEnabled(false); // Initially disabled
		signInPanel.add(signInButton, c);
		tabbedPane.addTab("Sign In", signInPanel);
		c.gridy++;
		
		resetPassword = new JButton("Reset password with pin");
		resetPassword.setBackground(ColorConstants.CRIMSON_RED);
		resetPassword.setForeground(ColorConstants.DEEP_BLUE);
		signInPanel.add(resetPassword, c);

		// create the registration panel
		JPanel registerPanel = new JPanel(new GridBagLayout());
		c.gridy = 0;
		c.gridx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new java.awt.Insets(5, 5, 5, 5);
		registerPanel.setBackground(ColorConstants.CHARCOAL);

		// Use TitledTextField for username
		usernameRegister = new TitledTextField("Username", "Enter your username", 20);
		registerPanel.add(usernameRegister, c);
		c.gridy++;

		passwordRegister = new TitledPasswordField("Password", "Enter your password", 20);
		registerPanel.add(passwordRegister, c);
		c.gridy++;

		passwordRegisterConfirm = new TitledPasswordField("Password", "Enter your password", 20);
		registerPanel.add(passwordRegisterConfirm, c);
		c.gridy++;

		// warning label for passwords
		passwordWarningLabel = new JLabel("");
		passwordWarningLabel.setForeground(ColorConstants.CRIMSON_RED);
		passwordWarningLabel.setPreferredSize(new Dimension(200, 20));
		passwordWarningLabel.setVisible(false);
		registerPanel.add(passwordWarningLabel, c);
		c.gridy++;

		// Use TitledTextField for PIN
		pinRegister = new TitledTextField("PIN(Last 4 of social)", "Enter your PIN", 20);
		registerPanel.add(pinRegister, c);
		c.gridy++;

		registerButton = new JButton("Register");
		registerButton.setEnabled(false); // Initially disabled
		registerButton.setBackground(ColorConstants.SLATE_GRAY);
		registerButton.setForeground(ColorConstants.CRIMSON_RED);
		registerPanel.add(registerButton, c);
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
		
		//key listener for enter key to submit
		KeyAdapter enterKeyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					System.out.println("Enter key pressed");
					int selectedIndex = tabbedPane.getSelectedIndex();
					if(selectedIndex == 0) {
						System.out.println("Sign In button clicked");
						signInButton.doClick();
					} else if (selectedIndex == 1) {
						System.out.println("Register button clicked");
						registerButton.doClick();
					}
				}
			}
		};

		// focus listener for pin
		pinRegister.getTextField().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				checkRegisterFields();
			}
		});

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
		passwordSignIn.getPasswordField().getDocument().addDocumentListener(documentListener);
		usernameRegister.getTextField().getDocument().addDocumentListener(documentListener);
		passwordRegister.getPasswordField().getDocument().addDocumentListener(documentListener);
		passwordRegisterConfirm.getPasswordField().getDocument().addDocumentListener(documentListener);
		pinRegister.getTextField().getDocument().addDocumentListener(documentListener);
		pinRegister.getTextField().addKeyListener(enterKeyListener);
		passwordSignIn.getPasswordField().addKeyListener(enterKeyListener);
		
		System.out.println("Login_Register_View created");
	}

	private void checkSignInFields() {
		boolean isSignInEnabled = !usernameSignIn.getText().trim().isEmpty()
				&& passwordSignIn.getPasswordField().getPassword().length > 0;
		signInButton.setEnabled(isSignInEnabled);
		
		if(signInButton.isEnabled()) {
			signInButton.setBackground(ColorConstants.DEEP_BLUE);
			signInButton.setForeground(ColorConstants.LIME_GREEN);
		}
	}

	private void checkRegisterFields() {
		boolean passwordsMatch = new String(passwordRegister.getPasswordField().getPassword())
				.equals(new String(passwordRegisterConfirm.getPasswordField().getPassword()));
		boolean isRegisterEnabled = !usernameRegister.getText().trim().isEmpty()
				&& passwordRegister.getPasswordField().getPassword().length > 0
				&& passwordRegisterConfirm.getPasswordField().getPassword().length > 0
				&& pinRegister.getText().matches("\\d{4}") && passwordsMatch; // Ensure passwords match before enabling
																				// the register button
		registerButton.setEnabled(isRegisterEnabled);
		if (registerButton.isEnabled()) {
			registerButton.setBackground(ColorConstants.DEEP_BLUE);
			registerButton.setForeground(ColorConstants.ORANGE);
		}

		// Update the password warning label text and visibility
		passwordWarningLabel.setText(passwordsMatch ? "" : "Passwords do not match!");
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
		return new String(passwordSignIn.getPasswordField().getPassword());
	}

	public String getUsernameRegister() {
		return usernameRegister.getText();
	}

	public String getPasswordRegister() {
		return new String(passwordRegister.getPasswordField().getPassword());
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
	
	public void addResetPAsswordListener(ActionListener listenForReset) {
		resetPassword.addActionListener(listenForReset);
	}
	public JButton getPasswordResetButton() {
		return resetPassword;
	}

}
