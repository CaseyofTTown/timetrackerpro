package view;

import java.awt.*;
import java.awt.print.*;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class CustomPrintable implements Printable {
    private JPanel panel;
    private double margin;

    public CustomPrintable(JPanel panel, double margin) {
        this.panel = panel;
        this.margin = margin;
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (!(panel instanceof Container)) {
            throw new PrinterException("Component is not a container");
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pageFormat.getImageableX() + margin, pageFormat.getImageableY() + margin);

        double scaleX = (pageFormat.getImageableWidth() - 2 * margin) / panel.getWidth();
        double scaleY = (pageFormat.getImageableHeight() - 2 * margin) / panel.getHeight();
        double scale = Math.min(scaleX, scaleY);
        g2d.scale(scale, scale);

        int totalHeight = panel.getHeight();
        int pageHeight = (int) ((pageFormat.getImageableHeight() - 2 * margin) / scale);
        int totalPages = (int) Math.ceil((double) totalHeight / pageHeight);

        if (pageIndex >= totalPages) {
            return NO_SUCH_PAGE;
        }

        g2d.translate(0, -pageIndex * pageHeight);

        // Print the content
        panel.printAll(g2d);

        return PAGE_EXISTS;
    }
}
