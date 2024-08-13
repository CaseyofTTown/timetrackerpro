package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import model.CallLogCardSelectionListener;
import model.ColorConstants;
import model.DailyCallLog;
import controller.TTController;

public class CallLogDisplay extends JPanel {
	private TTController controller;
	private JPanel callLogDisplayPanel;
	private JScrollPane scrollPane;
	private CallLogCard selectedCard;
	private List<CallLogCard> callLogCards;

	public CallLogDisplay(TTController controller) {
		this.controller = controller;
		setLayout(new BorderLayout());
		setBackground(ColorConstants.CHARCOAL);
		callLogCards = new ArrayList<CallLogCard>(); // list of cards

		callLogDisplayPanel = new JPanel();
		callLogDisplayPanel.setLayout(new BoxLayout(callLogDisplayPanel, BoxLayout.Y_AXIS));
		callLogDisplayPanel.setBackground(ColorConstants.CHARCOAL);

		scrollPane = new JScrollPane(callLogDisplayPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		add(scrollPane, BorderLayout.CENTER);

	}

	 // Method to add a new call log card
	public void addCallLogCard(CallLogCard callLogCard) {
	    callLogCard.setAlignmentX(Component.LEFT_ALIGNMENT);
	    callLogDisplayPanel.add(callLogCard);
	    callLogDisplayPanel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing between cards
	    revalidate();
	    repaint();
	}


	/*

    // Method to add all call log cards
    public void addAllCallLogCards(List<DailyCallLog> callLogs, CallLogCardSelectionListener listener) {
        System.out.println("clearing call log list - addAllCallLogCards");
        callLogDisplayPanel.removeAll();
        callLogCards.clear();
        for (DailyCallLog callLog : callLogs) {
            try {
                CallLogCard card = new CallLogCard(callLog, controller);
                card.setSelectionListener(listener); // Set the listener
                callLogCards.add(card);
                callLogDisplayPanel.add(card);
                callLogDisplayPanel.add(Box.createVerticalStrut(10)); // Add spacing between cards
                System.out.println("callLogCard added to callLogDisplayPanel for CallLog ID: " + callLog.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        revalidate();
        repaint();
    }
    */

	// Method to clear all entries
	public void clearAllEntries() {
		callLogDisplayPanel.removeAll();
		revalidate();
		repaint();
	}

	public CallLogCard getSelectedCard() {
		return selectedCard;
	}

	public int getSelectedCardLogId() {
		return selectedCard != null ? selectedCard.getCallLogId(): -1;
	}

	// Custom cell renderer for the call log list
	private class CallLogListCellRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (value instanceof DailyCallLog) {
				DailyCallLog callLog = (DailyCallLog) value;
				setText("Truck Unit: " + callLog.getTruckUnitNumber() + " | Date: " + callLog.getStartDate() + " - "
						+ callLog.getEndDate());
				setBackground(isSelected ? ColorConstants.DEEP_BLUE : ColorConstants.CHARCOAL);
				setForeground(isSelected ? ColorConstants.GOLD : ColorConstants.LIME_GREEN);
			}
			return c;
		}
	}
}
