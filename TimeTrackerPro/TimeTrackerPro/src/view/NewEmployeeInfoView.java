package view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import org.jdatepicker.impl.JDatePickerImpl;
import model.CertificationLevelenum;

public class NewEmployeeInfoView extends JFrame {
	private JTextField nameField;
	private JComboBox<String> emsCertifiedComboBox;
	private JComboBox<CertificationLevelenum> certificationLevelComboBox;
	private JTextField certificationNumberField;
	private JDatePickerImpl expirationDateField;
	private JButton submitNewEmployeeButton;

	public NewEmployeeInfoView() {

		setTitle("New Employee Information");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(new Color(50, 50, 50));
		add(panel);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;

		panel.add(new JLabel("Full Name:"), c);
		c.gridy++;
		nameField = new JTextField("Enter first and last name", 20);
		nameField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				nameField.setText("");
			}
		});
		panel.add(nameField, c);
		c.gridy++;

		// selector used to controll collection of cert exp date
		panel.add(new JLabel("Are you a certified EMS provider?"), c);
		c.gridy++;
		emsCertifiedComboBox = new JComboBox<>(new String[] { "Please select", "Yes", "No" });
		emsCertifiedComboBox.addActionListener(e -> {
			boolean isCertified = emsCertifiedComboBox.getSelectedItem().equals("Yes");
			boolean isAnswered = !emsCertifiedComboBox.getSelectedItem().equals("Please select");
			certificationLevelComboBox.setEnabled(isCertified);
			certificationNumberField.setEnabled(isCertified);
			expirationDateField.setEnabled(isCertified);
			submitNewEmployeeButton.setEnabled(isAnswered); //forces a selection to continue
			checkFields();
		});
		panel.add(emsCertifiedComboBox, c);
		c.gridy++;

		panel.add(new JLabel("Certification Level:"), c);
		c.gridy++;
		certificationLevelComboBox = new JComboBox<>(CertificationLevelenum.values());
		certificationLevelComboBox.setEnabled(false);
		panel.add(certificationLevelComboBox, c);
		c.gridy++;

		panel.add(new JLabel("Certification Number:"), c);
		c.gridy++;
		certificationNumberField = new JTextField(20);
		certificationNumberField.setEnabled(false);
		panel.add(certificationNumberField, c);
		c.gridy++;

		panel.add(new JLabel("Expiration Date:"), c);
		c.gridy++;
		DatePicker datePicker = new DatePicker();
		expirationDateField = datePicker.getDatePicker();
		expirationDateField.setEnabled(false);
		panel.add(expirationDateField, c);
		c.gridy++;

		submitNewEmployeeButton = new JButton("Submit");
		submitNewEmployeeButton.setEnabled(false);
		panel.add(submitNewEmployeeButton, c);

		DocumentListener documentListener = new DocumentListener() {
			public void changedUpdate(DocumentEvent documentEvent) {
				checkFields();
			}

			public void insertUpdate(DocumentEvent documentEvent) {
				checkFields();
			}

			public void removeUpdate(DocumentEvent documentEvent) {
				checkFields();
			}
		};

		nameField.getDocument().addDocumentListener(documentListener);
		certificationNumberField.getDocument().addDocumentListener(documentListener);
		emsCertifiedComboBox.addItemListener(itemEvent -> checkFields());
		certificationLevelComboBox.addItemListener(itemEvent -> checkFields());

	}

	private void checkFields() {
	    boolean isCertified = emsCertifiedComboBox.getSelectedItem().equals("Yes");
	    boolean isAnswered = !emsCertifiedComboBox.getSelectedItem().equals("Please select");

	    if (isAnswered && (nameField.getText().trim().isEmpty() || (isCertified && (certificationNumberField.getText().trim().isEmpty()
	            || certificationLevelComboBox.getSelectedItem() == null
	            || expirationDateField.getJFormattedTextField().getText().trim().isEmpty())))) {
	        submitNewEmployeeButton.setEnabled(false);
	    } else {
	        submitNewEmployeeButton.setEnabled(true);
	    }
	}
	//TODO: getters for the data so view and controller can use it
}
