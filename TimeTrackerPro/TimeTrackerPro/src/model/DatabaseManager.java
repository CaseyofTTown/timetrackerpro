package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

	private Connection connection;
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

	public DatabaseManager(String dbUrl) {
		try {
			connection = DriverManager.getConnection(dbUrl);
			createTables();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void createTables() {
		String sqlCreateEmployeesTable = "CREATE TABLE IF NOT EXISTS employees (\n" + " id integer PRIMARY KEY,\n"
				+ " name text NOT NULL,\n" + " level text,\n" + " certificationNumber text,\n"
				+ " certExpirationDate text\n" + ");";

		String sqlCreateTimeSheetsTable = "CREATE TABLE IF NOT EXISTS timesheets (\n"
				+ " id integer PRIMARY KEY AUTOINCREMENT,\n" + " employeeId integer NOT NULL,\n"
				+ " shiftStartDate text,\n" + " shiftEndDate text,\n" + " shiftStartTime TIME,\n"
				+ " shiftEndTime TIME,\n" + " overtimeComment text, \n" + " hoursWorked INTEGER, \n"
				+ " FOREIGN KEY(employeeId) REFERENCES employees(id)\n" + ");";

		String sqlCreateUsersTable = "CREATE TABLE IF NOT EXISTS users (\n " + " username text PRIMARY KEY, \n"
				+ " hashedPassword text NOT NULL, \n" + " salt text NOT NULL, \n" + " employeeId integer, \n"
				+ "pin integer, \n" + "FOREIGN KEY(employeeID) REFERENCES employees(id)\n" + ")";

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

	// Add a time sheet entry to the database
	public void addTimeSheet(TimeSheet timeSheet) {
		String sql = "INSERT INTO timesheets(employeeId, shiftStartDate, shiftEndDate, shiftStartTime, shiftEndTime, overtimeComment, hoursWorked) VALUES(?,?,?,?,?,?,?)";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, timeSheet.getEmployeeId());
			pstmt.setString(2, formatDate(timeSheet.getShiftStartDate()));
			pstmt.setString(3, formatDate(timeSheet.getShiftEndDate()));
			pstmt.setTime(4, convertToSqlTime(timeSheet.getShiftStartTime()));
			pstmt.setTime(5, convertToSqlTime(timeSheet.getShiftEndTime()));
			pstmt.setString(6, timeSheet.getOvertimeComment());
			pstmt.setLong(7, timeSheet.getHoursWorked());
			System.out.println("Adding TimeSheet: " + timeSheet);
			System.out.println("SQL: " + sql);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Update a time sheet entry in the database
	public void updateTimeSheet(TimeSheet timeSheet) {
		String sql = "UPDATE timesheets SET employeeId = ?, shiftStartDate = ?, shiftEndDate = ?, shiftStartTime = ?, shiftEndTime = ?, overtimeComment = ?, hoursWorked = ? WHERE id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, timeSheet.getEmployeeId());
			pstmt.setString(2, formatDate(timeSheet.getShiftStartDate()));
			pstmt.setString(3, formatDate(timeSheet.getShiftEndDate()));
			pstmt.setTime(4, convertToSqlTime(timeSheet.getShiftStartTime()));
			pstmt.setTime(5, convertToSqlTime(timeSheet.getShiftEndTime()));
			pstmt.setString(6, timeSheet.getOvertimeComment());
			pstmt.setLong(7, timeSheet.getHoursWorked());
			pstmt.setInt(8, timeSheet.getTimeSheetId());

			System.out.println("Executing update: " + sql);
			System.out.println("TimeSheet ID: " + timeSheet.getTimeSheetId());

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("TimeSheet updated successfully.");
			} else {
				System.out.println("TimeSheet update failed: No rows affected.");
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("General Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Delete a time sheet entry from the database
	public void deleteTimeSheet(int id) {
		String sql = "DELETE FROM timesheets WHERE id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, id);

			System.out.println("Executing delete: " + sql);
			System.out.println("TimeSheet ID: " + id);

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("TimeSheet deleted successfully.");
			} else {
				System.out.println("TimeSheet delete failed: No rows affected.");
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("General Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public List<TimeSheet> getTimeSheetsByDateRange(Date startDate, Date endDate) {
	    List<TimeSheet> timeSheets = new ArrayList<>();
	    String sql = "SELECT timesheets.id, timesheets.employeeId, employees.name, timesheets.shiftStartDate, timesheets.shiftEndDate, timesheets.shiftStartTime, timesheets.shiftEndTime, timesheets.overtimeComment, timesheets.hoursWorked "
	            + "FROM timesheets " + "JOIN employees ON timesheets.employeeId = employees.id "
	            + "WHERE timesheets.shiftStartDate >= ? AND timesheets.shiftEndDate <= ?"
	            +"ORDER BY timesheets.shiftStartDate ASC";

	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, formatDate(startDate));
	        pstmt.setString(2, formatDate(endDate));

	        System.out.println("Executing query: " + sql);
	        System.out.println("Start Date: " + formatDate(startDate));
	        System.out.println("End Date: " + formatDate(endDate));

	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            int timeSheetId = rs.getInt("id");
	            int employeeId = rs.getInt("employeeId");
	            String employeeName = rs.getString("name");
	            java.util.Date shiftStartDate = parseDate(rs.getString("shiftStartDate"));
	            java.util.Date shiftEndDate = parseDate(rs.getString("shiftEndDate"));
	            LocalTime shiftStartTime = convertToLocalTime(rs.getTime("shiftStartTime"));
	            LocalTime shiftEndTime = convertToLocalTime(rs.getTime("shiftEndTime"));
	            String overtimeComment = rs.getString("overtimeComment");
	            long hoursWorked = rs.getLong("hoursWorked");

	            System.out.println("Processing TimeSheet ID: " + timeSheetId);

	            TimeSheet timeSheet;
	            if (overtimeComment != null && !overtimeComment.isEmpty()) {
	                timeSheet = new TimeSheet(timeSheetId, employeeId, employeeName, shiftStartDate, shiftEndDate,
	                        shiftStartTime, shiftEndTime, overtimeComment, hoursWorked);
	            } else {
	                timeSheet = new TimeSheet(timeSheetId, employeeId, employeeName, shiftStartDate, shiftEndDate,
	                        shiftStartTime, shiftEndTime, hoursWorked);
	            }

	            timeSheets.add(timeSheet);
	        }
	    } catch (SQLException e) {
	        System.out.println("SQL Exception: " + e.getMessage());
	        e.printStackTrace();
	    } catch (Exception e) {
	        System.out.println("General Exception: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return timeSheets;
	}


	public TimeSheet getTimeSheetById(int timeSheetId) {
	    String sql = "SELECT timesheets.id, timesheets.employeeId, employees.name, timesheets.shiftStartDate, timesheets.shiftEndDate, timesheets.shiftStartTime, timesheets.shiftEndTime, timesheets.overtimeComment, timesheets.hoursWorked "
	            + "FROM timesheets " + "JOIN employees ON timesheets.employeeId = employees.id "
	            + "WHERE timesheets.id = ?";

	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, timeSheetId);

	        System.out.println("Executing query: " + sql);
	        System.out.println("TimeSheet ID: " + timeSheetId);

	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            int id = rs.getInt("id");
	            int employeeId = rs.getInt("employeeId");
	            String employeeName = rs.getString("name");
	            java.util.Date shiftStartDate = parseDate(rs.getString("shiftStartDate"));
	            java.util.Date shiftEndDate = parseDate(rs.getString("shiftEndDate"));
	            LocalTime shiftStartTime = convertToLocalTime(rs.getTime("shiftStartTime"));
	            LocalTime shiftEndTime = convertToLocalTime(rs.getTime("shiftEndTime"));
	            String overtimeComment = rs.getString("overtimeComment");
	            long hoursWorked = rs.getLong("hoursWorked");

	            System.out.println("Processing TimeSheet ID: " + id);

	            return new TimeSheet(id, employeeId, employeeName, shiftStartDate, shiftEndDate, shiftStartTime,
	                    shiftEndTime, overtimeComment, hoursWorked);
	        }
	    } catch (SQLException e) {
	        System.out.println("SQL Exception: " + e.getMessage());
	        e.printStackTrace();
	    } catch (Exception e) {
	        System.out.println("General Exception: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return null; // Return null if no time sheet is found
	}


	public int getEmployeeIdByName(String employeeName) {
		String sql = "SELECT id FROM employees WHERE name = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, employeeName);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("id");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return -1; // Return -1 if not found
	}

	public boolean registerUser(String username, String hashedPassword, String salt, int pin) {
		String sql = "INSERT INTO users(username, hashedPassword, salt, employeeId, pin) VALUES(?,?,?,?,?)";
		int result = 0; // number of rows affected to verify success
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
		return result > 0; // true if successful

	}

	// find highest employee id in database or start with 001
	private int generateEmployeeId() {
		String sql = "SELECT MAX(employeeId) AS max_id FROM users";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int maxId = rs.getInt("max_id");
				return maxId > 0 ? maxId + 1 : 1;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// if error occurs return a defaul value
		System.out.println("Error in generateEmployeeID");
		return 1;
	}

	public int getEmployeeIdByUsername(String username) {
		String sql = "SELECT employeeId FROM users WHERE username = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("employeeId");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return -1;
	}

	public Employee getEmployeeById(int employeeId) {
		String sql = "SELECT * FROM employees WHERE id = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, employeeId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				// User has an entry in employee table
				return new Employee(rs.getInt("id"), rs.getString("name"),
						CertificationLevelenum.valueOf(rs.getString("level")), rs.getString("certificationNumber"),
						new java.util.Date(rs.getLong("certExpirationDate")));
			}
		} catch (SQLException e) {
			// Error occurred
			System.out.println(e.getMessage());
		}
		// User does not have an entry, will display new employee info GUI
		System.out.println("No entry in employee table for user");
		return null;
	}

	public List<String> getAllEmployeeNames() {
		List<String> employeeNames = new ArrayList<>();
		String sql = "SELECT name FROM employees";
		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				employeeNames.add(rs.getString("name"));
			}
			System.out.println("getAllEmployeeNames returned with: " + employeeNames.size() + " names");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return employeeNames;
	}

	public String[] getSaltAndHashedPassword(String username) {
		String sql = "SELECT salt, hashedPassword FROM users WHERE username = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String salt = rs.getString("salt");
				String hashedPassword = rs.getString("hashedPassword");
				return new String[] { salt, hashedPassword };
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

	// if user resets password with pin, use this
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

	// helper methods for date formatting and parsing

	private String formatDate(java.util.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(date);
	}

	private java.util.Date parseDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			System.out.println("Error parsing date: " + e.getMessage());
			return null;
		}
	}

	// Ensure proper time format storage for db.
	public String formatTime(LocalTime time) {
		return time.format(DateTimeFormatter.ofPattern("HH:mm"));
	}

	private LocalTime parseTime(String time) {
		try {
			// Replace "-" with ":" to match the expected format
			time = time.replace("-", ":");
			return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
		} catch (DateTimeParseException e) {
			System.out.println("Error parsing time: " + e.getMessage());
			return null;
		}
	}

	// Convert LocalTime to java.sql.Time
	public static java.sql.Time convertToSqlTime(LocalTime localTime) {
		return java.sql.Time.valueOf(localTime);
	}

	// Convert java.sql.Time to LocalTime
	public static LocalTime convertToLocalTime(java.sql.Time sqlTime) {
		return sqlTime.toLocalTime();
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

	// TODO delete this later:
	public void dropTimesheetsTable() {
		String sql = "DROP TABLE IF EXISTS timesheets";

		try (Statement stmt = connection.createStatement()) {
			stmt.execute(sql);
			System.out.println("timesheets table dropped");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
