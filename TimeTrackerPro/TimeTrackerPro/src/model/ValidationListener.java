package model;

import java.sql.Time;
import java.util.Date;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ValidationListener implements DocumentListener {
	private Runnable validationCallback;

	public ValidationListener(Runnable validationCallback) {
		this.validationCallback = validationCallback;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		validationCallback.run();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		validationCallback.run();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		validationCallback.run();
	}

	public static boolean validateFields(Date shiftStartDate, Date shiftEndDate, Time shiftStartTime,
			Time shiftEndTime) {
		if (shiftStartDate != null && shiftEndDate != null && shiftStartDate.after(shiftEndDate)) {
			return false;
		}

		if (shiftStartDate != null && shiftEndDate != null && shiftStartDate.equals(shiftEndDate)
				&& shiftStartTime != null && shiftEndTime != null && shiftStartTime.after(shiftEndTime)) {
			return false;
		}

		return true;
	}
}
