import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import controller.TTController;
import model.DatabaseManager;
import view.viewClass;

public class TimeTrackerProApplication {

    private static final String dbUrl = "jdbc:sqlite:" + System.getProperty("user.home") + "/Documents/TimeTrackerPro/timeTracker.db";

    public static void main(String[] args) {

        System.out.println("testing");

        DatabaseManager db = createDatabaseManager();
        if (db != null) {
            System.out.println("Database manager Successfully created!");
        }

        // Create and display the GUI
        SwingUtilities.invokeLater(() -> {
            viewClass view = new viewClass();
            view.setIconImage(new ImageIcon("src/timeTrackerLogo.ico").getImage()); // Set the icon here
            view.setVisible(true);

            // create the controller
            TTController controller = new TTController(db, view);
        });

    }

    private static DatabaseManager createDatabaseManager() {
        try {
            File dbDir = new File(System.getProperty("user.home") + "/Documents/TimeTrackerPro");
            if (!dbDir.exists()) {
                dbDir.mkdirs();
                System.out.println("Directory created: " + dbDir.getAbsolutePath());
            } else {
                System.out.println("Directory already exists: " + dbDir.getAbsolutePath());
            }

            File dbFile = new File(dbDir, "timeTracker.db");
            if (!dbFile.exists()) {
                try (InputStream is = TimeTrackerProApplication.class.getResourceAsStream("/timeTracker.db");
                     FileOutputStream fos = new FileOutputStream(dbFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                    System.out.println("Database file created: " + dbFile.getAbsolutePath());
                }
            } else {
                System.out.println("Database file already exists: " + dbFile.getAbsolutePath());
            }

            return new DatabaseManager(dbUrl);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
