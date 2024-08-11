package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
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

import model.ColorConstants;
import model.DailyCallLog;
import controller.TTController;

public class DailyCallLogPanel extends JPanel {
    private JButton createNewCallLogBttn;
    private JButton modifyDailyCallLogBttn;
    private JButton deleteCallLogButton;
    private JButton updateCallLogDisplayDateRangesBttn;
    private CallLogDisplay callLogDisplay;
    private JDatePickerImpl startDatePicker;
    private JDatePickerImpl endDatePicker;
    private CallLogEntryPanel callLogEntryPanel;
    private List<String> crewMembers;
    private JSplitPane splitPane;
    private TTController controller;

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

        callLogDisplay.getSelectionModel().addListSelectionListener(e -> {
            boolean isSelected = callLogDisplay.getSelectedCallLogId() != -1;
            modifyDailyCallLogBttn.setEnabled(isSelected);
            deleteCallLogButton.setEnabled(isSelected);
        });
        
        //add action listener to createNewCallLogBttn
        createNewCallLogBttn.addActionListener(e ->showAddNewCallLogPanel());
        
     // Add action listener to cancel button
        callLogEntryPanel.getCancelButton().addActionListener(e -> hideAddNewCallLogPanel());
        callLogEntryPanel.getSubmitButton().addActionListener(e -> handleCreateNewCallLog());
        
        System.out.println("DailyCallLogPanel created");
        revalidate();
        repaint();
    }

    private void handleCreateNewCallLog() {
    	Date shiftStartDate = callLogEntryPanel.getStartDate();
    	Date shiftEndDate = callLogEntryPanel.getEndDate();
    	String truckUnitNumber = callLogEntryPanel.getTruckUnitNumber();
    	
		DailyCallLog log = new DailyCallLog(shiftStartDate, shiftEndDate, truckUnitNumber);
		
		//add employees to objects list 
		if(callLogEntryPanel.getCrewMemberList() != null) {
			List<String> emplList = callLogEntryPanel.getCrewMemberList();
			for(String Employees : emplList) {
				log.addCrewMember(Employees);
			}
		}
		controller.createNewCallLog(log);
    	//reset and hide entry panel
    	hideAddNewCallLogPanel();
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
        return callLogDisplay.getSelectedCallLogId();
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
        if(callLogEntryPanel != null) {
        this.callLogEntryPanel.setCrewMemberList(crewMembers);
        } else {
        	System.out.println("callLogEntryPanel was null when setCrewMember list in DailyCallLogPanel was called");
        }
    }

    public void showAddNewCallLogPanel() {
        splitPane.setDividerLocation(0.5);
        callLogEntryPanel.setVisible(true);
    }

    // Getters for CallLogEntryPanel to allow view.controller to create a new Call Log
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
    // Getters/setters to update callLogDisplay
    public void addAllCallLogsToDisplay(List<DailyCallLog> callLogs) {
        callLogDisplay.addAllCallLogCards(callLogs);
    }


    // Setters to modify a call log
    public void setTruckUnitNumberOnModCallLog(String truckUnitNumber) {
        callLogEntryPanel.setTruckUnitNumber(truckUnitNumber);
    }

    public void setLogStartDateOnModCallLog(Date startDate) {
        callLogEntryPanel.setStartDate(startDate);
    }

}
