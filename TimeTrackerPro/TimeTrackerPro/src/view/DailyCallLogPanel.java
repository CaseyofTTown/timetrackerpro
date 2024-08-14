package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.BorderFactory;

import org.jdatepicker.impl.JDatePickerImpl;

import model.CallLogCardSelectionListener;
import model.ColorConstants;
import model.DailyCallLog;
import controller.TTController;
import model.CallLogCardSelectionListener;

public class DailyCallLogPanel extends JPanel implements CallLogCardSelectionListener {
	private JButton createNewCallLogBttn;
	private JButton modifyDailyCallLogBttn;
	private JButton deleteCallLogButton;
	private JButton updateCallLogDisplayDateRangesBttn;
	private CallLogDisplay callLogDisplay;
	private JDatePickerImpl startDatePicker;
	private JDatePickerImpl endDatePicker;
	private CallLogEntryPanel callLogEntryPanel;
	private List<String> crewMembers;
	private List<DailyCallLog> dailyLogList;
	private JSplitPane splitPane;
	private TTController controller;
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private CallLogCard selectedCard;
	private int idOfSelectedCallLogCard;

	public DailyCallLogPanel(TTController controller) {
		this.controller = controller;
		if (crewMembers == null) {
			System.out.println("employee names list null on dl panel");
			crewMembers = new ArrayList<>();
		} else {
			System.out.println("DailyLogPanel created with " + crewMembers.size() + " crew members passed");
		}

		setLayout(new BorderLayout());
		setBackground(ColorConstants.CHARCOAL);

		// Date Range selection
		JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		datePanel.setBackground(ColorConstants.DARK_GRAY);
		datePanel.setForeground(ColorConstants.GOLD);

		JLabel dateRangeLabel = new JLabel("Select Date Range: ");
		dateRangeLabel.setForeground(ColorConstants.GOLD);
		dateRangeLabel.setFont(new Font("Arial", Font.BOLD, 14));
		datePanel.add(dateRangeLabel);

		DatePicker startDatePickerComponent = new DatePicker();
		startDatePicker = startDatePickerComponent.getDatePicker();
		DatePicker endDatePickerComponent = new DatePicker();
		endDatePicker = endDatePickerComponent.getDatePicker();

		// Update button
		updateCallLogDisplayDateRangesBttn = new JButton("Update Date Range");
		updateCallLogDisplayDateRangesBttn.setBackground(ColorConstants.SLATE_GRAY);
		updateCallLogDisplayDateRangesBttn.setForeground(ColorConstants.GOLD);
		updateCallLogDisplayDateRangesBttn.setFont(new Font("Arial", Font.BOLD, 14));

		datePanel.add(startDatePicker);
		datePanel.add(endDatePicker);
		datePanel.add(updateCallLogDisplayDateRangesBttn);

		add(datePanel, BorderLayout.NORTH);

		// CallLogDisplay Component
		callLogDisplay = new CallLogDisplay(controller);
		updateCallLog();

		// CallLogEntryPanel (initially hidden)
		callLogEntryPanel = new CallLogEntryPanel(crewMembers);
		callLogEntryPanel.setVisible(false);

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(callLogDisplay), callLogEntryPanel);
		splitPane.setResizeWeight(0.8); // adjust size weight
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(300);
		add(splitPane, BorderLayout.CENTER);

		// Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBackground(ColorConstants.DARK_GRAY);

		createNewCallLogBttn = new JButton("Add New Call Log");
		modifyDailyCallLogBttn = new JButton("Modify Call Log");
		deleteCallLogButton = new JButton("Delete Call Log");

		styleButton(createNewCallLogBttn);
		styleButton(modifyDailyCallLogBttn);
		styleButton(deleteCallLogButton);

		buttonPanel.add(createNewCallLogBttn);
		buttonPanel.add(modifyDailyCallLogBttn);
		buttonPanel.add(deleteCallLogButton);

		add(buttonPanel, BorderLayout.SOUTH);

		// Disable buttons if call log not selected
		modifyDailyCallLogBttn.setEnabled(false);
		deleteCallLogButton.setEnabled(false);

		// listeners for modify/delete call log buttons
		modifyDailyCallLogBttn.addActionListener(e -> loadCreateNewCallLogIdWithExistingLog());

		// add action listener to createNewCallLogBttn
		createNewCallLogBttn.addActionListener(e -> showAddNewCallLogPanel());

		// delete call logs action listener
		deleteCallLogButton.addActionListener(e -> deleteCallLogWithSelectedId());
		// Add action listener to cancel button
		callLogEntryPanel.getCancelButton().addActionListener(e -> hideAddNewCallLogPanel());
		callLogEntryPanel.getSubmitButton().addActionListener(e -> handleCreateNewCallLog());

		// listener to update call log date range
		startDatePicker.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateStartSqlDateInController();
				updateCallLog();
			}
		});
		endDatePicker.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateEndDateInController();
				updateCallLog();
			}
		});
		//leaving for a way to force refresh incase something is wrong 
		updateCallLogDisplayDateRangesBttn.addActionListener(e -> updateCallLog());

		System.out.println("DailyCallLogPanel created");
		revalidate();
		repaint();
	}
	

	@Override
	public void onCardSelected(CallLogCard selectedCard) {
	    System.out.println("onCardSelected called with card: " + selectedCard);
	    if (selectedCard != null) {
	        idOfSelectedCallLogCard = selectedCard.getCallLogId(); // store selected id
	        this.selectedCard = selectedCard;
	    } else {
	        idOfSelectedCallLogCard = -1; // reset id
	        this.selectedCard = null;
	    }
	    boolean isSelected = selectedCard != null && selectedCard.isSelected();
	    modifyDailyCallLogBttn.setEnabled(isSelected);
	    deleteCallLogButton.setEnabled(isSelected);
	}



	private void loadCreateNewCallLogIdWithExistingLog() {
		// TODO Auto-generated method stub

	}

	private void deleteCallLogWithSelectedId() {
		controller.deleteCallLogWithId(idOfSelectedCallLogCard);
		updateCallLog();
	}

	//create call log cards, set listener, pass cards to display for it to show them. no longer creating them in display
	public void updateCallLog() {
	    try {
	        callLogDisplay.clearAllEntries();
	        dailyLogList = controller.getCallLogsFromDateToDate();
	        for (DailyCallLog callLog : dailyLogList) {
	            CallLogCard card = new CallLogCard(callLog, controller);
	            card.setSelectionListener(this); // Set the listener
	            callLogDisplay.addCallLogCard(card);
	        }
	        System.out.println(dailyLogList.size() + " call logs passed to callLogDisplay, addAllCallLogCards called -updateCallLogs()");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    revalidate();
	    repaint();
	}


	private void updateStartSqlDateInController() {
		if (controller != null) {
			Date startDate = (Date) startDatePicker.getModel().getValue();

			// convert to sql date
			java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
			controller.setSqlStartDate(sqlStartDate);
		} else {
			System.out.println("controller was null when updateStartSqlDateInController called it");
		}
	}

	private void updateEndDateInController() {
		if (controller != null) {
			Date endDate = (Date) endDatePicker.getModel().getValue();
			// convert to sql
			java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
			controller.setSqlEndDate(sqlEndDate);
			updateCallLog();
		} else {
			System.out.println("controller was null when updateEndSqlDateInController called it");

		}
	}

	private void handleCreateNewCallLog() {
		Date shiftStartDate = callLogEntryPanel.getStartDate();
		Date shiftEndDate = callLogEntryPanel.getEndDate();
		String truckUnitNumber = callLogEntryPanel.getTruckUnitNumber();

		DailyCallLog log = new DailyCallLog(shiftStartDate, shiftEndDate, truckUnitNumber);

		// add employees to objects list
		if (callLogEntryPanel.getCrewMemberList() != null) {
			List<String> emplList = callLogEntryPanel.getCrewMemberList();
			for (String Employees : emplList) {
				log.addCrewMember(Employees);
			}
		}
		controller.createNewCallLog(log);
		// reset and hide entry panel
		hideAddNewCallLogPanel();
		updateCallLog();
	}

	public void hideAddNewCallLogPanel() {
		callLogEntryPanel.setVisible(false);
		resetCallLogEntryPanel();
	}

	private void resetCallLogEntryPanel() {
		callLogEntryPanel.getTruckUnitNumberField().setText("");
		callLogEntryPanel.getStartDatePicker().getModel().setValue(null);
		callLogEntryPanel.getEndDatePicker().getModel().setValue(null);
		callLogEntryPanel.resetFields();
	}

	private void styleButton(JButton button) {
		button.setBackground(ColorConstants.DEEP_BLUE);
		button.setForeground(ColorConstants.LIME_GREEN);
		button.setFont(new Font("Arial", Font.BOLD, 12));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
	}

	// Getters for buttons and display
	public JButton getCreateNewCallLogBttn() {
		return createNewCallLogBttn;
	}

	public JButton getModifyDailyCallLogBttn() {
		return modifyDailyCallLogBttn;
	}

	public JButton getDeleteCallLogButton() {
		return deleteCallLogButton;
	}

	public CallLogDisplay getCallLogDisplay() {
		return callLogDisplay;
	}

	// Getters for date fields for call log display range
	public Date getStartDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(startDatePicker.getModel().getYear(), startDatePicker.getModel().getMonth(),
				startDatePicker.getModel().getDay());
		return calendar.getTime();
	}

	public Date getEndDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(endDatePicker.getModel().getYear(), endDatePicker.getModel().getMonth(),
				endDatePicker.getModel().getDay());
		return calendar.getTime();
	}

	// Getter for update date range button
	public JButton getUpdateCallLogDisplayDateRangesBttn() {
		return updateCallLogDisplayDateRangesBttn;
	}

	public JDatePickerImpl getStartDatePicker() {
		return startDatePicker;
	}

	public JDatePickerImpl getEndDatePicker() {
		return endDatePicker;
	}

	public int getSelectedCallLogId() {
		if (selectedCard != null) {
			return selectedCard.getCallLogId();
		} else {
			System.out.println("unable to get call log id, was null");
			return -1;
		}
	}

	public void setStartDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		startDatePicker.getModel().setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		startDatePicker.getModel().setSelected(true);
	}

	public void setEndDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		endDatePicker.getModel().setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		endDatePicker.getModel().setSelected(true);
	}

	public void setCrewMemberList(List<String> crewMembers) {
		this.crewMembers = crewMembers;
		if (callLogEntryPanel != null) {
			this.callLogEntryPanel.setCrewMemberList(crewMembers);
		} else {
			System.out.println("callLogEntryPanel was null when setCrewMember list in DailyCallLogPanel was called");
		}
	}

	public void showAddNewCallLogPanel() {
		splitPane.setDividerLocation(0.5);
		callLogEntryPanel.setVisible(true);
	}

	// Getters for CallLogEntryPanel to allow view.controller to create a new Call
	// Log
	public String getTruckUnitNumber() {
		return callLogEntryPanel.getTruckUnitNumber();
	}

	public Date getLogStartDate() {
		return callLogEntryPanel.getStartDate();
	}

	public Date getLogEndDate() {
		return callLogEntryPanel.getEndDate();
	}

	public List<String> getCrewMembers() {
		return callLogEntryPanel.getCrewMembers();
	}

	public JButton getSubmitCallLogButton() {
		return callLogEntryPanel.getSubmitButton();
	}

	public JButton getCancelCallLogButton() {
		return callLogEntryPanel.getCancelButton();
	}

	public JPanel getCallLogEntryPanel() {
		return callLogEntryPanel;
	}


	// Setters to modify a call log
	public void setTruckUnitNumberOnModCallLog(String truckUnitNumber) {
		callLogEntryPanel.setTruckUnitNumber(truckUnitNumber);
	}

	public void setLogStartDateOnModCallLog(Date startDate) {
		callLogEntryPanel.setStartDate(startDate);
	}

}
