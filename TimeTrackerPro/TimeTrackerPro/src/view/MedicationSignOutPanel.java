package view;

import model.ColorConstants;

import javax.swing.*;
import java.awt.*;

public class MedicationSignOutPanel extends JPanel {
    private Image image;

    public MedicationSignOutPanel() {
        try {
            // Load the image using a relative path
            String relativePath = "src/Designer.png";
            this.image = new ImageIcon(relativePath).getImage();

            if (image == null) {
                System.err.println("Image not found: " + relativePath);
            }
        } catch (Exception e) {
            System.err.println("Image not found: " + e.getMessage());
        }
        setBackground(ColorConstants.CHARCOAL);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            int imgWidth = image.getWidth(this);
            int imgHeight = image.getHeight(this);
            double imgAspect = (double) imgWidth / imgHeight;

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            double panelAspect = (double) panelWidth / panelHeight;

            int x = 0, y = 0, width = panelWidth, height = panelHeight;

            if (imgAspect > panelAspect) {
                height = (int) (panelWidth / imgAspect);
                y = (panelHeight - height) / 2;
            } else {
                width = (int) (panelHeight * imgAspect);
                x = (panelWidth - width) / 2;
            }

            g.drawImage(image, x, y, width, height, this);
        }
    }
}
