package view;

import javax.swing.*;
import java.util.List;

import model.CertificationLevelenum;
import model.ColorConstants;
import model.TimeSheet;

import java.awt.*;
import java.util.Date;

public class viewClass extends JFrame {

	private Login_Register_View loginRegisterView;
	private NewEmployeeInfoView newEmployeeInfoView;
	private TimeSheetPanel timeSheetPanel;
	private HomeView homeView;
	private CardLayout cardLayout;
	private JPanel mainPanel;

	public viewClass() {
		// set the window title
		setTitle("Time Tracker Pro");

		// set default close operation
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set the size of the window
		//setSize(800, 600);

		// set a dark theme using UiManager
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			UIManager.put("control", ColorConstants.CHARCOAL);
			UIManager.put("nimbusBase", ColorConstants.DARK_GRAY);
			UIManager.put("nimbusAlertYellow", ColorConstants.GOLD);
			UIManager.put("nimbusDisabledText", ColorConstants.SLATE_GRAY);
			UIManager.put("nimbusFocus", ColorConstants.DEEP_BLUE);
			UIManager.put("nimbusLightBackground", ColorConstants.DARK_GRAY);
			UIManager.put("nimbusSelectionBackground", ColorConstants.LIGHT_GRAY);
			UIManager.put("text", ColorConstants.WHITE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// create a panel with CardLayout
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		mainPanel.setBackground(ColorConstants.DARK_GRAY);

		// add the panel to the frame
		add(mainPanel);

		// create an instance of Login_Register_View
		loginRegisterView = new Login_Register_View();
		mainPanel.add(loginRegisterView.getContentPane(), "LoginRegisterView");

		// create an instance of NewEmployeeInfoView
		newEmployeeInfoView = new NewEmployeeInfoView();
		mainPanel.add(newEmployeeInfoView.getContentPane(), "NewEmployeeInfoView");
		
		timeSheetPanel = new TimeSheetPanel();
		mainPanel.add(timeSheetPanel, "TimeSheetsPanel");
		

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

	public void showHomeView(String employeeName) {
	    if (homeView != null) {
	        mainPanel.remove(homeView.getContentPane());
	    }
	    homeView = new HomeView(employeeName);
	    mainPanel.add(homeView.getContentPane(), "HomeView");
	    homeView.setVisible(true);
	    //homeView.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    cardLayout.show(mainPanel, "HomeView");
	    System.out.println("HomeView shown with employee name: " + employeeName);
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

	//getters for buttons on HomePage, timeSheetPanel
	public TimeSheetPanel getTimeSheetPanel() {
		return timeSheetPanel;
	}
	public void setStartDate(Date date) {
		homeView.setStartDate(date);
		}

	public void setEndDate(Date date) {
		homeView.setEndDate(date);
	}

	public void showModifyTimeSheetView(TimeSheet timeSheet) {
		// TODO Auto-generated method stub
		
	}

	public void updateTimeSheetDisplay(List<TimeSheet> timeSheets) {
		TimeSheetDisplay timeSheetDisplay = getTimeSheetPanel().getTimeSheetDisplay();
		timeSheetDisplay.clearAllEntries();
		for(TimeSheet timeSheet : timeSheets) {
			timeSheetDisplay.addTimeSheetEntry(timeSheet);
		}
	}
}
