package view;

import javax.swing.*;
import java.awt.*;

public class viewClass extends JFrame {

	private Login_Register_View loginRegisterView;

	public viewClass() {
		// set the window title
		setTitle("Time Tracker Pro");

		// set default close operation
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set the size of the window
		setSize(800, 600);

		// set a dark theme using UiManager
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			UIManager.put("control", new Color(40, 40, 40));
			UIManager.put("nimbusBase", new Color(0, 0, 0));
			UIManager.put("nimbusAlertYellow", new Color(255, 187, 0));
			UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
			UIManager.put("nimbusFocus", new Color(115, 164, 209));
			UIManager.put("nimbusLightBackground", new Color(50, 50, 50));
			UIManager.put("nimbusSelectionBackground", new Color(100, 100, 100));
			UIManager.put("text", new Color(255, 255, 255));

		} catch (Exception e) {
			e.printStackTrace();
		}

		// create a panel with a BoxLayout
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(new Color(50, 50, 50));

		// add the panel to the frame
		add(panel);

		// The components will be added here in the future

		// create an instance of Login_Register_View to the panel
		loginRegisterView = new Login_Register_View();
		panel.add(loginRegisterView.getContentPane());

		// pack frame to fit contents @ preferred size
		pack();

		// Center frame on the escreen
		setLocationRelativeTo(null);

		// method to show login/register view

	}

	public void showLoginRegisterView() {
		loginRegisterView.setVisible(true);
	}
	
	public void hideLoginRegisterview() {
		loginRegisterView.setVisible(false);
	}
	
	//getters so controller can access the buttons
	public JButton getSignInButton() {
		return loginRegisterView.getSignInButton();
	}
	public JButton getRegisterButton() {
		return loginRegisterView.getRegisterButton();
	}

}