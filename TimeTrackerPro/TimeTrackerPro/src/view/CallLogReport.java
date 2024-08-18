package view;

import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import model.AmbulanceCall;
import model.DailyCallLog;

public class CallLogReport {
    private List<DailyCallLog> dailyCallLogs;

    public CallLogReport(List<DailyCallLog> dailyCallLogs) {
        this.dailyCallLogs = dailyCallLogs;
        // Sort the dailyCallLogs by start date in descending order
        this.dailyCallLogs.sort(Comparator.comparing(DailyCallLog::getStartDate).reversed());
    }

    public JPanel generateReportPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Date formatter to display date and weekday
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");

        Font tableFont = new Font("Arial", Font.PLAIN, 16);
        for (DailyCallLog log : dailyCallLogs) {
            // Create the call log table
            String[] logColumnNames = { "Start Date", "End Date", "Truck Unit Number", "Crew Members" };
            DefaultTableModel logTableModel = new DefaultTableModel(logColumnNames, 0);
            logTableModel.addRow(new Object[] { dateFormat.format(log.getStartDate()), dateFormat.format(log.getEndDate()),
                    log.getTruckUnitNumber(), String.join(", ", log.getCrewMembers()) });
            JTable logTable = new JTable(logTableModel);
            setPreferredColumnWidths(logTable, new int[]{150, 150, 150, 200});
            logTable.setFont(tableFont);
            logTable.setRowHeight(20); // Adjust the row height to fit the font size
            logTable.getTableHeader().setFont(tableFont);
            logTable.setPreferredScrollableViewportSize(logTable.getPreferredSize());

            // Create the ambulance call table
            String[] callColumnNames = { "Call Date", "Patient's Name", "Call Category", "Pickup Location",
                    "Dropoff Location", "Total Miles", "Insurance", "AIC Employee" };
            DefaultTableModel callTableModel = new DefaultTableModel(callColumnNames, 0);

            for (AmbulanceCall call : log.getAmbulanceCalls()) {
                callTableModel.addRow(new Object[] { dateFormat.format(call.getCallDate()), call.getPatientsName(),
                        call.getCallCategory(), call.getPickupLocation(), call.getDropoffLocation(),
                        call.getTotalMiles(), call.getInsurance(), call.getAicName() });
            }
            JTable callTable = new JTable(callTableModel);
            setPreferredColumnWidths(callTable, new int[]{150, 150, 150, 200, 200, 100, 150, 150});
            callTable.setFont(tableFont);
            callTable.setRowHeight(20); // Adjust the row height to fit the font size
            callTable.getTableHeader().setFont(tableFont);
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

    private void setPreferredColumnWidths(JTable table, int[] widths) {
        for (int i = 0; i < widths.length; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }
    }
}
