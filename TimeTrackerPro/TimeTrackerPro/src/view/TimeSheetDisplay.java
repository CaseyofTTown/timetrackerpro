package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;
import java.util.List;
import model.ColorConstants;
import model.TimeSheet;
import java.awt.*;
import java.text.SimpleDateFormat;

public class TimeSheetDisplay extends JTable {

	private DefaultTableModel model;
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private List<Integer> timeSheetIds;

	public TimeSheetDisplay() {
		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int rod, int column) {
				return false;
			}
		};
		setModel(model);
		setBackground(ColorConstants.CHARCOAL);
		setForeground(ColorConstants.LIME_GREEN);
		setFont(new Font("Arial", Font.PLAIN, 12));
		setRowHeight(25);
		getTableHeader().setBackground(ColorConstants.DEEP_BLUE);
		getTableHeader().setForeground(ColorConstants.LIME_GREEN);
		getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

		// Define columns
		model.addColumn("Employee Name");
		model.addColumn("Shift Start Date");
		model.addColumn("Shift End Date");
		model.addColumn("Shift Start Time");
		model.addColumn("Shift End Time");
		model.addColumn("Hours Worked");
		model.addColumn("Overtime Comments");

		// Set custom cell renderer
		setDefaultRenderer(Object.class, new CustomCellRenderer());

		timeSheetIds = new ArrayList<>();

		System.out.println("TimeSheetDisplay created");
	}

	@Override
	protected void paintComponent(Graphics g) {
		System.out.println("paintComponent called on timeSheetDisplay"); // Debug statement
		super.paintComponent(g);
		if (model.getRowCount() == 0) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(ColorConstants.CHARCOAL); // Set background color
			g2.fillRect(0, 0, getWidth(), getHeight()); // Fill the background
			g2.setColor(ColorConstants.LIME_GREEN);
			g2.setFont(new Font("Arial", Font.BOLD, 18));
			FontMetrics fm = g2.getFontMetrics();
			String message = "No data to display";
			int x = (getWidth() - fm.stringWidth(message)) / 2;
			int y = getHeight() / 2;
			g2.drawString(message, x, y);
			System.out.println("setting paint component timeSheetDisplay");
		}
	}

	// Custom cell renderer class
	private class CustomCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (isSelected) {
				c.setBackground(ColorConstants.DEEP_BLUE);
				c.setForeground(ColorConstants.GOLD);
			} else {
				c.setBackground(ColorConstants.CHARCOAL);
				c.setForeground(ColorConstants.LIME_GREEN);
			}
			return c;
		}
	}

	// Method to add a new time sheet entry
	public void addTimeSheetEntry(TimeSheet timesheet) {
		model.addRow(new Object[] { timesheet.getEmployeeName(), formatDate(timesheet.getShiftStartDate()),
				formatDate(timesheet.getShiftEndDate()), timesheet.getShiftStartTime(), timesheet.getShiftEndTime(),
				timesheet.getFormattedHoursWorked(), timesheet.getOvertimeComment() });
		timeSheetIds.add(timesheet.getTimeSheetId());
		revalidate();
		repaint();
	}

	// Method to add all time sheet entries
	public void addAllTimeSheetsToDisplay(List<TimeSheet> timesheets) {
		for (TimeSheet timesheet : timesheets) {
			model.addRow(new Object[] { timesheet.getEmployeeName(), formatDate(timesheet.getShiftStartDate()),
					formatDate(timesheet.getShiftEndDate()), timesheet.getShiftStartTime(), timesheet.getShiftEndTime(),
					timesheet.getFormattedHoursWorked(), timesheet.getOvertimeComment() });
			timeSheetIds.add(timesheet.getTimeSheetId());

		}

		revalidate();
		repaint();
	}

	// Method to update an existing time sheet entry
	public void updateTimeSheetEntry(int rowIndex, TimeSheet timesheet) {
		model.setValueAt(timesheet.getEmployeeName(), rowIndex, 0);
		model.setValueAt(formatDate(timesheet.getShiftStartDate()), rowIndex, 1);
		model.setValueAt(formatDate(timesheet.getShiftEndDate()), rowIndex, 2);
		model.setValueAt(timesheet.getShiftStartTime(), rowIndex, 3);
		model.setValueAt(timesheet.getShiftEndTime(), rowIndex, 4);
		model.setValueAt(timesheet.getFormattedHoursWorked(), rowIndex, 5);
		model.setValueAt(timesheet.getOvertimeComment(), rowIndex, 6);
		revalidate();
		repaint();
	}

	// Method to remove a time sheet entry
	public void removeTimeSheetEntry(int rowIndex) {
		model.removeRow(rowIndex);
		timeSheetIds.remove(rowIndex);
		revalidate();
		repaint();
	}

	// Method to clear all entries
	public void clearAllEntries() {
		model.setRowCount(0);
		timeSheetIds.clear();
		revalidate();
		repaint();
	}

	// Helper method to format date
	private String formatDate(java.util.Date date) {
		return DATE_FORMAT.format(date);
	}
	
	public int getSelectedTimeSheetId() {
		int selectedRow = getSelectedRow();
		if(selectedRow != -1) {
			System.out.println("selected timeSheetRow: " + selectedRow);
			return timeSheetIds.get(selectedRow);
		}
		throw new IllegalStateException("No row selected");
	}

}
