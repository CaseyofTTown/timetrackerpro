package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import model.ColorConstants;

public class CallLogEntryPanel extends JPanel {
    private JTextField truckUnitNumberField;
    private JDatePickerImpl startDatePicker;
    private JDatePickerImpl endDatePicker;
    private JComboBox<String> crewMemberComboBox;
    private JButton addCrewMemberButton;
    private JButton removeCrewMemberButton;
    private DefaultListModel<String> crewMemberListModel;
    private JList<String> crewMemberList;
    private JButton submitButton;
    private JButton cancelButton;
    private JLabel warningLabel;

    private List<String> crewMembers;

    public CallLogEntryPanel(List<String> crewMembers) {
        if (crewMembers == null) {
            crewMembers = new ArrayList<>();
        }
        this.crewMembers = crewMembers;

        setLayout(new GridBagLayout());
        setBackground(ColorConstants.CHARCOAL);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5); // spacing of columns

        // Truck Unit Number Field
        truckUnitNumberField = new JTextField(20);
        truckUnitNumberField.setBackground(ColorConstants.DARK_GRAY);
        truckUnitNumberField.setForeground(ColorConstants.GOLD);
        addComponent("Truck Unit Number", truckUnitNumberField, c, 0, 0);

        // Start Date Picker
        DatePicker startDatePickerComponent = new DatePicker();
        startDatePicker = startDatePickerComponent.getDatePicker();
        startDatePicker.getJFormattedTextField().setBackground(ColorConstants.DARK_GRAY);
        startDatePicker.getJFormattedTextField().setForeground(ColorConstants.GOLD);
        addComponent("Start Date", startDatePicker, c, 0, 1);

        // End Date Picker
        DatePicker endDatePickerComponent = new DatePicker();
        endDatePicker = endDatePickerComponent.getDatePicker();
        endDatePicker.getJFormattedTextField().setBackground(ColorConstants.DARK_GRAY);
        endDatePicker.getJFormattedTextField().setForeground(ColorConstants.GOLD);
        addComponent("End Date", endDatePicker, c, 0, 2);


        // Crew Member Dropdown
        crewMemberComboBox = new JComboBox<>(crewMembers.toArray(new String[0]));
        crewMemberComboBox.setBackground(ColorConstants.CHARCOAL);
        crewMemberComboBox.setForeground(ColorConstants.GOLD);
        addComponent("Crew Member", crewMemberComboBox, c, 0, 3);

        // Add Crew Member Button
        addCrewMemberButton = new JButton("Add Crew Member");
        addCrewMemberButton.setBackground(ColorConstants.DEEP_BLUE);
        addCrewMemberButton.setForeground(ColorConstants.LIME_GREEN);
        addCrewMemberButton.addActionListener(e -> addCrewMember());
        addComponent("", addCrewMemberButton, c, 1, 3);

        // Remove Crew Member Button
        removeCrewMemberButton = new JButton("Remove Crew Member");
        removeCrewMemberButton.setBackground(ColorConstants.DEEP_BLUE);
        removeCrewMemberButton.setForeground(ColorConstants.LIME_GREEN);
        removeCrewMemberButton.addActionListener(e -> removeCrewMember());
        addComponent("", removeCrewMemberButton, c, 2, 3);

        // Crew Member List
        crewMemberListModel = new DefaultListModel<>();
        crewMemberList = new JList<>(crewMemberListModel);
        crewMemberList.setBackground(ColorConstants.DARK_GRAY);
        crewMemberList.setForeground(ColorConstants.GOLD);
        addComponent("Crew Members", new JScrollPane(crewMemberList), c, 0, 4);

        // Warning Label
        warningLabel = new JLabel("Invalid entries detected. Please check all fields.");
        warningLabel.setForeground(ColorConstants.CRIMSON_RED);
        warningLabel.setVisible(false);
        addComponent("", warningLabel, c, 0, 5);

        // Submit Button
        submitButton = new JButton("Submit");
        submitButton.setBackground(ColorConstants.DEEP_BLUE);
        submitButton.setForeground(ColorConstants.LIME_GREEN);
        submitButton.setEnabled(false);
        addComponent("", submitButton, c, 0, 6);

        // Cancel Button
        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(ColorConstants.CRIMSON_RED);
        cancelButton.setForeground(ColorConstants.WHITE);
        addComponent("", cancelButton, c, 1, 6);

        System.out.println("CallLogEntryPanel created");
    }

    private void addComponent(String label, JComponent component, GridBagConstraints c, int row, int col) {
        c.gridx = col;
        c.gridy = row;
        if (!label.isEmpty()) {
            JLabel jLabel = new JLabel(label);
            jLabel.setForeground(ColorConstants.GOLD);
            add(jLabel, c);
        }
        c.gridy++;
        add(component, c);
    }

    private void addCrewMember() {
        String crewMember = (String) crewMemberComboBox.getSelectedItem();
        if (crewMember != null && !crewMemberListModel.contains(crewMember)) {
            crewMemberListModel.addElement(crewMember);
        }
    }

    private void removeCrewMember() {
        String selectedCrewMember = crewMemberList.getSelectedValue();
        if (selectedCrewMember != null) {
            crewMemberListModel.removeElement(selectedCrewMember);
        }
    }

    public JButton getSubmitButton() {
        return this.submitButton;
    }

    public JButton getCancelButton() {
        return this.cancelButton;
    }

    public JTextField getTruckUnitNumberField() {
        return this.truckUnitNumberField;
    }

    public JDatePickerImpl getStartDatePicker() {
        return this.startDatePicker;
    }

    public JDatePickerImpl getEndDatePicker() {
        return this.endDatePicker;
    }

    public JList<String> getCrewMemberList() {
        return this.crewMemberList;
    }

    public void setCrewMemberList(List<String> crewMembers) {
        this.crewMembers = crewMembers;
        crewMemberComboBox.setModel(new DefaultComboBoxModel<>(crewMembers.toArray(new String[0])));
        revalidate();
        repaint();
    }

    // Getters to provide data up the chain back to controller
    public String getTruckUnitNumber() {
        return truckUnitNumberField.getText();
    }

    public Date getStartDate() {
        return (Date) startDatePicker.getModel().getValue();
    }

    public Date getEndDate() {
        return (Date) endDatePicker.getModel().getValue();
    }

    public List<String> getCrewMembers() {
        List<String> crewMembers = new ArrayList<>();
        for (int i = 0; i < crewMemberListModel.getSize(); i++) {
            crewMembers.add(crewMemberListModel.getElementAt(i));
        }
        return crewMembers;
    }

    // Setters for modifying a call log
    public void setTruckUnitNumber(String truckUnitNumber) {
        truckUnitNumberField.setText(truckUnitNumber);
    }

    public void setStartDate(Date startDate) {
        UtilDateModel model = (UtilDateModel) startDatePicker.getModel();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        model.setValue(calendar.getTime());
        model.setSelected(true);
    }

    public void setEndDate(Date endDate) {
        UtilDateModel model = (UtilDateModel) endDatePicker.getModel();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        model.setValue(calendar.getTime());
        model.setSelected(true);
    }

    public void setCrewMembers(List<String> crewMembers) {
        crewMemberListModel.clear();
        for (String crewMember : crewMembers) {
            crewMemberListModel.addElement(crewMember);
        }
    }



    public void resetFields() {
        truckUnitNumberField.setText("");
        startDatePicker.getModel().setValue(null);
        endDatePicker.getModel().setValue(null);
        crewMemberListModel.clear();
    }
}
