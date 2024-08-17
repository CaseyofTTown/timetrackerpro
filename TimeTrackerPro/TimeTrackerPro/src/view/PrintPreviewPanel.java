package view;

import javax.swing.*;
import java.awt.*;

public class PrintPreviewPanel extends JPanel {
    private JTable reportTable;
    private JTable summaryTable;

    public PrintPreviewPanel() {
        setLayout(new BorderLayout(10, 10)); // Add margins

        // Initialize tables
        reportTable = new JTable();
        summaryTable = new JTable();

        // Add the tables to the panel
        add(reportTable.getTableHeader(), BorderLayout.NORTH);
        add(reportTable, BorderLayout.CENTER);
        add(summaryTable.getTableHeader(), BorderLayout.SOUTH);
        add(summaryTable, BorderLayout.SOUTH);
    }

    public void setReportContent(JTable reportTable, JTable summaryTable) {
        // Update the tables with new data
        this.reportTable.setModel(reportTable.getModel());
        this.summaryTable.setModel(summaryTable.getModel());

        // Revalidate and repaint to refresh the display
        revalidate();
        repaint();
    }
}
