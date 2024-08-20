package view;
import javax.swing.*;

import controller.TTController;
import model.DatabaseManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        activeListModel = new DefaultListModel<>();
        inactiveListModel = new DefaultListModel<>();
        activeEmployeeList = new JList<>(activeListModel);
        inactiveEmployeeList = new JList<>(inactiveListModel);

        JPanel listPanel = new JPanel(new GridLayout(1, 2));
        listPanel.add(new JScrollPane(activeEmployeeList));
        listPanel.add(new JScrollPane(inactiveEmployeeList));

        activateButton = new JButton("Activate");
        deactivateButton = new JButton("Deactivate");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(activateButton);
        buttonPanel.add(deactivateButton);

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
