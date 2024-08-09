package model;

import java.sql.Time;
import java.util.Date;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

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

    public static boolean validateFields(Date shiftStartDate, Date shiftEndDate, Time shiftStartTime, Time shiftEndTime) {
        System.out.println("Validating fields:");
        System.out.println("Shift Start Date: " + shiftStartDate);
        System.out.println("Shift End Date: " + shiftEndDate);
        System.out.println("Shift Start Time: " + shiftStartTime);
        System.out.println("Shift End Time: " + shiftEndTime);

        // Check if any required fields are null
        if (shiftStartDate == null || shiftEndDate == null || shiftStartTime == null || shiftEndTime == null) {
            System.out.println("Validation failed: One or more fields are null.");
            return false;
        }

        // Convert Date to LocalDate
        LocalDate startLocalDate = shiftStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = shiftEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Check if shift start date is after shift end date
        if (startLocalDate.isAfter(endLocalDate)) {
            System.out.println("Validation failed: Shift start date is after shift end date.");
            return false;
        }

        // Extract hours and minutes from Time objects, ignoring seconds
        LocalTime startLocalTime = shiftStartTime.toLocalTime().withSecond(0).withNano(0);
        LocalTime endLocalTime = shiftEndTime.toLocalTime().withSecond(0).withNano(0);

        // Convert LocalTime to total minutes since midnight
        int startTotalMinutes = startLocalTime.getHour() * 60 + startLocalTime.getMinute();
        int endTotalMinutes = endLocalTime.getHour() * 60 + endLocalTime.getMinute();

        // Print total minutes for debugging
        System.out.println("Start Total Minutes: " + startTotalMinutes);
        System.out.println("End Total Minutes: " + endTotalMinutes);

        // Check if shift start time is after shift end time on the same day
        if (startLocalDate.equals(endLocalDate) && startTotalMinutes >= endTotalMinutes) {
            System.out.println("Validation failed: Shift start time is after or equal to shift end time on the same day.");
            return false;
        }

        System.out.println("Validation passed.");
        return true;
    }
}
