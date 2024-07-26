import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

	private Connection connection;

	public DatabaseManager(String dbUrl) {
		try {
			connection = DriverManager.getConnection(dbUrl);
			createTables();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	 private void createTables() {
	        String sqlCreateEmployeesTable = "CREATE TABLE IF NOT EXISTS employees (\n"
	                + " id integer PRIMARY KEY,\n"
	                + " name text NOT NULL,\n"
	                + " level text,\n"
	                + " certificationNumber text,\n"
	                + " certExpirationDate text\n"
	                + ");";

	        String sqlCreateTimeSheetsTable = "CREATE TABLE IF NOT EXISTS timesheets (\n"
	                + " id integer PRIMARY KEY,\n"
	                + " employeeName text NOT NULL,\n"
	                + " shiftStartDate text,\n"
	                + " shiftEndDate text,\n"
	                + " shiftStartTime text,\n"
	                + " shiftEndTime text\n"
	                + ");";

	        try (Statement stmt = connection.createStatement()) {
	            stmt.execute(sqlCreateEmployeesTable);
	            stmt.execute(sqlCreateTimeSheetsTable);
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	    }

	// add employee to the database
	public void addEmployee(Employee employee) {
		String sql = "INSERT INTO employees(name, level, certificationNumber, certExpirationDate) VALUES(?,?,?,?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, employee.getName());
			pstmt.setString(2, employee.getCertLevel().toString());
			pstmt.setString(3, employee.getCertificationNumber());
			pstmt.setDate(4, new Date(employee.getCertExpDate().getTime()));
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	// add a time sheet entry to the database
	public void addTimeSheet(TimeSheet timeSheet) {
		String sql = "INSERT INTO timesheets(employeeName, shiftStartDate, shiftEndDate, shiftStartTime, shiftEndTime) VALUES(?,?,?,?,?)";

		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, timeSheet.getEmployeeName());
			pstmt.setDate(2, new java.sql.Date(timeSheet.getShiftStartDate().getTime()));
			pstmt.setDate(3, new java.sql.Date(timeSheet.getShiftEndDate().getTime()));
			pstmt.setTime(4, new java.sql.Time(timeSheet.getShiftStartTime().getTime()));
			pstmt.setTime(5, new java.sql.Time(timeSheet.getShiftEndTime().getTime()));
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	public List<TimeSheet> getRecentTimeSheetsForEmployee(int id) {
		List<TimeSheet> timeSheets = new ArrayList<TimeSheet>();

		PreparedStatement pstmt = null;
		try {
			String sql = "SELECT * FROM timesheets WHERE employeeId = ? AND shiftStartDate >= datetime('now', '-1 month')";

			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				// loop to create time sheet objects
				TimeSheet timeSheet = new TimeSheet(rs.getInt("id"), rs.getString("employeeName"),
						rs.getDate("shiftStartDate"), rs.getDate("shiftEndDate"), rs.getDate("shiftStartTime"),
						rs.getDate("shiftEndTime"));
				// add to the list thats going to be returned
				timeSheets.add(timeSheet);
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return timeSheets;
	}

}
