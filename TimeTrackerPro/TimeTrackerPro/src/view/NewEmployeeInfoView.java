package view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Date;

import org.jdatepicker.impl.JDatePickerImpl;
import model.CertificationLevelenum;
import model.ColorConstants;

public class NewEmployeeInfoView extends JFrame {
	private TitledTextField nameField;
	private JComboBox<String> emsCertifiedComboBox;
	private JComboBox<CertificationLevelenum> certificationLevelComboBox;
	private TitledTextField certificationNumberField;
	private JDatePickerImpl expirationDateField;
	private JButton submitNewEmployeeButton;

	public NewEmployeeInfoView() {

		setTitle("New Employee Information");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(ColorConstants.CHARCOAL);
		add(panel);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5,5,5,5); //padding
		
		JLabel topLabel = new JLabel("Additional Information", SwingConstants.CENTER);
		topLabel.setForeground(ColorConstants.GOLD);
		panel.add(topLabel, c);
		c.gridy++;

		nameField = new TitledTextField("Full Name","First and Last", 20);
		nameField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				nameField.setText("");
			}
		});
		panel.add(nameField, c);
		c.gridy++;

		JLabel emsProviderLabel = new JLabel("Are you a certified EMS provider?");
		emsProviderLabel.setForeground(ColorConstants.GOLD);
		panel.add(emsProviderLabel, c);
		c.gridy++;

		
		
		emsCertifiedComboBox = new JComboBox<>(new String[] { "Please select", "Yes", "No" });
		emsCertifiedComboBox.setBackground(ColorConstants.CHARCOAL);
		emsCertifiedComboBox.setForeground(ColorConstants.GOLD);
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

		JLabel certificationLevelLabel = new JLabel("Certification Level:");
		certificationLevelLabel.setForeground(ColorConstants.GOLD);
		panel.add(certificationLevelLabel, c);
		c.gridy++;

		certificationLevelComboBox = new JComboBox<>(CertificationLevelenum.values());
		certificationLevelComboBox.setBackground(ColorConstants.CHARCOAL); // Set background color
		certificationLevelComboBox.setForeground(ColorConstants.GOLD); // Set text color
		certificationLevelComboBox.setEnabled(false);
		panel.add(certificationLevelComboBox, c);
		c.gridy++;


		certificationNumberField = new TitledTextField("Certification Number", "Include letter",20);
		certificationNumberField.setEnabled(false);
		panel.add(certificationNumberField, c);
		c.gridy++;

		JLabel expirationDateLabel = new JLabel("Certification Expiration Date:");
		expirationDateLabel.setForeground(ColorConstants.GOLD);
		panel.add(expirationDateLabel, c);
		c.gridy++;

		DatePicker datePicker = new DatePicker();
		expirationDateField = datePicker.getDatePicker();
		expirationDateField.getJFormattedTextField().setBackground(ColorConstants.DARK_GRAY); // Set background color
		expirationDateField.getJFormattedTextField().setForeground(ColorConstants.GOLD); // Set text color
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

		nameField.getTextField().getDocument().addDocumentListener(documentListener);
		certificationNumberField.getTextField().getDocument().addDocumentListener(documentListener);
		emsCertifiedComboBox.addItemListener(itemEvent -> checkFields());
		certificationLevelComboBox.addItemListener(itemEvent -> checkFields());
		expirationDateField.getJFormattedTextField().getDocument().addDocumentListener(documentListener);
		
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
	        submitNewEmployeeButton.setBackground(ColorConstants.DEEP_BLUE);
	        submitNewEmployeeButton.setForeground(ColorConstants.LIME_GREEN);
	    }
	}
	//getters for the data so view and controller can use it
	
	public String getName() {
		return this.nameField.getText();
	}
	
	public String getEmsCertified() {
		return (String) this.emsCertifiedComboBox.getSelectedItem();
	}
	
	public CertificationLevelenum getCertificationLevel() {
		return (CertificationLevelenum) this.certificationLevelComboBox.getSelectedItem();
	}
	
	public String getCertificationNumber() {
		return this.certificationNumberField.getText();
	}
	
	public Date getExpirationDate() {
		try {
			return (Date) this.expirationDateField.getModel().getValue();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public JButton getSubmitNewEmployeeButton() {
		return submitNewEmployeeButton;
	}

	
}
