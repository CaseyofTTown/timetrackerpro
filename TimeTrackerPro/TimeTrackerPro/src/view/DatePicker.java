package view;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import model.ColorConstants;

import javax.swing.text.DefaultFormatterFactory;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JPanel;
import javax.swing.text.DateFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class DatePicker {
    private JDatePickerImpl datePicker;

    public DatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        
        //customize the colors
        customizeDatePicker();
    }

    public JDatePickerImpl getDatePicker() {
        return datePicker;
    }
    
    private void customizeDatePicker() {
        // Customize the text field
        JFormattedTextField textField = datePicker.getJFormattedTextField();
        textField.setBackground(ColorConstants.DARK_GRAY);
        textField.setForeground(ColorConstants.GOLD);
        textField.setBorder(BorderFactory.createLineBorder(ColorConstants.GOLD));

        

        // Customize the buttons
        JButton calendarButton = (JButton) datePicker.getComponent(1);
        calendarButton.setBackground(ColorConstants.SLATE_GRAY);
        calendarButton.setForeground(ColorConstants.GOLD);
    }

    
    class DateLabelFormatter extends AbstractFormatter {
        private String datePattern = "yyyy-MM-dd";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }
    }
}
