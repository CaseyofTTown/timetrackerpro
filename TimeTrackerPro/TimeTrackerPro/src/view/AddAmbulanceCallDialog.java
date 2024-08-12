package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import model.AmbulanceCall;
import model.ColorConstants;
import model.TypeOfCallEnum;
import controller.TTController;

public class AddAmbulanceCallDialog extends JDialog {
	private DatePicker callDateField;
	private TitledTextField patientsNameField;
	private JComboBox<TypeOfCallEnum> callCategoryField;
	private TitledTextField pickupLocationField;
	private TitledTextField dropoffLocationField;
	private TitledTextField totalMilesField;
	private TitledTextField insuranceField;
	private TitledTextField aicEmployeeField;
	private TTController controller;
	private int dailyLogId;
	private JButton submitButton;

	public AddAmbulanceCallDialog(Frame owner, TTController controller, int dailyLogId, Date[] callDates) {
		super(owner, "Add Ambulance Call", true);
		this.controller = controller;
		this.dailyLogId = dailyLogId;
		setLocationRelativeTo(null); // Center the dialog

		// Set dark layout
		getContentPane().setBackground(ColorConstants.DARK_GRAY);

		// Create and add components with padding
		JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.setBackground(ColorConstants.DARK_GRAY);

		callDateField = new DatePicker();
		JLabel callDateLabel = new JLabel("Call Date:");
		callDateLabel.setForeground(ColorConstants.GOLD);
		panel.add(callDateLabel);
		panel.add(callDateField.getDatePicker());

		callCategoryField = new JComboBox<>(TypeOfCallEnum.values());
		callCategoryField.setBackground(ColorConstants.DARK_GRAY);
		callCategoryField.setForeground(ColorConstants.GOLD);
		JLabel callCategoryLabel = new JLabel("Call Category:");
		callCategoryLabel.setForeground(ColorConstants.GOLD);
		panel.add(callCategoryLabel);
		panel.add(callCategoryField);

		patientsNameField = new TitledTextField("Patient's Name", "", 20);
		panel.add(patientsNameField);

		pickupLocationField = new TitledTextField("Pickup Location", "", 20);
		panel.add(pickupLocationField);

		dropoffLocationField = new TitledTextField("Dropoff Location", "", 20);
		panel.add(dropoffLocationField);

		totalMilesField = new TitledTextField("Total trip miles", "", 20);
		panel.add(totalMilesField);

		insuranceField = new TitledTextField("Insurance", "", 20);
		panel.add(insuranceField);

		aicEmployeeField = new TitledTextField("AIC Name", "", 20);
		panel.add(aicEmployeeField);

		// Submit button
		submitButton = new JButton("Submit");
		submitButton.setBackground(ColorConstants.DEEP_BLUE);
		submitButton.setForeground(ColorConstants.LIME_GREEN);
		submitButton.setEnabled(false);
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Collect data from fields
				Date callDate = (Date) callDateField.getDatePicker().getModel().getValue();
				String patientsName = patientsNameField.getText();
				TypeOfCallEnum callCategory = (TypeOfCallEnum) callCategoryField.getSelectedItem();
				String pickupLocation = pickupLocationField.getText();
				String dropoffLocation = dropoffLocationField.getText();
				int totalMiles = Integer.parseInt(totalMilesField.getText());
				String insurance = insuranceField.getText();
				String aicEmployee = aicEmployeeField.getText();

				// Create an AmbulanceCall object
				AmbulanceCall call;
				try {
					if (callCategory == TypeOfCallEnum.Refusal) {
						call = new AmbulanceCall(dailyLogId, callDate, callCategory, pickupLocation, aicEmployee);
					} else {
						call = new AmbulanceCall(dailyLogId, callDate, patientsName, callCategory, pickupLocation,
								dropoffLocation, totalMiles, insurance, aicEmployee);
					}

					// Use the controller to submit the call to the database
					controller.addAmbulanceCall(call);
				} catch (Exception exception) {
					exception.printStackTrace();
				}

				// Close the dialog
				dispose();
			}
		});
		panel.add(submitButton);

		// Cancel button
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBackground(ColorConstants.CRIMSON_RED);
		cancelButton.setForeground(ColorConstants.WHITE);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panel.add(cancelButton);

		add(panel);

		// Add listener to callCategoryField
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
				// Enable submit button if a valid selection is made and required fields are
				// filled
				submitButton.setEnabled(selectedCategory != null && isFormValid());
				System.out.println("Call category selected: " + selectedCategory);
			}
		});
		
		pack();
	}

	private boolean isFormValid() {
		if (callCategoryField.getSelectedItem() == TypeOfCallEnum.Refusal) {
			return !pickupLocationField.getText().isEmpty() && !aicEmployeeField.getText().isEmpty();
		} else {
			return !patientsNameField.getText().isEmpty() && !pickupLocationField.getText().isEmpty()
					&& !dropoffLocationField.getText().isEmpty() && !totalMilesField.getText().isEmpty()
					&& !insuranceField.getText().isEmpty() && !aicEmployeeField.getText().isEmpty();
		}
	}
}
