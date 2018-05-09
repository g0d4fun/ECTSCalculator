package com.rafaonseek.rafa.ectscalculator;

/**
 * Created by Rafa on 2/20/2018.
 */

public class Utils {

    public static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
