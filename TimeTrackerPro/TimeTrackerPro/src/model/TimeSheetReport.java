package model;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
    }

    public JTable generateReportTable() {
        String[] columnNames = {"Employee Name", "Shift Start Date", "Shift End Date", "Shift Start Time", "Shift End Time", "Hours Worked", "Overtime Comments"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Organize data into 7-day blocks and sort by date
        Map<Integer, List<TimeSheet>> employeeTimeSheets = new HashMap<>();
        for (TimeSheet ts : timeSheets) {
            employeeTimeSheets.computeIfAbsent(ts.getEmployeeId(), k -> new ArrayList<>()).add(ts);
        }

        // Sort time sheets by start date
        for (List<TimeSheet> tsList : employeeTimeSheets.values()) {
            tsList.sort(Comparator.comparing(TimeSheet::getShiftStartDate));
        }

        // Generate report content
        for (Employee emp : employees) {
            List<TimeSheet> empTimeSheets = employeeTimeSheets.get(emp.getId());
            if (empTimeSheets != null) {
                for (TimeSheet ts : empTimeSheets) {
                    Date shiftStartDate = ts.getShiftStartDate();
                    if (shiftStartDate.before(startDate) || shiftStartDate.after(endDate)) {
                        continue;
                    }

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

        return table;
    }

    public JTable generateSummaryTable() {
        String[] columnNames = {"Employee Name", "Week 1 Hours", "Week 2 Hours", "Total Hours"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Organize data into 7-day blocks and sort by date
        Map<Integer, List<TimeSheet>> employeeTimeSheets = new HashMap<>();
        for (TimeSheet ts : timeSheets) {
            employeeTimeSheets.computeIfAbsent(ts.getEmployeeId(), k -> new ArrayList<>()).add(ts);
        }

        // Sort time sheets by start date
        for (List<TimeSheet> tsList : employeeTimeSheets.values()) {
            tsList.sort(Comparator.comparing(TimeSheet::getShiftStartDate));
        }

        // Generate summary content
        for (Employee emp : employees) {
            List<TimeSheet> empTimeSheets = employeeTimeSheets.get(emp.getId());
            if (empTimeSheets != null) {
                long totalWeek1 = 0;
                long totalWeek2 = 0;

                for (TimeSheet ts : empTimeSheets) {
                    Date shiftStartDate = ts.getShiftStartDate();
                    if (shiftStartDate.before(startDate) || shiftStartDate.after(endDate)) {
                        continue;
                    }

                    if (shiftStartDate.before(addDays(startDate, 7))) {
                        totalWeek1 += ts.getHoursWorked();
                    } else {
                        totalWeek2 += ts.getHoursWorked();
                    }
                }

                if (totalWeek1 > 0 || totalWeek2 > 0) {
                    model.addRow(new Object[]{
                        emp.getName(),
                        totalWeek1 / 60 + " hours " + totalWeek1 % 60 + " minutes",
                        totalWeek2 / 60 + " hours " + totalWeek2 % 60 + " minutes",
                        (totalWeek1 + totalWeek2) / 60 + " hours " + (totalWeek1 + totalWeek2) % 60 + " minutes"
                    });
                }
            }
        }

        return table;
    }

    private Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
}
