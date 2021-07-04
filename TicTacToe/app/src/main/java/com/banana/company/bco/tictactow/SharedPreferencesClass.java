package com.banana.company.bco.tictactow;

import android.content.SharedPreferences;

public class SharedPreferencesClass {


    /* the names of sharedPreferences
     * one to the player one to save his color and shape
     * and another to the player two to save his color and shape
     * and the last is for the money he have
     */
    public static String P1_PREFERENCE = "P1";
    public static String P2_PREFERENCE = "P2";
    public static String MONEY_PREFERENCE = "moneyPreference";

    // these player one keys one for the shape and other for the color
    public static String KEY_P1_SHAPE = "P1Shape";
    public static String KEY_P1_COLOR = "P1Color";

    // these player two keys one for the shape and other for the color
    public static String KEY_P2_SHAPE = "P2Shape";
    public static String KEY_P2_COLOR = "P2Color";

    // this key for the money he have
    public static String KEY_MONEY = "money";

    // the SharedPreferences
    public static SharedPreferences P1;
    public static SharedPreferences P2;
    public static SharedPreferences money;

}
