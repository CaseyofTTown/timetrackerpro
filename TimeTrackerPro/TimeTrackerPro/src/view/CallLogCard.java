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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.DailyCallLog;
import model.AmbulanceCall;
import model.CallLogCardSelectionListener;
import model.ColorConstants;

public class CallLogCard extends JPanel {
	private CallLogCardSelectionListener selectionListener;

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
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public CallLogCard(DailyCallLog callLog, TTController controller) {
		this.callLog = callLog;
		this.callLogId = callLog.getId();
		this.controller = controller;
		setLayout(new BorderLayout());
		setBackground(ColorConstants.DARK_GRAY);
		setBorder(BorderFactory.createLineBorder(ColorConstants.GOLD, 2));

		// Header panel
		headerPanel = new JPanel(new GridBagLayout());
		headerPanel.setBackground(ColorConstants.DARK_GRAY);
		headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = dateFormat.format(callLog.getStartDate());
		String endDate = dateFormat.format(callLog.getEndDate());

		// Truck Unit
		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel truckLabel = new JLabel("Truck Unit: ");
		truckLabel.setForeground(ColorConstants.GOLD);
		truckLabel.setFont(new Font("Arial", Font.BOLD, 14));
		headerPanel.add(truckLabel, gbc);

		gbc.gridx = 1;
		JLabel truckValueLabel = new JLabel(callLog.getTruckUnitNumber());
		truckValueLabel.setForeground(ColorConstants.ORANGE);
		truckValueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		headerPanel.add(truckValueLabel, gbc);

		// Log Start Date
		gbc.gridx = 2;
		JLabel startDateLabel = new JLabel("Log Start Date: ");
		startDateLabel.setForeground(ColorConstants.GOLD);
		startDateLabel.setFont(new Font("Arial", Font.BOLD, 14));
		headerPanel.add(startDateLabel, gbc);

		gbc.gridx = 3;
		JLabel startDateValueLabel = new JLabel(startDate);
		startDateValueLabel.setForeground(ColorConstants.ORANGE);
		startDateValueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		headerPanel.add(startDateValueLabel, gbc);

		// Log End Date
		gbc.gridx = 4;
		JLabel endDateLabel = new JLabel("Log End Date: ");
		endDateLabel.setForeground(ColorConstants.GOLD);
		endDateLabel.setFont(new Font("Arial", Font.BOLD, 14));
		headerPanel.add(endDateLabel, gbc);

		gbc.gridx = 5;
		JLabel endDateValueLabel = new JLabel(endDate);
		endDateValueLabel.setForeground(ColorConstants.ORANGE);
		endDateValueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		headerPanel.add(endDateValueLabel, gbc);

		// Crew Members
		gbc.gridx = 6;
		JLabel crewLabel = new JLabel("Crew Members: ");
		crewLabel.setForeground(ColorConstants.GOLD);
		crewLabel.setFont(new Font("Arial", Font.BOLD, 14));
		headerPanel.add(crewLabel, gbc);

		gbc.gridx = 7;
		JLabel crewValueLabel = new JLabel(String.join(", ", callLog.getCrewMembers()));
		crewValueLabel.setForeground(ColorConstants.ORANGE);
		crewValueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		headerPanel.add(crewValueLabel, gbc);

		// Expand Button
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 8;
		gbc.anchor = GridBagConstraints.CENTER;
		expandButton = new JButton("Expand");
		expandButton.setBackground(ColorConstants.DARK_GRAY);
		expandButton.setForeground(ColorConstants.GOLD);
		expandButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleDetails();
			}
		});
		headerPanel.add(expandButton, gbc);

		add(headerPanel, BorderLayout.NORTH);

		// Details panel
		detailsPanel = new JPanel(new BorderLayout());
		detailsPanel.setBackground(ColorConstants.DARK_GRAY);
		detailsPanel.setVisible(false);

		// Table for Ambulance Calls
		callTableModel = new DefaultTableModel(new Object[] { "Call Date", "Patients Name", "Call Category",
				"Pickup Location", "Dropoff Location", "Total Miles", "Insurance", "AIC Name" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Only the Actions column is editable
			}
		};
		callTable = new JTable(callTableModel);
		callTable.setBackground(ColorConstants.CHARCOAL);
		callTable.setForeground(ColorConstants.LIME_GREEN);
		callTable.setFont(new Font("Arial", Font.PLAIN, 12));
		callTable.setRowHeight(25);
		callTable.getTableHeader().setBackground(ColorConstants.DARK_GRAY);
		callTable.getTableHeader().setForeground(ColorConstants.LIME_GREEN);
		callTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		callTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		callTable.setDefaultRenderer(Object.class, new CallTableCellRenderer());

	
		// Populate the table with Ambulance Calls
		populateCallTable();

		 // Add the table header and table to the details panel
        detailsPanel.add(callTable.getTableHeader(), BorderLayout.NORTH);
		detailsPanel.add(callTable, BorderLayout.CENTER);

		// Add Call button
		addCallButton = new JButton("Add Call");
		addCallButton.setBackground(ColorConstants.SLATE_GRAY);
		addCallButton.setForeground(ColorConstants.GOLD);
		addCallButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showAddAmbulanceCallPopup();
			}
		});

		// modify call button
		JButton modifyCallButton = new JButton("Modify Call");
		modifyCallButton.setBackground(ColorConstants.SLATE_GRAY);
		modifyCallButton.setForeground(ColorConstants.GOLD);
		modifyCallButton.setEnabled(false);

		// Delete call button
		JButton deleteCallButton = new JButton("Delete Call");
		deleteCallButton.setBackground(ColorConstants.CRIMSON_RED);
		deleteCallButton.setForeground(ColorConstants.WHITE);
		deleteCallButton.setEnabled(false);

		// button panel with add/modify/and delete
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBackground(ColorConstants.DARK_GRAY);
		buttonPanel.add(addCallButton);
		buttonPanel.add(modifyCallButton);
		buttonPanel.add(deleteCallButton);

		detailsPanel.add(buttonPanel, BorderLayout.SOUTH);

		// selection listener for the call LOG not ambulance calls
		addSelectionListener();

		add(detailsPanel, BorderLayout.CENTER);

		// selection listener for ambulance call table
		callTable.getSelectionModel().addListSelectionListener(e -> {
			boolean isSelected = callTable.getSelectedRow() != -1;
			modifyCallButton.setEnabled(isSelected);
			deleteCallButton.setEnabled(isSelected);
		});

		// listeners for modify and delete buttons
		modifyCallButton.addActionListener(e -> {
			int selectedRow = callTable.getSelectedRow();
			if (selectedRow != -1) {
				AmbulanceCall selectedCall = callLog.getAmbulanceCalls().get(selectedRow);
				showModifyAmbulanceCallPopup(selectedCall);
			}
		});
		deleteCallButton.addActionListener(e -> {
		    int selectedRow = callTable.getSelectedRow();
		    if (selectedRow != -1) {
		        AmbulanceCall selectedCall = callLog.getAmbulanceCalls().get(selectedRow);
		        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this call?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
		        if (confirm == JOptionPane.YES_OPTION) {
		            controller.deleteAmbulanceCall(selectedCall.getId()); // Call the controller to delete the call
		            refreshData(); // Refresh the table data
		        }
		    }
		});
		
		this.isSelected = false;
	}

	public void showAddAmbulanceCallPopup() {
		System.out.println("adding new ambulance call");
		Date[] callDates = { callLog.getStartDate(), callLog.getEndDate() };
		List<String> employees = callLog.getCrewMembers();
		AddAmbulanceCallDialog dialog = new AddAmbulanceCallDialog((Frame) SwingUtilities.getWindowAncestor(this),
				controller, callLogId, callDates, employees, false);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				refreshData();
			}
		});
		dialog.setVisible(true);

	}
	
	public void showModifyAmbulanceCallPopup(AmbulanceCall selectedCall) {
	    System.out.println("modifying existing ambulance call");
	    Date[] callDates = { callLog.getStartDate(), callLog.getEndDate() };
	    List<String> employees = callLog.getCrewMembers();
	    AddAmbulanceCallDialog dialog = new AddAmbulanceCallDialog((Frame) SwingUtilities.getWindowAncestor(this),
	            controller, callLogId, callDates, employees, true);

	    // Set the values of the dialog using the selected call data
	    dialog.setCallDetails(selectedCall);

	    dialog.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosed(WindowEvent e) {
	            refreshData();
	        }
	    });
	    dialog.setVisible(true);
	}


	private void populateCallTable() {
		callTableModel.setRowCount(0); // Clear existing rows
		for (AmbulanceCall call : callLog.getAmbulanceCalls()) {
			callTableModel.addRow(new Object[] { call.getCallDate(), call.getPatientsName(), call.getCallCategory(),
					call.getPickupLocation(), call.getDropoffLocation(), call.getTotalMiles(), call.getInsurance(),
					call.getAicName() });
		}
	}

	private void refreshData() {
		// Fetch updated data from the controller
		List<AmbulanceCall> updatedCalls = controller.getAmbulanceCallsByID(callLogId);
		callLog.setAmbulanceCalls(updatedCalls);
		populateCallTable();
	}

	// below handles selecting and deselecting a card
	public void setSelectionListener(CallLogCardSelectionListener listener) {
		this.selectionListener = listener;
	}

	
    

	public void deselect() {
		System.out.println("callign deselect in callLogCard");
		isSelected = false;
		setBackground(ColorConstants.CHARCOAL);
		headerPanel.setBackground(isSelected ? ColorConstants.DEEP_BLUE : ColorConstants.DARK_GRAY);
		if(selectionListener != null) {
			selectionListener.onCardSelected(null);
		}
	}

	public void select() {
		System.out.println("callign select in callLogCard on card" + this);

		isSelected = true;
		setBackground(ColorConstants.DEEP_BLUE);
		headerPanel.setBackground(ColorConstants.DEEP_BLUE);
		if (selectionListener != null) {
			selectionListener.onCardSelected(this);
		} else {
			System.out.println("selectionListener was null when select was called");
		}
	}
	
	 public void addSelectionListener() {
	        this.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                System.out.println("Mouse clicked on card: " + CallLogCard.this);
	                if (isSelected) {
	                    deselect();
	                } else {
	                    if (selectedCard != null) {
	                        selectedCard.deselect();
	                    }
	                    select();
	                    selectedCard = CallLogCard.this;
	                }
	            }
	        });
	 }

	private void toggleDetails() {
		detailsPanel.setVisible(!detailsPanel.isVisible());
		expandButton.setText(detailsPanel.isVisible() ? "Collapse" : "Expand");
		revalidate(); // Adjust the size of the card
	}

	private class CallTableCellRenderer extends DefaultTableCellRenderer {
		private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
			if (column == 0 && value instanceof Date) { // Date column
				value = dateFormat.format((Date) value);
				setText(value.toString());
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

	public boolean isSelected() {
		return isSelected;
	}
}
