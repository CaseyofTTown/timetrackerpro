package view;

import javax.swing.*;

import controller.TTController;
import model.ColorConstants;
import model.Employee;
import model.KeyBindingUtil;
import model.TimeSheet;
import model.TimeSheetReport;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

		JButton addCertificationListButton = new JButton("Add Certification List");
		addCertificationListButton.setBackground(ColorConstants.DEEP_BLUE);
		addCertificationListButton.setForeground(ColorConstants.LIME_GREEN);

		JButton addCallLogButton = new JButton("Add Call Log");
		addCallLogButton.setBackground(ColorConstants.DEEP_BLUE);
		addCallLogButton.setForeground(ColorConstants.LIME_GREEN);

		controlPanel.add(addTimeSheetButton);
		controlPanel.add(addCertificationListButton);
		controlPanel.add(addCallLogButton);

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

	    // Convert java.sql.Date to java.util.Date
	    java.util.Date utilStartDate = new java.util.Date(startDate.getTime());
	    java.util.Date utilEndDate = new java.util.Date(endDate.getTime());

	    // Generate the report
	    TimeSheetReport report = new TimeSheetReport(timeSheets, employees, utilStartDate, utilEndDate);
	    JTable reportTable = report.generateReportTable();

	    // Display the report in the print preview panel
	    printPreviewPanel.setReportContent(reportTable, utilStartDate, utilEndDate);
	}



}
