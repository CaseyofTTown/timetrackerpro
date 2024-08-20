package view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Calendar;
import java.util.Date;

import org.jdatepicker.impl.JDatePickerImpl;

import controller.TTController;
import model.CertificationLevelenum;
import model.ColorConstants;
import model.Employee;
import model.KeyBindingUtil;

public class NewEmployeeInfoView extends JFrame {
	private TitledTextField nameField;
	private JComboBox<String> emsCertifiedComboBox;
	private JComboBox<CertificationLevelenum> certificationLevelComboBox;
	private TitledTextField certificationNumberField;
	private JDatePickerImpl expirationDateField;
	private JButton submitNewEmployeeButton;
	private boolean isUpdating = false;
	private TTController controller;
	private JButton updateEmployeeInfoButton;
	private Employee employee;

	public NewEmployeeInfoView(boolean isUpdating) {
		this.isUpdating = isUpdating;
	
		setTitle("New Employee Information");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(ColorConstants.CHARCOAL);
		add(panel);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5); // padding

		JLabel topLabel = new JLabel("Additional Information", SwingConstants.CENTER);
		topLabel.setForeground(ColorConstants.GOLD);
		panel.add(topLabel, c);
		c.gridy++;

		nameField = new TitledTextField("Full Name", "First and Last", 20);
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

			certificationLevelComboBox.setEnabled(isCertified);
			certificationNumberField.setEnabled(isCertified);
			expirationDateField.setEnabled(isCertified);
			// submitNewEmployeeButton.setEnabled(isAnswered); // forces a selection to
			// continue
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

		certificationNumberField = new TitledTextField("Certification Number", "Include letter", 20);
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

		updateEmployeeInfoButton = new JButton("Update");
		updateEmployeeInfoButton.setBackground(ColorConstants.SLATE_GRAY);
		updateEmployeeInfoButton.setForeground(ColorConstants.ORANGE);
		updateEmployeeInfoButton.setEnabled(false);

		if (isUpdating) {
			panel.add(updateEmployeeInfoButton, c);
			updateEmployeeInfoButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (controller != null) {
						Employee newEmployee = buildEmployeeObjectFromFields();
						controller.updateEmployeeInfo(newEmployee);
						System.out.println("updateEmplyoeeInfo called");
					} else {
						System.out.println("controller was null on NewEmployeePage");
					}
				}
			});

		} else {

			panel.add(submitNewEmployeeButton, c);
		}

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

		System.out.println("NewEmployeeInfoView created");
		setupKeyBindings();
		revalidate();
		repaint();
	}

	private void setupKeyBindings() {
		if (!isUpdating) {
			KeyBindingUtil.addSubmitAndCancelBindings(getRootPane(), submitNewEmployeeButton, null);

		} else {
			KeyBindingUtil.addSubmitAndCancelBindings(getRootPane(), updateEmployeeInfoButton, null);
		}
	}

	private void checkFields() {
		boolean isCertified = emsCertifiedComboBox.getSelectedItem().equals("Yes");
		boolean isAnswered = !emsCertifiedComboBox.getSelectedItem().equals("Please select");

		boolean nameFilled = !nameField.getText().trim().isEmpty();
		boolean certificationNumberFilled = !certificationNumberField.getText().trim().isEmpty();
		boolean certificationLevelSelected = certificationLevelComboBox.getSelectedItem() != null;
		boolean expirationDateFilled = !expirationDateField.getJFormattedTextField().getText().trim().isEmpty();

		// must have name, certSelection, if not certified, otherwise must enter all
		// fields
		boolean allFieldsFilled = isAnswered && nameFilled
				&& (!isCertified || (certificationNumberFilled && certificationLevelSelected && expirationDateFilled));

		if (allFieldsFilled) {
			System.out.println("passed field validation");
			submitNewEmployeeButton.setEnabled(true);
			submitNewEmployeeButton.setBackground(ColorConstants.DEEP_BLUE);
			submitNewEmployeeButton.setForeground(ColorConstants.LIME_GREEN);
			
			updateEmployeeInfoButton.setEnabled(true);
		} else {
			submitNewEmployeeButton.setEnabled(false);
			updateEmployeeInfoButton.setEnabled(false);
		}
	}

	// getters for the data so view and controller can use it

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

	public void addSubmitEmployeeInfoButtonListener(ActionListener listenForSubmitButton) {
		System.out.println("listener added to submit employee button");
		submitNewEmployeeButton.addActionListener(listenForSubmitButton);
	}

	private Employee buildEmployeeObjectFromFields() {
		String name = getName();
		String isEmsCertified = getEmsCertified();
		CertificationLevelenum certificationLevel;
		String certificationNumber = null;
		Date expirationDate = null;

		if (isEmsCertified.equals("Yes")) {
			certificationLevel = getCertificationLevel();
			certificationNumber = getCertificationNumber();
			expirationDate = getExpirationDate();
		} else {
			certificationLevel = CertificationLevelenum.DRIVER;
		}

		boolean isActive = true;
		try {
			employee = new Employee(employee.getId(), name, certificationLevel, certificationNumber, expirationDate, isActive);
		} catch (Exception e) {
			System.out.println("Unable to create employee object: " + e.getMessage());
		}

		return employee;
	}

	// called by homeview to update employee info
	public void setEmployeeInfoFromExisting(Employee employee, TTController controller) {
		this.controller = controller; // set controller instance for class
		this.nameField.setText(employee.getName());
		this.employee = employee; // storing employee object to extract id before passing back to controller as a
									// whole object
		if (employee.getCertLevel() == CertificationLevelenum.DRIVER) {
			this.emsCertifiedComboBox.setSelectedIndex(2);
			this.certificationLevelComboBox.setSelectedItem(CertificationLevelenum.DRIVER);
		} else {
			this.emsCertifiedComboBox.setSelectedIndex(1);
			this.certificationLevelComboBox.setSelectedItem(employee.getCertLevel());
			this.certificationNumberField.setText(employee.getCertificationNumber());
			// Set the date picker with the employee's expiration date
			Date expirationDate = employee.getCertExpDate();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(expirationDate);
			this.expirationDateField.getModel().setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			this.expirationDateField.getModel().setSelected(true);
		}
		revalidate();
		repaint();

	}

}
