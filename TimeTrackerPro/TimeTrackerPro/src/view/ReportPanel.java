package view;

import javax.swing.*;

import controller.TTController;
import model.AmbulanceCall;
import model.ColorConstants;
import model.DailyCallLog;
import model.Employee;
import model.KeyBindingUtil;
import model.TimeSheet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportPanel extends JPanel {
	private JSplitPane splitPane;
	private PrintPreviewPanel printPreviewPanel;
	private JPanel controlPanel;
	private TTController controller;

	public ReportPanel(TTController controller) {
		this.controller = controller;
		setLayout(new BorderLayout());

		// Create the split pane
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(800); // Adjust as needed

		// Create the print preview panel
		printPreviewPanel = new PrintPreviewPanel();

		// Create the control panel
		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.setBackground(ColorConstants.DARK_GRAY);

		// Add buttons to the control panel
		JButton addTimeSheetButton = new JButton("Generate Payroll report");
		addTimeSheetButton.setBackground(ColorConstants.DEEP_BLUE);
		addTimeSheetButton.setForeground(ColorConstants.GOLD);

		JButton addCertificationListButton = new JButton("Generate Certification Report");
		addCertificationListButton.setBackground(ColorConstants.DEEP_BLUE);
		addCertificationListButton.setForeground(ColorConstants.GOLD);

		JButton addCallLogButton = new JButton("Generate Call Log Report");
		addCallLogButton.setBackground(ColorConstants.DEEP_BLUE);
		addCallLogButton.setForeground(ColorConstants.GOLD);
		
		JButton addTotalCountSummaryButton = new JButton("Generate Call Count report");
		addTotalCountSummaryButton.setForeground(ColorConstants.DEEP_BLUE);
		addTotalCountSummaryButton.setBackground(ColorConstants.GOLD);
		
	

		controlPanel.add(addTimeSheetButton);
		controlPanel.add(Box.createVerticalStrut(10));
		controlPanel.add(addCertificationListButton);
		controlPanel.add(Box.createVerticalStrut(10));
		controlPanel.add(addCallLogButton);
		controlPanel.add(Box.createVerticalStrut(10));
		controlPanel.add(addTotalCountSummaryButton);
		

		// Add panels to the split pane
		splitPane.setLeftComponent(printPreviewPanel);
		splitPane.setRightComponent(controlPanel);

		// Add the split pane to the main panel
		add(splitPane, BorderLayout.CENTER);

		addTimeSheetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showDateRangeDialog();
			}
		});
		
		 addCallLogButton.addActionListener(new ActionListener() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		            showCallLogDateRangeDialog();
		        }
		    });
		 addCertificationListButton.addActionListener(new ActionListener() {
			 @Override
			 public void actionPerformed(ActionEvent e) {
				 generateEmployeeReport();
			 }
		 });
		
		 addTotalCountSummaryButton.addActionListener(new ActionListener() {
			 @Override
			 public void actionPerformed(ActionEvent e) {
				 showTotalCountSummaryDateRangeDialog();
			 }
		 });
		 
	}
	private void showCallLogDateRangeDialog() {
        // Create date pickers
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();

        // Create panel for the dialog
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Start Date:"));
        panel.add(startDatePicker.getDatePicker());
        panel.add(new JLabel("End Date:"));
        panel.add(endDatePicker.getDatePicker());

        // Create buttons
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");

        // Add buttons to the panel
        panel.add(submitButton);
        panel.add(cancelButton);

        // Create the dialog
        JDialog dialog = new JDialog((Frame) null, "Choose Date Range", true);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        // Add action listeners for buttons
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.Date startDate = (java.util.Date) startDatePicker.getDatePicker().getModel().getValue();
                java.util.Date endDate = (java.util.Date) endDatePicker.getDatePicker().getModel().getValue();
               
                //revert here back to generateCallLogsReport if it breaks
                generateCallLogsReport(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
                dialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        // Add key bindings
        KeyBindingUtil.addSubmitAndCancelBindings(panel, submitButton, cancelButton);

        dialog.setVisible(true);
    }
	
	private void showTotalCountSummaryDateRangeDialog() {
	    // Create date pickers
	    DatePicker startDatePicker = new DatePicker();
	    DatePicker endDatePicker = new DatePicker();

	    // Create panel for the dialog
	    JPanel panel = new JPanel(new GridLayout(3, 2));
	    panel.add(new JLabel("Start Date:"));
	    panel.add(startDatePicker.getDatePicker());
	    panel.add(new JLabel("End Date:"));
	    panel.add(endDatePicker.getDatePicker());

	    // Create buttons
	    JButton submitButton = new JButton("Submit");
	    JButton cancelButton = new JButton("Cancel");

	    // Add buttons to the panel
	    panel.add(submitButton);
	    panel.add(cancelButton);

	    // Create the dialog
	    JDialog dialog = new JDialog((Frame) null, "Choose Date Range", true);
	    dialog.getContentPane().add(panel);
	    dialog.pack();
	    dialog.setLocationRelativeTo(null);

	    // Add action listeners for buttons
	    submitButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            java.util.Date startDate = (java.util.Date) startDatePicker.getDatePicker().getModel().getValue();
	            java.util.Date endDate = (java.util.Date) endDatePicker.getDatePicker().getModel().getValue();

	            // Trigger the function to generate the total count summary report
	            generateTotalCountSummaryReport(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
	            dialog.dispose();
	        }
	    });

	    cancelButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            dialog.dispose();
	        }
	    });

	    // Add key bindings
	    KeyBindingUtil.addSubmitAndCancelBindings(panel, submitButton, cancelButton);

	    dialog.setVisible(true);
	}

	

	public PrintPreviewPanel getPrintPreviewPanel() {
		return printPreviewPanel;
	}

	private void showDateRangeDialog() {
		// Create date pickers
		DatePicker startDatePicker = new DatePicker();
		DatePicker endDatePicker = new DatePicker();

		// Create panel for the dialog
		JPanel panel = new JPanel(new GridLayout(3, 2));
		panel.add(new JLabel("Start Date:"));
		panel.add(startDatePicker.getDatePicker());
		panel.add(new JLabel("End Date:"));
		panel.add(endDatePicker.getDatePicker());

		// Create buttons
		JButton submitButton = new JButton("Submit");
		JButton cancelButton = new JButton("Cancel");

		// Add buttons to the panel
		panel.add(submitButton);
		panel.add(cancelButton);

		// Create the dialog
		JDialog dialog = new JDialog((Frame) null, "Choose Date Range", true);
		dialog.getContentPane().add(panel);
		dialog.pack();
		dialog.setLocationRelativeTo(null);

		// Add action listeners for buttons
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				java.util.Date startDate = (java.util.Date) startDatePicker.getDatePicker().getModel().getValue();
				java.util.Date endDate = (java.util.Date) endDatePicker.getDatePicker().getModel().getValue();
				generatePayrollReport(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
				dialog.dispose();
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		// Add key bindings
		KeyBindingUtil.addSubmitAndCancelBindings(panel, submitButton, cancelButton);

		// Show the dialog
		dialog.setVisible(true);
	}
	private void generatePayrollReport(java.sql.Date startDate, java.sql.Date endDate) {
	    // Fetch the data using the controller
	    List<TimeSheet> timeSheets = controller.getTimeSheetsByDateRange(startDate, endDate);
	    List<Employee> employees = controller.getAllEmployees();

	    // Generate the report
	    TimeSheetReport report = new TimeSheetReport(timeSheets, employees, new java.util.Date(startDate.getTime()), new java.util.Date(endDate.getTime()));
	    JPanel reportPanel = report.generateReportPanel();

	    // Format the dates
	    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");
	    String formattedStartDate = dateFormat.format(new java.util.Date(startDate.getTime()));
	    String formattedEndDate = dateFormat.format(new java.util.Date(endDate.getTime()));

	    // Display the report in the print preview panel with formatted dates
	    String reportTitle = "Time Sheet Report (" + formattedStartDate + " - " + formattedEndDate + ")";
	    printPreviewPanel.addReportContent(reportPanel, reportTitle);
	}


	private void generateCallLogsReport(java.sql.Date startDate, java.sql.Date endDate) {
	    // Fetch the data using the controller
	    List<DailyCallLog> dailyCallLogs = controller.getCallLogsForReportPanel(startDate, endDate);

	    // Generate the report panel
	    CallLogReport report = new CallLogReport(dailyCallLogs, startDate, endDate);
	    JPanel reportPanel = report.generateReportPanel();

	    // Format the dates
	    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
	    String formattedStartDate = dateFormat.format(new java.util.Date(startDate.getTime()));
	    String formattedEndDate = dateFormat.format(new java.util.Date(endDate.getTime()));

	    // Display the report in the print preview panel with formatted dates
	    String reportTitle = "Call Logs and Ambulance Calls Report (" + formattedStartDate + " - " + formattedEndDate + ")";
	    printPreviewPanel.addReportContent(reportPanel, reportTitle);
	    
	    
	}

	
	private void generateEmployeeReport() {
	    // Fetch the data using the controller
	    List<Employee> employees = controller.getAllEmployees();
	    // Generate the report panel
	    EmployeeReport report = new EmployeeReport(employees);
	    JPanel reportPanel = report.generateReportPanel();


	    // Display the report in the print preview panel with formatted dates
	    String reportTitle = "Employee Report";
	    printPreviewPanel.addReportContent(reportPanel, reportTitle);
	}

	
	private void generateTotalCountSummaryReport(java.sql.Date startDate, java.sql.Date endDate) {
	    // Fetch the data using the controller
	    List<AmbulanceCall> calls = controller.getAmbulanceCallsByDateRange(startDate, endDate);

	    // Generate the report panel
	    TotalCountSummary report = new TotalCountSummary(calls, startDate, endDate);
	    JPanel reportPanel = report.generateReportPanel();

	    // Format the dates
	    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
	    String formattedStartDate = dateFormat.format(new java.util.Date(startDate.getTime()));
	    String formattedEndDate = dateFormat.format(new java.util.Date(endDate.getTime()));

	    // Display the report in the print preview panel with formatted dates
	    String reportTitle = "Total Count Summary Report (" + formattedStartDate + " - " + formattedEndDate + ")";
	    printPreviewPanel.addReportContent(reportPanel, reportTitle);
	}

	

	
	
	



}
