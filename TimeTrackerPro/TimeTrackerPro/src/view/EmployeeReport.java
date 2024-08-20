package view;

import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import model.Employee;

public class EmployeeReport {
    private List<Employee> employees;

    public EmployeeReport(List<Employee> employees) {
        this.employees = employees;
    }

    public JPanel generateReportPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");

        // Create the employee table
        String[] columnNames = { "Name", "Level", "Certification Number", "Certification Expiration Date" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Employee employee : employees) {
            String certExpDate = (employee.getCertExpDate() != null) ? dateFormat.format(employee.getCertExpDate()) : "N/A";
            tableModel.addRow(new Object[] { employee.getName(), employee.getCertLevel(), employee.getCertificationNumber(), certExpDate });
        }

        JTable table = new JTable(tableModel);
        setPreferredColumnWidths(table, new int[]{150, 150, 200, 200});
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
