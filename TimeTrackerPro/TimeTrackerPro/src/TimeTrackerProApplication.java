import javax.swing.SwingUtilities;

public class TimeTrackerProApplication {

	// db url
	private static final String dbUrl = "jdbc:sqlite:timeTracker.db";

	public static void main(String[] args) {

		System.out.println("testing");
		
		//create the database and tables
		try {
			DatabaseManager db = new DatabaseManager(dbUrl);
			System.out.println("Database manager Successfully created!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		//Create and display the GUI
		SwingUtilities.invokeLater(() -> {
			viewClass view = new viewClass();
			view.setVisible(true);
		});
		
	}

}
