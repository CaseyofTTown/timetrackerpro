package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

import model.ColorConstants;

import java.awt.print.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrintPreviewPanel extends JPanel implements Printable {
    private JPanel reportContainer;
    private JButton printButton;
    private Date startDate;
    private Date endDate;

    public PrintPreviewPanel() {
        setLayout(new BorderLayout(10, 10)); // Add margins

        // Initialize report container
        reportContainer = new JPanel();
        reportContainer.setLayout(new BoxLayout(reportContainer, BoxLayout.Y_AXIS));

        // Add the report container to the panel with margins
        JScrollPane scrollPane = new JScrollPane(reportContainer);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add the print button
        printButton = new JButton("Print");
        printButton.setBackground(ColorConstants.DARK_GRAY);
        printButton.setForeground(ColorConstants.ORANGE);
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printReport();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(printButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void addReportContent(JTable reportTable, String title) {
        // Add the new report content to the report container
        reportContainer.add(new JLabel(title, JLabel.CENTER));
        reportContainer.add(new JScrollPane(reportTable));

        // Refresh the panel
        reportContainer.revalidate();
        reportContainer.repaint();
    }

    public void addReportContent(JPanel reportPanel, String title) {
        // Add the new report content to the report container
        reportContainer.add(new JLabel(title, JLabel.CENTER));
        reportContainer.add(new JScrollPane(reportPanel));

        // Refresh the panel
        reportContainer.revalidate();
        reportContainer.repaint();
    }

    private void printReport() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException pe) {
                JOptionPane.showMessageDialog(this, "Printing Failed: " + pe.getMessage(), "Print Result", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Calculate the number of pages
        double scale = pageFormat.getImageableWidth() / reportContainer.getWidth();
        g2d.scale(scale, scale);

        int totalHeight = reportContainer.getHeight();
        int pageHeight = (int) pageFormat.getImageableHeight();
        int totalPages = (int) Math.ceil((double) totalHeight / pageHeight);

        if (pageIndex >= totalPages) {
            return NO_SUCH_PAGE;
        }

        // Translate to the correct page
        g2d.translate(0, -pageIndex * pageHeight);

        // Print the report container
        reportContainer.printAll(g);

        return PAGE_EXISTS;
    }


}
