package model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

//used to add key bindings throughout the application
public class KeyBindingUtil {

    public static void addKeyBinding(JComponent component, KeyStroke keyStroke, String actionName, Action action) {
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();

        inputMap.put(keyStroke, actionName);
        actionMap.put(actionName, action);
    }

    public static void addSubmitAndCancelBindings(JComponent component, JButton submitButton, JButton cancelButton) {
        Action submitAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (submitButton.isEnabled()) {
                    submitButton.doClick();
                }
            }
        };

        Action cancelAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButton.doClick();
            }
        };

        addKeyBinding(component, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "submit", submitAction);
        addKeyBinding(component, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel", cancelAction);
    }
}
