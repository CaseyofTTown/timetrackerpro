 package view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.AmbulanceCall;
import model.ColorConstants;
import model.KeyBindingUtil;
import model.TypeOfCallEnum;
import controller.TTController;
import java.util.List;

public class AddAmbulanceCallDialog extends JDialog {
	private JComboBox<Date> callDateField;
	private JComboBox<String> aicEmployeeField;
	private TitledTextField patientsNameField;
	private JComboBox<TypeOfCallEnum> callCategoryField;
	private TitledTextField pickupLocationField;
	private TitledTextField dropoffLocationField;
	private TitledTextField totalMilesField;
	private TitledTextField insuranceField;
	private TTController controller;
	private JButton cancelButton;
	private int dailyLogId;
	private JButton submitButton;
	private boolean isModifyMode;
	private AmbulanceCall existingCall;

	public AddAmbulanceCallDialog(Frame owner, TTController controller, int dailyLogId, Date[] callDates,
			List<String> employees, boolean isModifyMode) {
		super(owner, "Add Ambulance Call", true);
		this.controller = controller;
		this.dailyLogId = dailyLogId;
		this.isModifyMode = isModifyMode;
		setLocationRelativeTo(null); // Center the dialog

		// Set dark layout
		getContentPane().setBackground(ColorConstants.DARK_GRAY);

		// Create and add components with padding
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding on left and right edges
		panel.setBackground(ColorConstants.DARK_GRAY);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5); // Little space between fields
		gbc.fill = GridBagConstraints.HORIZONTAL;

		callDateField = new JComboBox<>(callDates);
        callDateField.setRenderer(new DefaultListCellRenderer() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                if (value instanceof Date) {
                    value = formatter.format((Date) value);
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        callDateField.setBackground(ColorConstants.DARK_GRAY);
        callDateField.setForeground(ColorConstants.GOLD);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(callDateField, gbc);

		callCategoryField = new JComboBox<>(TypeOfCallEnum.values());
		callCategoryField.setBackground(ColorConstants.DARK_GRAY);
		callCategoryField.setForeground(ColorConstants.GOLD);
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(callCategoryField, gbc);

		patientsNameField = new TitledTextField("Patient's Name", "", 20);
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(patientsNameField, gbc);

		pickupLocationField = new TitledTextField("Pickup Location", "", 20);
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(pickupLocationField, gbc);

		dropoffLocationField = new TitledTextField("Dropoff Location", "", 20);
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(dropoffLocationField, gbc);

		totalMilesField = new TitledTextField("Total trip miles", "", 20);
		gbc.gridx = 1;
		gbc.gridy = 2;
		panel.add(totalMilesField, gbc);

		insuranceField = new TitledTextField("Insurance", "", 20);
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(insuranceField, gbc);

		aicEmployeeField = new JComboBox<>(employees.toArray(new String[0]));
		aicEmployeeField.setBackground(ColorConstants.DARK_GRAY);
		aicEmployeeField.setForeground(ColorConstants.GOLD);
		gbc.gridx = 1;
		gbc.gridy = 3;
		panel.add(aicEmployeeField, gbc);

		// Submit button
		submitButton = new JButton(isModifyMode ? "Update" : "Submit");
		submitButton.setBackground(ColorConstants.DEEP_BLUE);
		submitButton.setForeground(ColorConstants.LIME_GREEN);
		submitButton.setEnabled(false);
		submitButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // Collect data from fields
		        Date callDate = (Date) callDateField.getSelectedItem();
		        String patientsName = patientsNameField.getText();
		        TypeOfCallEnum callCategory = (TypeOfCallEnum) callCategoryField.getSelectedItem();
		        String pickupLocation = pickupLocationField.getText();
		        String dropoffLocation = dropoffLocationField.getText();
		        int totalMiles = totalMilesField.getText().trim().isEmpty() ? 0
		                : Integer.parseInt(totalMilesField.getText().trim());
		        String insurance = insuranceField.getText();
		        String aicEmployee = (String) aicEmployeeField.getSelectedItem();

		        try {
		            AmbulanceCall call;
		            if (callCategory == TypeOfCallEnum.Refusal) {
		                call = new AmbulanceCall(dailyLogId, callDate, callCategory, pickupLocation, aicEmployee);
		            } else {
		                call = new AmbulanceCall(dailyLogId, callDate, patientsName, callCategory, pickupLocation,
		                        dropoffLocation, totalMiles, insurance, aicEmployee);
		            }

		            if (isModifyMode && existingCall != null) {
		                // Update the existing call
		                call.setId(existingCall.getId()); //get existing call ids and add to new obj used to update
		                controller.updateAmbulanceCall(call);
		            } else {
		                // Create a new call
		                controller.addAmbulanceCall(call);
		                
		            }
		        } catch (Exception exception) {
		            exception.printStackTrace();
		        }

		        // Close the dialog
		        dispose();
		    }
		});

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		panel.add(submitButton, gbc);

		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setBackground(ColorConstants.CRIMSON_RED);
		cancelButton.setForeground(ColorConstants.WHITE);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		gbc.gridy = 5;
		panel.add(cancelButton, gbc);

		add(panel);

		// Ensure the ActionListener for callCategoryField correctly enables the
		// submitButton
		callCategoryField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TypeOfCallEnum selectedCategory = (TypeOfCallEnum) callCategoryField.getSelectedItem();
				if (selectedCategory == TypeOfCallEnum.Refusal) {
					patientsNameField.setEnabled(false);
					dropoffLocationField.setEnabled(false);
					totalMilesField.setEnabled(false);
					insuranceField.setEnabled(false);
				} else {
					patientsNameField.setEnabled(true);
					dropoffLocationField.setEnabled(true);
					totalMilesField.setEnabled(true);
					insuranceField.setEnabled(true);
				}
				// Check form validity
				checkFormValidity();
				System.out.println("Call category selected: " + selectedCategory);
			}
		});
		
		//set key bindings
		setBindings();

		// Add DocumentListeners to text fields
		addDocumentListener(patientsNameField.getTextField());
		addDocumentListener(pickupLocationField.getTextField());
		addDocumentListener(dropoffLocationField.getTextField());
		addDocumentListener(totalMilesField.getTextField());
		addDocumentListener(insuranceField.getTextField());

		pack(); // Adjust the size of the dialog to fit the components
	}

	// Update the isFormValid() method
	private boolean isFormValid() {
		TypeOfCallEnum selectedCategory = (TypeOfCallEnum) callCategoryField.getSelectedItem();
		if (selectedCategory == TypeOfCallEnum.Refusal) {
			return !pickupLocationField.getText().isEmpty() && aicEmployeeField.getSelectedItem() != null;
		} else {
			return !patientsNameField.getText().isEmpty() && !pickupLocationField.getText().isEmpty()
					&& !dropoffLocationField.getText().isEmpty() && !totalMilesField.getText().isEmpty()
					&& !insuranceField.getText().isEmpty() && aicEmployeeField.getSelectedItem() != null;
		}
	}

	// Add DocumentListener to text fields
	private void addDocumentListener(JTextField textField) {
		textField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				checkFormValidity();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				checkFormValidity();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				checkFormValidity();
			}
		});
	}

	// Method to check form validity and enable/disable submit button
	private void checkFormValidity() {
		submitButton.setEnabled(isFormValid());
	}
	
	//set key bindings
	private void setBindings() {
        KeyBindingUtil.addSubmitAndCancelBindings(this.getRootPane(), submitButton, cancelButton);
    }
	
	//set fields with an existing ambulance call
	public void setCallDetails(AmbulanceCall call) {
	    this.existingCall = call;
	    callDateField.setSelectedItem(call.getCallDate());
	    patientsNameField.setText(call.getPatientsName());
	    callCategoryField.setSelectedItem(call.getCallCategory());
	    pickupLocationField.setText(call.getPickupLocation());
	    dropoffLocationField.setText(call.getDropoffLocation());
	    totalMilesField.setText(String.valueOf(call.getTotalMiles()));
	    insuranceField.setText(call.getInsurance());
	    aicEmployeeField.setSelectedItem(call.getAicName());
	}

}
