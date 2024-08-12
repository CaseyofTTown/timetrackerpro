package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.TTController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import model.DailyCallLog;
import model.AmbulanceCall;
import model.ColorConstants;

public class CallLogCard extends JPanel {
	private DailyCallLog callLog;
	private JButton expandButton;
	private JPanel detailsPanel;
	private JButton addCallButton;
	private JTable callTable;
	private DefaultTableModel callTableModel;
	private TTController controller;
	private JPanel headerPanel;
	private boolean isSelected;
	private int callLogId;
	private static CallLogCard selectedCard = null; // keep track of selected card

	public CallLogCard(DailyCallLog callLog, TTController controller) {
		this.callLog = callLog;
		this.callLogId = callLog.getId();
		this.controller = controller;
		setLayout(new BorderLayout());
		setBackground(ColorConstants.DARK_GRAY);
		setBorder(BorderFactory.createLineBorder(ColorConstants.GOLD, 2));

		// Header panel
		headerPanel = new JPanel(new GridLayout(2, 1));
		headerPanel.setBackground(ColorConstants.DARK_GRAY);
		headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// MouseListener to detect selection
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (selectedCard != null && selectedCard != CallLogCard.this) {
					selectedCard.deselect(); // Deselect the previously selected card
				}
				isSelected = !isSelected;
				setBackground(isSelected ? ColorConstants.DEEP_BLUE : ColorConstants.DARK_GRAY);
				headerPanel.setBackground(isSelected ? ColorConstants.DEEP_BLUE : ColorConstants.DARK_GRAY);

				firePropertyChange("selectedCard", !isSelected, isSelected);
				if (isSelected) {
					selectedCard = CallLogCard.this; // Update the selected card
				} else {
					selectedCard = null; // No card is selected
				}
			}
		});

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = dateFormat.format(callLog.getStartDate());
		String endDate = dateFormat.format(callLog.getEndDate());

		JLabel headerLabel = new JLabel(
				"Truck Unit: " + callLog.getTruckUnitNumber() + " | Date: " + startDate + " - " + endDate);
		headerLabel.setForeground(ColorConstants.ORANGE);
		headerLabel.setBackground(ColorConstants.DARK_GRAY);
		headerPanel.add(headerLabel, BorderLayout.NORTH);

		JLabel crewLabel = new JLabel("Crew Members: " + String.join(", ", callLog.getCrewMembers()));
		crewLabel.setForeground(ColorConstants.LIME_GREEN);
		headerPanel.add(crewLabel, BorderLayout.CENTER);

		expandButton = new JButton("Expand");
		expandButton.setBackground(ColorConstants.DARK_GRAY);
		expandButton.setForeground(ColorConstants.GOLD);
		expandButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				toggleDetails();
			}
		});
		headerPanel.add(expandButton);

		add(headerPanel, BorderLayout.NORTH);

		// Details panel
		detailsPanel = new JPanel(new BorderLayout());
		detailsPanel.setBackground(ColorConstants.DARK_GRAY);
		detailsPanel.setVisible(false);

		// Table for Ambulance Calls
		callTableModel = new DefaultTableModel(new Object[] { "Call Date", "Patients Name", "Call Category",
				"Pickup Location", "Dropoff Location", "Total Miles", "Insurance", "AIC Name", "Actions" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 8; // Only the Actions column is editable
			}
		};
		callTable = new JTable(callTableModel);
		callTable.setBackground(ColorConstants.CHARCOAL);
		callTable.setForeground(ColorConstants.LIME_GREEN);
		callTable.setFont(new Font("Arial", Font.PLAIN, 12));
		callTable.setRowHeight(25);
		callTable.getTableHeader().setBackground(ColorConstants.DEEP_BLUE);
		callTable.getTableHeader().setForeground(ColorConstants.LIME_GREEN);
		callTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		callTable.setDefaultRenderer(Object.class, new CallTableCellRenderer());

		// Populate the table with Ambulance Calls
		for (AmbulanceCall call : callLog.getAmbulanceCalls()) {
			callTableModel.addRow(new Object[] { call.getCallDate(), call.getPatientsName(), call.getCallCategory(),
					call.getPickupLocation(), call.getDropoffLocation(), call.getTotalMiles(), call.getInsurance(),
					call.getAicName(), createDeleteButton(call) });
		}

		detailsPanel.add(new JScrollPane(callTable), BorderLayout.CENTER);

		// Add Call button
		addCallButton = new JButton("Add Call");
		addCallButton.setBackground(ColorConstants.SLATE_GRAY);
		addCallButton.setForeground(ColorConstants.GOLD);
		detailsPanel.add(addCallButton, BorderLayout.SOUTH);

		add(detailsPanel, BorderLayout.CENTER);
	}

	public void deselect() {
		isSelected = false;
		setBackground(ColorConstants.CHARCOAL);
		headerPanel.setBackground(isSelected ? ColorConstants.DEEP_BLUE : ColorConstants.DARK_GRAY);

		firePropertyChange("selectedCard", true, false);
	}

	private void toggleDetails() {
		detailsPanel.setVisible(!detailsPanel.isVisible());
		expandButton.setText(detailsPanel.isVisible() ? "Collapse" : "Expand");
	}

	private JButton createDeleteButton(AmbulanceCall call) {
		JButton deleteButton = new JButton("Delete");
		deleteButton.setBackground(ColorConstants.DEEP_BLUE);
		deleteButton.setForeground(ColorConstants.GOLD);
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Logic to delete the call
				controller.deleteAmbulanceCall(call);
				callTableModel.removeRow(callTable.getSelectedRow());
			}
		});
		return deleteButton;
	}

	// Custom cell renderer for the table of ambulance calls
	private class CallTableCellRenderer extends DefaultTableCellRenderer {
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
			if (column == 8) { // Actions column
				return (Component) value;
			}
			return c;
		}
	}

	public JButton getAddCallButton() {
		return addCallButton;
	}

	public int getCallLogId() {
		return this.callLogId;
	}
}
