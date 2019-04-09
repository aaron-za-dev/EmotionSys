package com.aaronzadev.util;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DataValidator {

    private static final String FLOAT_REGEX = "\\d{0,10}([\\.]\\d{0,2})?";
    private static final String INTEGER_REGEX = "\\d*";//"([1-9][0-9]*)?";
    //private static final String PHONE_NUMBER_REGEX = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";

    public static void setMaxSizeValidator(TextField tfield, int size){

        tfield.textProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue.length() > size){
               tfield.setText(oldValue);
            }

        });

    }

    public static void setMaxSizeValidator(TextArea tArea, int size){

        tArea.textProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue.length() > size){
                tArea.setText(oldValue);
            }

        });

    }

    public static void setFloatValidator(TextField tfield){

        tfield.textProperty().addListener((observable, oldValue, newValue) -> {

            if(!newValue.matches(FLOAT_REGEX)){
                tfield.setText(oldValue);
            }

        });

    }

    public static void setIntegerValidator(TextField tfield){

        tfield.textProperty().addListener((observable, oldValue, newValue) -> {

            if(!newValue.matches(INTEGER_REGEX)){
               tfield.setText(oldValue);
            }

        });
    }


}
