package view;

import java.awt.*;
import java.awt.print.*;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

public class CustomPrintable implements Printable {
    private Component component;
    private double margin;

    public CustomPrintable(Component component, double margin) {
        this.component = component;
        this.margin = margin;
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (!(component instanceof Container)) {
            throw new PrinterException("Component is not a container");
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pageFormat.getImageableX() + margin, pageFormat.getImageableY() + margin);

        double scaleX = (pageFormat.getImageableWidth() - 2 * margin) / component.getWidth();
        double scaleY = (pageFormat.getImageableHeight() - 2 * margin) / component.getHeight();
        double scale = Math.min(scaleX, scaleY);
        g2d.scale(scale, scale);

        int totalHeight = component.getHeight();
        int pageHeight = (int) ((pageFormat.getImageableHeight() - 2 * margin) / scale);
        int totalPages = (int) Math.ceil((double) totalHeight / pageHeight);

        if (pageIndex >= totalPages) {
            return NO_SUCH_PAGE;
        }

        g2d.translate(0, -pageIndex * pageHeight);

        // Print the content
        int y = 0;
        for (Component comp : ((Container) component).getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                label.printAll(g2d);
                g2d.translate(0, label.getHeight());
                y += label.getHeight();
            } else if (comp instanceof JTable) {
                JTable table = (JTable) comp;
                // Print the table header
                JTableHeader header = table.getTableHeader();
                header.printAll(g2d);
                g2d.translate(0, header.getHeight());
                y += header.getHeight();
            }

            int compHeight = comp.getHeight();
            if (y + compHeight > pageHeight) {
                // Move to the next page if the component doesn't fit
                g2d.translate(0, pageHeight);
                y = 0;
            }
            comp.printAll(g2d);
            y += compHeight;
        }

        return PAGE_EXISTS;
    }
}
