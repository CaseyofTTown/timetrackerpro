package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrintPreviewPanel extends JPanel {
	private JTable reportTable;
	private JTable summaryTable;
	private JButton printButton;
	private Date startDate;
	private Date endDate;

	public PrintPreviewPanel() {
		setLayout(new BorderLayout(10, 10)); // Add margins

		// Initialize tables
		reportTable = new JTable();
		summaryTable = new JTable();

		// Add the tables to the panel with margins
		JPanel reportPanel = new JPanel(new BorderLayout());
		reportPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		reportPanel.add(reportTable.getTableHeader(), BorderLayout.NORTH);
		reportPanel.add(reportTable, BorderLayout.CENTER);

		JPanel summaryPanel = new JPanel(new BorderLayout());
		summaryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		summaryPanel.add(summaryTable.getTableHeader(), BorderLayout.NORTH);
		summaryPanel.add(summaryTable, BorderLayout.CENTER);

		// Add the print button
		printButton = new JButton("Print");
		printButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				printReport();
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(printButton);

		add(reportPanel, BorderLayout.NORTH);
		add(summaryPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	public void setReportContent(JTable reportTable, Date startDate, Date endDate) {
		// Update the tables with new data
		this.reportTable.setModel(reportTable.getModel());
		this.startDate = startDate;
		this.endDate = endDate;

		// Revalidate and repaint to refresh the display
		revalidate();
		repaint();
	}

	private void printReport() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateRange = sdf.format(startDate) + " to " + sdf.format(endDate);
		MessageFormat header = new MessageFormat("Report Date Range: " + dateRange);
		MessageFormat footer = new MessageFormat("Page {0}");

		try {
			boolean complete = reportTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
			if (complete) {
				JOptionPane.showMessageDialog(this, "Printing Complete", "Print Result",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Printing Cancelled", "Print Result",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (PrinterException pe) {
			JOptionPane.showMessageDialog(this, "Printing Failed: " + pe.getMessage(), "Print Result",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
