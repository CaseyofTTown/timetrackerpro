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

public class PrintPreviewPanel extends JPanel {
    private JPanel reportContainer;
    private JButton printButton;
    private JButton clearButton;
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
        
     // Add the clear button
        clearButton = new JButton("Clear");
        clearButton.setBackground(ColorConstants.DARK_GRAY);
        clearButton.setForeground(ColorConstants.ORANGE);
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearReport();
            }

            private void clearReport() {
                // Clear the report container
                reportContainer.removeAll();

                // Refresh the panel
                reportContainer.revalidate();
                reportContainer.repaint();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(printButton);
        buttonPanel.add(clearButton); 

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void addReportContent(JTable reportTable, String title) {
        // Add the new report content to the report container
        reportContainer.add(new JLabel(title, JLabel.CENTER));

        // Wrap the table in a JScrollPane to ensure headers are included
        JScrollPane scrollPane = new JScrollPane(reportTable);
        reportContainer.add(scrollPane);

        // Refresh the panel
        reportContainer.revalidate();
        reportContainer.repaint();
    }

    public void addReportContent(JPanel reportPanel, String title) {
        // Add the new report content to the report container
        reportContainer.add(new JLabel(title, JLabel.CENTER));

        // Wrap the panel in a JScrollPane to ensure headers are included
        JScrollPane scrollPane = new JScrollPane(reportPanel);
        reportContainer.add(scrollPane);

        // Refresh the panel
        reportContainer.revalidate();
        reportContainer.repaint();
    }





    private void printReport() {
        PrinterJob job = PrinterJob.getPrinterJob();
        double margin = 36; // 1/2 inch margin
        job.setPrintable(new CustomPrintable(reportContainer, margin));

        PageFormat pageFormat = job.defaultPage();
        
        // Determine the best orientation
        if (reportContainer.getWidth() > reportContainer.getHeight()) {
            pageFormat.setOrientation(PageFormat.LANDSCAPE);
        } else {
            pageFormat.setOrientation(PageFormat.PORTRAIT);
        }

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException pe) {
                JOptionPane.showMessageDialog(this, "Printing Failed: " + pe.getMessage(), "Print Result", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}