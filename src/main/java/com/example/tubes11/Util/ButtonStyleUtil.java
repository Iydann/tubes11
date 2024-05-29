package com.example.tubes11.Util;

import javafx.scene.control.Button;

public class ButtonStyleUtil {
    private static final String defaultStyle = "-fx-text-fill: #000000; -fx-background-color: #FFFFFF; -fx-effect: dropshadow(three-pass-box, #AAAAAA, 3, 0, 0, 3); -fx-pref-width: 150; -fx-font-size: 1.5em; -fx-font-family: 'Calibri Light'; -fx-pref-height: 40";
    private static final String activeStyle = "-fx-text-fill: #FFFFFF; -fx-background-color: #212121;-fx-effect: dropshadow(three-pass-box,  #212121, 3, 0, 0, 3); -fx-pref-width: 150; -fx-font-size: 1.5em; -fx-font-family: 'Calibri Light'; -fx-pref-height: 40";

    public static void resetButtonStyles(Button... buttons) {
        for (Button button : buttons) {
            button.setStyle(defaultStyle);
        }
    }

    public static void setActiveStyle(Button button) {
        button.setStyle(activeStyle);
    }
}
