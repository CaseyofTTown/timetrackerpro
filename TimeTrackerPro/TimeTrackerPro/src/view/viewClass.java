package view;

import javax.swing.*;

import model.CertificationLevelenum;

import java.awt.*;
import java.util.Date;

public class viewClass extends JFrame {

	private Login_Register_View loginRegisterView;
	private NewEmployeeInfoView newEmployeeInfoView;
	private HomeView homeView;
	private CardLayout cardLayout;
	private JPanel mainPanel;

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

		// create a panel with CardLayout
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		mainPanel.setBackground(new Color(50, 50, 50));

		// add the panel to the frame
		add(mainPanel);

		// create an instance of Login_Register_View
		loginRegisterView = new Login_Register_View();
		mainPanel.add(loginRegisterView.getContentPane(), "LoginRegisterView");

		// create an instance of NewEmployeeInfoView
		newEmployeeInfoView = new NewEmployeeInfoView();
		mainPanel.add(newEmployeeInfoView.getContentPane(), "NewEmployeeInfoView");

		// pack frame to fit contents @ preferred size
		pack();

		// Center frame on the screen
		setLocationRelativeTo(null);
	}

	public void showLoginRegisterView() {
		cardLayout.show(mainPanel, "LoginRegisterView");
	}

	public void hideLoginRegisterView() {
		loginRegisterView.setVisible(false);
	}

	public void showNewEmployeeInfoView() {
		cardLayout.show(mainPanel, "NewEmployeeInfoView");
	}

	public void hideNewEmployeeInfoView() {
		newEmployeeInfoView.setVisible(false);
	}

	// main view
	public void showHomeView(String employeeName) {
		if (homeView == null) {
			homeView = new HomeView(employeeName);
			mainPanel.add(homeView.getContentPane(), "HomeView");
		}
		cardLayout.show(mainPanel, "HomeView");
	}

	public void hideHomeView() {
		if (homeView != null) {
			homeView.setVisible(false);
		}
	}

	// getters for login_register_view so controller can access the buttons
	public JButton getSignInButton() {
		return loginRegisterView.getSignInButton();
	}

	public JButton getRegisterButton() {
		return loginRegisterView.getRegisterButton();
	}

	public String getUsernameSignIn() {
		return loginRegisterView.getUsernameSignIn();
	}

	public String getUsernameRegister() {
		return loginRegisterView.getUsernameRegister();
	}

	public String getPasswordSignIn() {
		return loginRegisterView.getPasswordSignIn();
	}

	public String getPasswordRegister() {
		return loginRegisterView.getPasswordRegister();
	}

	public int getPinRegister() {
		return loginRegisterView.getPinRegister();
	}

	// getters for NewEmployeeView
	public String getEmployeeName() {
		return newEmployeeInfoView.getName();
	}

	public String getIsEmsCertified() {
		return newEmployeeInfoView.getEmsCertified();
	}

	public CertificationLevelenum getCertificationLevel() {
		return (CertificationLevelenum) newEmployeeInfoView.getCertificationLevel();
	}

	public String getEmsCertificationNumber() {
		return newEmployeeInfoView.getCertificationNumber();
	}

	public Date getExpirationDate() {
		return newEmployeeInfoView.getExpirationDate();
	}

	public JButton getSubmitEmployeeInfoButton() {
		return newEmployeeInfoView.getSubmitNewEmployeeButton();
	}
}
