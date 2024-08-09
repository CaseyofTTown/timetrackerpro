package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import model.ColorConstants;
import model.DailyCallLog;
import controller.TTController;

public class CallLogDisplay extends JPanel {
    private TTController controller;
    private JList<DailyCallLog> callLogList;
    private DefaultListModel<DailyCallLog> callLogListModel;

    public CallLogDisplay(TTController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(ColorConstants.CHARCOAL);

        callLogListModel = new DefaultListModel<>();
        callLogList = new JList<>(callLogListModel);
        callLogList.setCellRenderer(new CallLogListCellRenderer());
        callLogList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(callLogList), BorderLayout.CENTER);
    }

    // Method to add a new call log card
    public void addCallLogCard(DailyCallLog callLog) {
        callLogListModel.addElement(callLog);
    }

    // Method to add all call log cards
    public void addAllCallLogCards(List<DailyCallLog> callLogs) {
        for (DailyCallLog callLog : callLogs) {
            addCallLogCard(callLog);
        }
    }

    // Method to clear all entries
    public void clearAllEntries() {
        callLogListModel.clear();
    }

    // Method to get the selected call log ID
    public int getSelectedCallLogId() {
        DailyCallLog selectedCallLog = callLogList.getSelectedValue();
        return selectedCallLog != null ? selectedCallLog.getId() : -1;
    }

    // Method to get the selection model
    public ListSelectionModel getSelectionModel() {
        return callLogList.getSelectionModel();
    }

    // Custom cell renderer for the call log list
    private class CallLogListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof DailyCallLog) {
                DailyCallLog callLog = (DailyCallLog) value;
                setText("Truck Unit: " + callLog.getTruckUnitNumber() + " | Date: " + callLog.getStartDate() + " - " + callLog.getEndDate());
                setBackground(isSelected ? ColorConstants.DEEP_BLUE : ColorConstants.CHARCOAL);
                setForeground(isSelected ? ColorConstants.GOLD : ColorConstants.LIME_GREEN);
            }
            return c;
        }
    }
}
