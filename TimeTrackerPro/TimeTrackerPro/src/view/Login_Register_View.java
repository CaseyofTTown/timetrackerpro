package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Login_Register_View extends JFrame {

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

		// create a label for the welcome message
		JLabel welcomeLabel = new JLabel("Welcome to Time Tracker Pro! \n" + "Please sign in to continue.",
				SwingConstants.CENTER);
		welcomeLabel.setForeground(Color.GREEN);
		panel.add(welcomeLabel, BorderLayout.NORTH);

		// create a tabbed pane for sign-in and registration options
		JTabbedPane tabbedPane = new JTabbedPane();

		// create the sign-in panel
		JPanel signInPanel = new JPanel(new GridLayout(2, 2));
		signInPanel.add(new JLabel("Username:"));
		signInPanel.add(new JTextField("Enter your username"));
		signInPanel.add(new JLabel("Password:"));
		signInPanel.add(new JPasswordField("Enter your password"));
		JButton signInButton = new JButton("Sign In");
		signInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: Add sign-in logic here
			}
		});
		signInPanel.add(signInButton);
		tabbedPane.addTab("Sign In", signInPanel);

		// create the registration panel
		JPanel registerPanel = new JPanel(new GridLayout(3, 2));
		registerPanel.add(new JLabel("Username:"));
		registerPanel.add(new JTextField("Enter your username"));
		registerPanel.add(new JLabel("Password:"));
		registerPanel.add(new JPasswordField("Enter your password"));
		registerPanel.add(new JLabel("PIN (last 4 digits of SSN):"));
		registerPanel.add(new JTextField("Enter your PIN"));
		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: Add registration logic here
			}
		});
		registerPanel.add(registerButton);
		tabbedPane.addTab("Register", registerPanel);

		// add the tabbed pane to the panel
		panel.add(tabbedPane, BorderLayout.CENTER);

		// pack the frame, which will take into account the preferred size of its
		// contents
		pack();

		// center the frame on the screen
		setLocationRelativeTo(null);
	}
}