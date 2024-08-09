package model;

import javax.swing.*;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeFormatter extends JFormattedTextField.AbstractFormatter {
    private DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("HHmm");
    private DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public Object stringToValue(String text) throws ParseException {
        try {
            if (text.length() == 4) {
                LocalTime time = LocalTime.parse(text, inputFormat);
                return time;
            } else {
                return null; // Return null for partial input
            }
        } catch (Exception e) {
            throw new ParseException("Invalid time format", 0);
        }
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            if (value instanceof LocalTime) {
                LocalTime time = (LocalTime) value;
                return time.format(outputFormat);
            } else {
                throw new ParseException("Invalid value type", 0);
            }
        }
        return "";
    }
}
