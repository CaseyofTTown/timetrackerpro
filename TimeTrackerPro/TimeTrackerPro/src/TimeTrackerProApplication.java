
public class TimeTrackerProApplication {

	// db url
	private static final String dbUrl = "jdbc:sqlite:timeTracker.db";

	public static void main(String[] args) {

		System.out.println("testing");
		
		try {
			DatabaseManager db = new DatabaseManager(dbUrl);
			System.out.println("Database manager Successfully created!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
