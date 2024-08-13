package model;

import view.CallLogCard;
//used as a custom itnerface for selecting cards
public interface CallLogCardSelectionListener {
	void onCardSelected(CallLogCard selectedCard);
}
