package model;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatter extends JFormattedTextField.AbstractFormatter {
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");

    @Override
    public Object stringToValue(String text) throws ParseException {
        if (text.length() == 4) {
            return timeFormat.parse(text);
        } else {
            throw new ParseException("Invalid time format", 0);
        }
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Date date = (Date) value;
            return timeFormat.format(date);
        }
        return "";
    }
}

