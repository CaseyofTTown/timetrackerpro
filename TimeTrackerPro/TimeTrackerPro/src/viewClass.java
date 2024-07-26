import javax.swing.*;
import java.awt.*;

public class viewClass extends JFrame {

	public viewClass() {
		// set the window title
		setTitle("Time Tracker Pro");

		// set default close operation
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set the size of the window
		setSize(800, 600);

		// set a dark theme using UiManager
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			UIManager.put("control", new Color(128, 128, 128));
			UIManager.put("info", new Color(128, 128, 128));
			UIManager.put("nimbusBase", new Color(18, 30, 49));
			UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
			UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
			UIManager.put("nimbusFocus", new Color(115, 164, 209));
			UIManager.put("nimbusGreen", new Color(176, 179, 50));
			UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
			UIManager.put("nimbusLightBackground", new Color(18, 30, 49));
			UIManager.put("nimbusOrange", new Color(191, 98, 4));
			UIManager.put("nimbusRed", new Color(169, 46, 34));
			UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
			UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
			UIManager.put("text", new Color(230, 230, 230));

		} catch (Exception e) {
			e.printStackTrace();
		}

		// create a panel with a BoxLayout
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(new Color(10, 30, 49));

		// add the panel to the frame
		add(panel);

		// create a label for the greeting message
		JLabel greetingLabel = new JLabel("Welcome to time Tracker Pro!");
		greetingLabel.setForeground(Color.green);

		greetingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(greetingLabel);

		// create a label for the explanation message
		//TODO make this fit and continue
		JLabel explanationLabel = new JLabel("This application is used for the creation and management of "
				+ "time sheets. Please be sure to enter your data in a timely and \naccurate manner for faster processing.");
		
		explanationLabel.setForeground(Color.GREEN);
		explanationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(explanationLabel);
		
		

	}

}
