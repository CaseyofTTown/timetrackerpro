package view;
import javax.swing.*;

import controller.TTController;
import model.ColorConstants;
import model.DatabaseManager;
import model.KeyBindingUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;

public class EmployeeStatusManager extends JPanel {
    private JList<String> activeEmployeeList;
    private JList<String> inactiveEmployeeList;
    private DefaultListModel<String> activeListModel;
    private DefaultListModel<String> inactiveListModel;
    private JButton activateButton;
    private JButton deactivateButton;
    private TTController controller;

    public EmployeeStatusManager(TTController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(ColorConstants.CHARCOAL);
        

        activeListModel = new DefaultListModel<>();
        inactiveListModel = new DefaultListModel<>();
        activeEmployeeList = new JList<>(activeListModel);
        inactiveEmployeeList = new JList<>(inactiveListModel);
        
        Font customFont = new Font("Arial", Font.PLAIN, 24);
        activeEmployeeList.setFont(customFont);
        activeEmployeeList.setBackground(ColorConstants.CHARCOAL);
        inactiveEmployeeList.setFont(customFont);
        inactiveEmployeeList.setBackground(ColorConstants.CHARCOAL);
        
        activeEmployeeList.setForeground(ColorConstants.GOLD);
        inactiveEmployeeList.setForeground(ColorConstants.CRIMSON_RED); 

        JPanel listPanel = new JPanel(new GridLayout(1, 2));
        listPanel.add(new JScrollPane(activeEmployeeList));
        listPanel.add(new JScrollPane(inactiveEmployeeList));
        
        listPanel.setBackground(ColorConstants.CHARCOAL);
        

        activateButton = new JButton("Activate");
        activateButton.setBackground(ColorConstants.SLATE_GRAY);
        activateButton.setForeground(ColorConstants.LIME_GREEN);
        
        deactivateButton = new JButton("Deactivate");
        deactivateButton.setBackground(ColorConstants.SLATE_GRAY);
        deactivateButton.setForeground(ColorConstants.CRIMSON_RED);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(activateButton);
        buttonPanel.add(deactivateButton);
        buttonPanel.setBackground(ColorConstants.SLATE_GRAY);

        add(listPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        activateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedEmployee = inactiveEmployeeList.getSelectedValue();
                if (selectedEmployee != null) {
                    controller.setEmployeeStatus(selectedEmployee, true);
                    refreshEmployeeLists();
                }
            }
        });

        deactivateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedEmployee = activeEmployeeList.getSelectedValue();
                if (selectedEmployee != null) {
                    controller.setEmployeeStatus(selectedEmployee, false);
                    refreshEmployeeLists();
                }
            }
        });

        refreshEmployeeLists();
        setKeyBindings();
    }

    private void setKeyBindings() {
        KeyBindingUtil.addKeyBinding(this, KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK), "activate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activateButton.doClick();
            }
        });

        KeyBindingUtil.addKeyBinding(this, KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK), "deactivate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deactivateButton.doClick();
            }
        });
    }

	private void refreshEmployeeLists() {
        activeListModel.clear();
        inactiveListModel.clear();
        Map<String, List<String>> employeeNamesByStatus = controller.getEmployeeNamesByStatus();
        for (String name : employeeNamesByStatus.get("active")) {
            activeListModel.addElement(name);
        }
        for (String name : employeeNamesByStatus.get("inactive")) {
            inactiveListModel.addElement(name);
        }
    }
}
