package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;

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

public class Login_Register_View extends JFrame {
	
	private JButton signInButton;
	private JButton registerButton;
	private JTextField usernameSignIn;
	private JPasswordField passwordSignIn;
	private JTextField usernameRegister;
	private JPasswordField passwordRegister;
	private JTextField pinRegister;
	

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
		signInPanel.add(new JLabel("Username:"), c);
		c.gridy++;
		
		//clear username text when it gains focus
		usernameSignIn = new JTextField("Enter your username", 20);
		usernameSignIn.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				usernameSignIn.setText("");
			}
		});
		signInPanel.add(usernameSignIn, c);
		c.gridy++;
		signInPanel.add(new JLabel("Password:"), c);
		c.gridy++;
		
		//clear password when gains focus
		passwordSignIn = new JPasswordField("Enter your password", 20);
		passwordSignIn.addFocusListener(new FocusAdapter(){
			@Override 
			public void focusGained(FocusEvent e) {
				passwordSignIn.setText("");
			}
		});
		signInPanel.add(passwordSignIn, c);
		c.gridy++;
		
		
		signInButton = new JButton("Sign In");
		signInPanel.add(signInButton, c);
		tabbedPane.addTab("Sign In", signInPanel);

		  // create the registration panel
        JPanel registerPanel = new JPanel(new GridBagLayout());
        c.gridy = 0;
        registerPanel.add(new JLabel("Username:"), c);
        c.gridy++;
        usernameRegister = new JTextField("Enter your username", 20);
        usernameRegister.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                usernameRegister.setText("");
            }
        });
        registerPanel.add(usernameRegister, c);
        c.gridy++;
        registerPanel.add(new JLabel("Password:"), c);
        c.gridy++;
        passwordRegister = new JPasswordField("Enter your password", 20);
        passwordRegister.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordRegister.setText("");
            }
        });
        registerPanel.add(passwordRegister, c);
        c.gridy++;
        registerPanel.add(new JLabel("PIN (last 4 digits of SSN):"), c);
        c.gridy++;
        
        pinRegister = new JTextField("Enter your PIN", 20);
        //verify is 4 digits
        pinRegister.setInputVerifier(new InputVerifier() {
        	@Override
        	public boolean verify(JComponent input) {
        		String text = ((JTextField) input).getText();
        		return text.matches("\\d(4)");
        	}
        });
        pinRegister.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                pinRegister.setText("");
            }
        });
        registerPanel.add(pinRegister, c);
        c.gridy++;
        registerButton = new JButton("Register");
       
        registerPanel.add(registerButton, c);
        c.gridy++;
        registerPanel.add(new JLabel(" "), c); // Padding at the bottom
        tabbedPane.addTab("Register", registerPanel);

		// add the tabbed pane to the panel
		panel.add(tabbedPane, BorderLayout.CENTER);

		// pack the frame, which will take into account the preferred size of its
		// contents
		pack();

		// center the frame on the screen
		setLocationRelativeTo(null);
	}
	//getters to provide buttons to view so it can to controller
	
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