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
    private int dailyLogId;
    private JButton submitButton;

    public AddAmbulanceCallDialog(Frame owner, TTController controller, int dailyLogId, Date[] callDates, List<String> employees) {
        super(owner, "Add Ambulance Call", true);
        this.controller = controller;
        this.dailyLogId = dailyLogId;
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
        submitButton = new JButton("Submit");
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
                int totalMiles = Integer.parseInt(totalMilesField.getText());
                String insurance = insuranceField.getText();
                String aicEmployee = (String) aicEmployeeField.getSelectedItem();

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
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(submitButton, gbc);

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
        gbc.gridy = 5;
        panel.add(cancelButton, gbc);

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
                // Enable submit button if a valid selection is made and required fields are filled
                submitButton.setEnabled(selectedCategory != null && isFormValid());
                System.out.println("Call category selected: " + selectedCategory);
            }
        });

        pack(); // Adjust the size of the dialog to fit the components
    }

    private boolean isFormValid() {
        if (callCategoryField.getSelectedItem() == TypeOfCallEnum.Refusal) {
            return !pickupLocationField.getText().isEmpty() && aicEmployeeField.getSelectedItem() != null;
        } else {
            return !patientsNameField.getText().isEmpty() && !pickupLocationField.getText().isEmpty()
                    && !dropoffLocationField.getText().isEmpty() && !totalMilesField.getText().isEmpty()
                    && !insuranceField.getText().isEmpty() && aicEmployeeField.getSelectedItem() != null;
        }
    }
}

