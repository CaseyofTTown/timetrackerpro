import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		String sqlCreateEmployeesTable = "CREATE TABLE IF NOT EXISTS employees (\n" + " id integer PRIMARY KEY AUTOINCREMENT,\n"
				+ " name text NOT NULL,\n" + " level text,\n" + " certificationNumber text,\n"
				+ " certExpirationDate text\n" + ");";

		String sqlCreateTimeSheetsTable = "CREATE TABLE IF NOT EXISTS timesheets (\n" + " id integer PRIMARY KEY AUTOINCREMENT,\n"
				+ " employeeId integer NOT NULL,\n" + " shiftStartDate text,\n" + " shiftEndDate text,\n"
				+ " shiftStartTime text,\n" + " shiftEndTime text,\n"
				+ " FOREIGN KEY(employeeId) REFERENCES employees(id)\n" + ");";

		try (Statement stmt = connection.createStatement()) {
			stmt.execute(sqlCreateEmployeesTable);
			stmt.execute(sqlCreateTimeSheetsTable);
			System.out.println("Succesfully created database tables");
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
		String sql = "INSERT INTO timesheets(employeeId, shiftStartDate, shiftEndDate, shiftStartTime, shiftEndTime) VALUES(?,?,?,?,?)";

		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, timeSheet.getEmployeeId());
			// rest of your code...
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
	        String sql = "SELECT timesheets.id, employees.name, shiftStartDate, shiftEndDate, shiftStartTime, shiftEndTime "
	                   + "FROM timesheets "
	                   + "JOIN employees ON timesheets.employeeId = employees.id "
	                   + "WHERE employeeId = ? AND shiftStartDate >= datetime('now', '-1 month')";

	        pstmt = connection.prepareStatement(sql);
	        pstmt.setInt(1, id);
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            TimeSheet timeSheet = new TimeSheet(rs.getInt("id"), id, rs.getString("name"),
	                    rs.getDate("shiftStartDate"), rs.getDate("shiftEndDate"), rs.getDate("shiftStartTime"),
	                    rs.getDate("shiftEndTime"));
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


	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
