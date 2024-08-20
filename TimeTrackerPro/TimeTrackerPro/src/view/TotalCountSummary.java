package view;

import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import model.TypeOfCallEnum;
import model.AmbulanceCall;

public class TotalCountSummary {
    private List<AmbulanceCall> calls;
    private java.sql.Date startDate;
    private java.sql.Date endDate;

    public TotalCountSummary(List<AmbulanceCall> calls, java.sql.Date startDate, java.sql.Date endDate) {
        this.calls = calls;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public JPanel generateReportPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        // Format the dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String formattedStartDate = dateFormat.format(new java.util.Date(startDate.getTime()));
        String formattedEndDate = dateFormat.format(new java.util.Date(endDate.getTime()));

        // Add the title label with the date range
        String title = "Total Count Date Range: (" + formattedStartDate + " - " + formattedEndDate + ")";
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel);

        // Count the occurrences of each type of call
        Map<TypeOfCallEnum, Integer> callCounts = new EnumMap<>(TypeOfCallEnum.class);
        for (TypeOfCallEnum type : TypeOfCallEnum.values()) {
            callCounts.put(type, 0);
        }
        int skilledCallCount = 0;
        for (AmbulanceCall call : calls) {
            TypeOfCallEnum type = call.getCallCategory();
            callCounts.put(type, callCounts.get(type) + 1);
            if (call.isSkilled()) {
                skilledCallCount++;
            }
        }

        // Create the table
        String[] columnNames = { "Type of Call", "Total Count" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (TypeOfCallEnum type : TypeOfCallEnum.values()) {
            tableModel.addRow(new Object[] { type.name().replace('_', ' '), callCounts.get(type) });
        }
        // Add the total skilled calls row
        tableModel.addRow(new Object[] { "Skilled Calls", skilledCallCount });

        JTable table = new JTable(tableModel);
        setPreferredColumnWidths(table, new int[]{200, 100});
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(20);
        table.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 16));
        table.setPreferredScrollableViewportSize(table.getPreferredSize());

        mainPanel.add(new JScrollPane(table));
        return mainPanel;
    }

    private void setPreferredColumnWidths(JTable table, int[] widths) {
        for (int i = 0; i < widths.length; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }
    }
}
