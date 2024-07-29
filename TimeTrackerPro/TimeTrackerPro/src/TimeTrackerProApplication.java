import javax.swing.SwingUtilities;

import controller.TTController;
import model.DatabaseManager;
import view.viewClass;

public class TimeTrackerProApplication {

	// db url
	private static final String dbUrl = "jdbc:sqlite:timeTracker.db";

	public static void main(String[] args) {

		System.out.println("testing");

		DatabaseManager db = createDatabaseManager();
		if (db != null) {
			System.out.println("Database manager Successfuly created!");
		}

		// Create and display the GUI
		SwingUtilities.invokeLater(() -> {
			viewClass view = new viewClass();
			view.setVisible(true);

			// create the controller
			TTController controller = new TTController(db, view);
		});

	}

	private static DatabaseManager createDatabaseManager() {
		try {
			return new DatabaseManager(dbUrl);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}