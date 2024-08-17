package model;

import java.awt.Dimension;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CallLogReport {
    private List<DailyCallLog> dailyCallLogs;

    public CallLogReport(List<DailyCallLog> dailyCallLogs) {
        this.dailyCallLogs = dailyCallLogs;
    }

    public JPanel generateReportPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        for (DailyCallLog log : dailyCallLogs) {
            // Create the call log table
            String[] logColumnNames = { "Start Date", "End Date", "Truck Unit Number" , "Crew Members"};
            DefaultTableModel logTableModel = new DefaultTableModel(logColumnNames, 0);
            logTableModel.addRow(new Object[]{
                log.getStartDate(),
                log.getEndDate(),
                log.getTruckUnitNumber(),
                String.join(", ", log.getCrewMembers())
            });
            JTable logTable = new JTable(logTableModel);
            logTable.setPreferredScrollableViewportSize(logTable.getPreferredSize());

            // Create the ambulance call table
            String[] callColumnNames = {"Call Date", "Patient's Name", "Call Category", "Pickup Location", "Dropoff Location", "Total Miles", "Insurance", "AIC Employee"};
            DefaultTableModel callTableModel = new DefaultTableModel(callColumnNames, 0);

            for (AmbulanceCall call : log.getAmbulanceCalls()) {
                callTableModel.addRow(new Object[]{
                    call.getCallDate(),
                    call.getPatientsName(),
                    call.getCallCategory(),
                    call.getPickupLocation(),
                    call.getDropoffLocation(),
                    call.getTotalMiles(),
                    call.getInsurance(),
                    call.getAicName()
                });
            }
            JTable callTable = new JTable(callTableModel);
            callTable.setPreferredScrollableViewportSize(callTable.getPreferredSize());

            // Add the tables to the main panel
            mainPanel.add(new JLabel("Call Log"));
            mainPanel.add(new JScrollPane(logTable));
            mainPanel.add(new JLabel("Ambulance Calls"));
            mainPanel.add(new JScrollPane(callTable));
            mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add spacing between logs
        }

        return mainPanel;
    }

}
