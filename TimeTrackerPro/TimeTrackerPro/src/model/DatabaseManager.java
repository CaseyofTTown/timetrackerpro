package model;

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
		String sqlCreateEmployeesTable = "CREATE TABLE IF NOT EXISTS employees (\n"
	            + " id integer PRIMARY KEY,\n" + " name text NOT NULL,\n" + " level text,\n"
	            + " certificationNumber text,\n" + " certExpirationDate text\n" + ");";

		String sqlCreateTimeSheetsTable = "CREATE TABLE IF NOT EXISTS timesheets (\n"
				+ " id integer PRIMARY KEY AUTOINCREMENT,\n" + " employeeId integer NOT NULL,\n"
				+ " shiftStartDate text,\n" + " shiftEndDate text,\n" + " shiftStartTime text,\n"
				+ " shiftEndTime text,\n" + " FOREIGN KEY(employeeId) REFERENCES employees(id)\n" + ");";

		String sqlCreateUsersTable = "CREATE TABLE IF NOT EXISTS users (\n " + " username text PRIMARY KEY, \n"
				+ " hashedPassword text NOT NULL, \n" + " salt text NOT NULL, \n" + " employeeId integer, \n"
				+ "FOREIGN KEY(employeeID) REFERENCES employees(id)\n" + ")";

		try (Statement stmt = connection.createStatement()) {
			stmt.execute(sqlCreateEmployeesTable);
			stmt.execute(sqlCreateTimeSheetsTable);
			stmt.execute(sqlCreateUsersTable);
			System.out.println("Succesfully created database tables");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// add employee to the database
	public boolean addEmployee(Employee employee) {
		String sql = "INSERT INTO employees(id, name, level, certificationNumber, certExpirationDate) VALUES(?,?,?,?,?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, employee.getId());
			pstmt.setString(2, employee.getName());
			pstmt.setString(3, employee.getCertLevel().toString());
			pstmt.setString(4, employee.getCertificationNumber());
			pstmt.setDate(5, new Date(employee.getCertExpDate().getTime()));
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
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
					+ "FROM timesheets " + "JOIN employees ON timesheets.employeeId = employees.id "
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

	public boolean registerUser(String username, String hashedPassword, String salt, int pin) {
		String sql = "INSERT INTO users(username, hashedPassword, salt, employeeId, pin) VALUES(?,?,?,?,?)";
		int result = 0; //number of rows affected to verify success
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, username);
			pstmt.setString(2, hashedPassword);
			pstmt.setString(3, salt);
			int employeeId = generateEmployeeId();
			pstmt.setInt(4, employeeId);
			pstmt.setInt(5, pin);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return result > 0; //true if successful

	}
	//find highest employee id in database or start with 001
	private int generateEmployeeId() {
		String sql = "SELECT MAX(employeeId) AS max_id FROM users";
		
		try(PreparedStatement pstmt = connection.prepareStatement(sql)){
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				int maxId = rs.getInt("max_id");
				return maxId > 0 ? maxId + 1 : 1;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		//if error occurs return a defaul value
		System.out.println("Error in generateEmployeeID");
		return 1;
	}
	
	public int getEmployeeIdByUsername(String username) {
		String sql = "SELECT employeeId FROM users WHERE username = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(sql)){
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt("employeeId");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return -1;
	}

	public String[] getSaltAndHashedPassword(String username) {
		String sql = "SELECT salt, hashedPassword FROM users WHERE username = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				String salt = rs.getString("salt");
				String hashedPassword = rs.getString("hashedPassword");
				return new String[] {salt, hashedPassword};
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("unknown error in getSaltAndHashedPassword db manager");
		return null;
	}
	
	public int getPin(String username) {
		String sql = "SELECT pin FROM users WHERE username = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			// if the user exists, return the pin
			if (rs.next()) {
				return rs.getInt("pin");
			} else {
				throw new IllegalArgumentException("User not found");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Database error");
			
		}
	}
	
	//if user resets password with pin, use this
	public void updatePasswordAndSalt(String username, String newHashedPassword, String newSalt) {
		String sql = "UPDATE users SET hashedPassword = ?, salt = ? WHERE username = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, newHashedPassword);
			pstmt.setString(2, newSalt);
			pstmt.setString(3, username);
			int affectedRows = pstmt.executeUpdate();

			// if no rows were affected, the user does not exist
			if (affectedRows == 0) {
				throw new IllegalArgumentException("User not found");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			// if there is a database error, throw a runtime exception
			throw new RuntimeException("Database error");
		}
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
