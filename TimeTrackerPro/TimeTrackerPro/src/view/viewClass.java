package view;

import javax.swing.*;
import java.util.List;

import model.CertificationLevelenum;
import model.ColorConstants;
import model.TimeSheet;

import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Time;
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

		/*
		 * moved to individual functions due to bugs, kept for reference // create an
		 * instance of Login_Register_View loginRegisterView = new
		 * Login_Register_View(); mainPanel.add(loginRegisterView.getContentPane(),
		 * "LoginRegisterView");
		 * 
		 * // create an instance of NewEmployeeInfoView newEmployeeInfoView = new
		 * NewEmployeeInfoView(); mainPanel.add(newEmployeeInfoView.getContentPane(),
		 * "NewEmployeeInfoView");
		 * 
		 * timeSheetPanel = new TimeSheetPanel(); mainPanel.add(timeSheetPanel,
		 * "TimeSheetsPanel");
		 */

		// pack frame to fit contents @ preferred size
		// pack();

		// Center frame on the screen
		setLocationRelativeTo(null);
		System.out.println("viewClass instance created");
		revalidate();
		repaint();
	}

	public void showLoginRegisterView() {
		if (loginRegisterView == null) {
			loginRegisterView = new Login_Register_View();
			mainPanel.add(loginRegisterView.getContentPane(), "LoginRegisterView");
		}
		cardLayout.show(mainPanel, "LoginRegisterView");
		updateWindowSize();
	}

	public void hideLoginRegisterView() {
		if (loginRegisterView != null) {
			loginRegisterView.setVisible(false);
			mainPanel.remove(loginRegisterView.getContentPane());
			loginRegisterView = null;
			System.out.println("LoginRegisterView removed");
			updateWindowSize();

		}
	}

	public void showNewEmployeeInfoView() {
		if (newEmployeeInfoView == null) {
			newEmployeeInfoView = new NewEmployeeInfoView();
			mainPanel.add(newEmployeeInfoView.getContentPane(), "NewEmployeeInfoView");

		}
		cardLayout.show(mainPanel, "NewEmployeeInfoView");
		updateWindowSize();

	}

	public void hideNewEmployeeInfoView() {
		if (newEmployeeInfoView != null) {
			newEmployeeInfoView.setVisible(false);
			mainPanel.remove(newEmployeeInfoView.getContentPane());
			newEmployeeInfoView = null;
			System.out.println("NewEmployeeInfoView removed");
			updateWindowSize();

		}
	}

	public void showHomeView(String employeeName) {
		if (homeView != null) {
			System.out.println("removing home view");
			mainPanel.remove(homeView);
		}
		homeView = new HomeView(employeeName);

		mainPanel.add(homeView, "HomeView");
		homeView.setVisible(true);

		cardLayout.show(mainPanel, "HomeView");
		System.out.println("HomeView shown with employee name: " + employeeName);
		updateWindowSize();
		revalidate();
		repaint();

	}

	public void hideHomeView() {
		if (homeView != null) {
			homeView.setVisible(false);
			updateWindowSize();

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

	// getters for buttons on HomePage, timeSheetPanel
	public TimeSheetPanel getTimeSheetPanel() {
		return homeView.getTimeSheetPanel();
	}

	public void setStartDate(Date date) {
		homeView.setStartDate(date);
	}

	public void setEndDate(Date date) {
		homeView.setEndDate(date);
	}

	public void showModifyTimeSheetView(TimeSheet timeSheet) {
		homeView.showAddNewTimeSheetUI();
		homeView.setEmployeeNameOnTS(timeSheet.getEmployeeName());
		homeView.setShiftStartDateOnModTs(timeSheet.getShiftStartDate());
		homeView.setShiftEndDateOnModTs(timeSheet.getShiftEndDate());
		homeView.setShiftStartTimeOnModTs(timeSheet.getShiftStartTime());
		homeView.setShiftEndTimeOnModTs(timeSheet.getShiftEndTime());
		homeView.setOverTimeCommentOnModTs(timeSheet.getOvertimeComment());
		revalidate();
		repaint();
		

	}

	public void showNewTimeSheetUI() {
		homeView.showAddNewTimeSheetUI();
	}

	// used to set the employee name list on TimeSheetPanel
	public void setEmployeeNameList(List<String> employeeNames) {
		System.out.println("passing " + employeeNames.size() + "to homeView");
		homeView.setEmployeeNameList(employeeNames);
	}

	public void updateTimeSheetDisplay(List<TimeSheet> timeSheets) {
		TimeSheetDisplay timeSheetDisplay = getTimeSheetPanel().getTimeSheetDisplay();
		timeSheetDisplay.clearAllEntries();
		System.out.println(timeSheets.size() + " time sheets passed to timeSheetDisplayPannel");
		for (TimeSheet timeSheet : timeSheets) {
			timeSheetDisplay.addTimeSheetEntry(timeSheet);
		}
		revalidate();
		repaint();
		updateWindowSize();

	}

	private void updateWindowSize() {
		pack();
		setExtendedState(JFrame.MAXIMIZED_BOTH); // for full screen
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	// getters so controller can create a new time sheet from
	// homeView-TimesheetPanel-timesheetentryPanel
	public String getSelectedEmployeeOnTimeSheet() {
		return homeView.getSelectedEmployeeName();
	}
	//method so controller can modify an existing time sheet
	public int getSelectedTimeSheetId() {
		return homeView.getSelectedTimeSheetId();
	}

	public Date getShiftStartDateOnTs() {
		return homeView.getShiftStartDate();
	}

	public Date getShiftEndDateOnTs() {
		return homeView.getShiftEndDate();
	}

	public Time getShiftStartTimeOnTs() {
		return homeView.getShiftStartTime();
	}

	public Time getShiftEndTimeOnTs() {
		return homeView.getShiftEndTime();
	}

	public String getOvertimeCommentsOnTs() {
		return homeView.getOvertimeComment();
	}

	public JButton getSubmitTimeSheetButton() {
		return homeView.getTimeSheetSubmitButton();
	}

	public JButton getCancelTimeSheetButton() {
		return homeView.getCancelTimeSheetSubmissionButton();
	}

	// getters/setters to update timeSheetDisplay
	public void addAllTimeSheetsToDisplay(List<TimeSheet> timeSheets) {
		homeView.addAllTimeSheetsToDisplay(timeSheets);
	}
}
