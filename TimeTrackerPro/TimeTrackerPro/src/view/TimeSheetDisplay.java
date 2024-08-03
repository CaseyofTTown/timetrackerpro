package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.ColorConstants;
import model.TimeSheet;

import java.awt.*;

public class TimeSheetDisplay extends JTable {

	private DefaultTableModel model;

	public TimeSheetDisplay() {
		model = new DefaultTableModel();
		setModel(model);
		setBackground(ColorConstants.CHARCOAL);
		setForeground(ColorConstants.LIME_GREEN);
		setFont(new Font("Arial", Font.PLAIN, 12));
		setRowHeight(25);
		getTableHeader().setBackground(ColorConstants.DEEP_BLUE);
		getTableHeader().setForeground(ColorConstants.LIME_GREEN);
		getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

		// Define columns
		model.addColumn("Employee Name");
		model.addColumn("Shift Start Date");
		model.addColumn("Shift End Date");
		model.addColumn("Shift Start Time");
		model.addColumn("Shift End Time");
		model.addColumn("Hours Worked");

	}

	  // Method to add a new time sheet entry
    public void addTimeSheetEntry(TimeSheet timesheet) {
        model.addRow(new Object[]{
            timesheet.getEmployeeName(),
            timesheet.getShiftStartDate(),
            timesheet.getShiftEndDate(),
            timesheet.getShiftStartTime(),
            timesheet.getShiftEndTime(),
            timesheet.getHoursWorked()
        });
    }

	// Method to update an existing time sheet entry
	public void updateTimeSheetEntry(int rowIndex, TimeSheet timesheet) {
		model.setValueAt(timesheet.getEmployeeName(), rowIndex, 0);
		model.setValueAt(timesheet.getShiftStartDate(), rowIndex, 1);
		model.setValueAt(timesheet.getShiftEndDate(), rowIndex, 2);
		model.setValueAt(timesheet.getShiftStartTime(), rowIndex, 3);
		model.setValueAt(timesheet.getShiftEndTime(), rowIndex, 4);
		model.setValueAt(timesheet.getHoursWorked(), rowIndex, 5);
	}

	// Method to remove a time sheet entry
	public void removeTimeSheetEntry(int rowIndex) {
		model.removeRow(rowIndex);
	}

	// Method to clear all entries
	public void clearAllEntries() {
		model.setRowCount(0);
	}
}
