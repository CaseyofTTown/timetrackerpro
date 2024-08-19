package view;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Employee;
import model.TimeSheet;

public class TimeSheetReport {
    private List<TimeSheet> timeSheets;
    private List<Employee> employees;
    private Date startDate;
    private Date endDate;

    public TimeSheetReport(List<TimeSheet> timeSheets, List<Employee> employees, Date startDate, Date endDate) {
        this.timeSheets = timeSheets;
        this.employees = employees;
        this.startDate = startDate;
        this.endDate = endDate;
        
        this.timeSheets.sort(Comparator.comparing(TimeSheet::getShiftStartDate).reversed());
    }

    public JPanel generateReportPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        String title = "Time Sheet Report (" + new SimpleDateFormat("EEE, MMM dd, yyyy").format(startDate) + " - " + new SimpleDateFormat("EEE, MMM dd, yyyy").format(endDate) + ")";
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JTable table = generateReportTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

       return panel;
    }

    private JTable generateReportTable() {
        String[] columnNames = {"Employee Name", "Shift Start Date", "Shift End Date", "Shift Start Time", "Shift End Time", "Hours Worked", "Overtime Comments"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        
        // Set preferred column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(150); // Employee Name
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Shift Start Date
        table.getColumnModel().getColumn(2).setPreferredWidth(200); // Shift End Date
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Shift Start Time
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Shift End Time
        table.getColumnModel().getColumn(5).setPreferredWidth(200); // Hours Worked
        table.getColumnModel().getColumn(6).setPreferredWidth(200); // Overtime Comments

        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(true);
        
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd, yyyy");

        // Organize data into 7-day blocks and sort by date
        Map<Integer, List<TimeSheet>> employeeTimeSheets = new HashMap<>();
        for (TimeSheet ts : timeSheets) {
            employeeTimeSheets.computeIfAbsent(ts.getEmployeeId(), k -> new ArrayList<>()).add(ts);
        }

        // Sort time sheets by start date
        for (List<TimeSheet> tsList : employeeTimeSheets.values()) {
            tsList.sort(Comparator.comparing(TimeSheet::getShiftStartDate));
        }

        String currentEmployee = null;
        // Generate report content
        for (Employee emp : employees) {
            List<TimeSheet> empTimeSheets = employeeTimeSheets.get(emp.getId());
            if (empTimeSheets != null) {
                for (TimeSheet ts : empTimeSheets) {
                    Date shiftStartDate = ts.getShiftStartDate();
                    if (shiftStartDate.before(startDate) || shiftStartDate.after(endDate)) {
                        continue;
                    }
                    
                    if (currentEmployee != null && !currentEmployee.equals(emp.getName())) {
                        model.addRow(new Object[]{"", "", "", "", "", "", ""}); // Add empty row for separation
                    }
                    currentEmployee = emp.getName();

                    model.addRow(new Object[]{
                        emp.getName(),
                        sdf.format(ts.getShiftStartDate()),
                        sdf.format(ts.getShiftEndDate()),
                        ts.getShiftStartTime(),
                        ts.getShiftEndTime(),
                        ts.getFormattedHoursWorked(),
                        ts.getOvertimeComment()
                    });
                }
            }
        }
        
        generateSummaryTable(model);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());

        return table;
    }

    private void generateSummaryTable(DefaultTableModel model) {
        Map<Integer, Long> employeeHours = new HashMap<>();
        for (TimeSheet ts : timeSheets) {
            if (ts.getShiftStartDate().before(startDate) || ts.getShiftStartDate().after(endDate)) {
                continue;
            }
            long hoursWorked = ts.calculateTimeWorked();
            employeeHours.put(ts.getEmployeeId(), employeeHours.getOrDefault(ts.getEmployeeId(), 0L) + hoursWorked);
        }

        model.addRow(new Object[]{"", "", "", "", "", "", ""}); // Add empty row for separation
        model.addRow(new Object[]{"Employee", "Total Hours", "", "", "", "", ""}); // Add summary header

        for (Employee emp : employees) {
            Long totalMinutes = employeeHours.get(emp.getId());
            if (totalMinutes != null) {
                long hours = totalMinutes / 60;
                long minutes = totalMinutes % 60;
                String totalHoursWorked = String.format("%d hours %d minutes", hours, minutes);
                model.addRow(new Object[]{emp.getName(), totalHoursWorked, "", "", "", "", ""});
            }
        }
    }

    private Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
}
