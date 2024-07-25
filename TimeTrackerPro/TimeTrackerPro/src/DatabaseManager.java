import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {

	private Connection connection;

	public DatabaseManager(String dbUrl) {
		try {
			connection = DriverManager.getConnection(dbUrl);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void addEmployee(Employee employee) {
		String sql = "INSERT INTO employees(name, level, certificationNumber, certExpirationDate) VALUES(?,?,?,?)";
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, employee.getName());
			pstmt.setString(2, employee.getCertLevel().toString());
			pstmt.setString(3,  employee.getCertificationNumber());
			pstmt.setDate(4,  new Date(employee.getCertExpDate().getTime()));
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void addTimeSheet(TimeSheet timeSheet) {
        String sql = "INSERT INTO timesheets(employeeName, shiftStartDate, shiftEndDate, shiftStartTime, shiftEndTime) VALUES(?,?,?,?,?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, timeSheet.getEmployee().getName());
            pstmt.setDate(2, new java.sql.Date(timeSheet.getShiftStartDate().getTime()));
            pstmt.setDate(3, new java.sql.Date(timeSheet.getShiftEndDate().getTime()));
            pstmt.setTime(4, new java.sql.Time(timeSheet.getShiftStartTime().getTime()));
            pstmt.setTime(5, new java.sql.Time(timeSheet.getShiftEndTime().getTime()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
