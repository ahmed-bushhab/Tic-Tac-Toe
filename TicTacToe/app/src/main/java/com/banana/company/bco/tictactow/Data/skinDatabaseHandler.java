package com.banana.company.bco.tictactow.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.banana.company.bco.tictactow.Util.Util;

import java.util.ArrayList;

/*
 * this for saving and showing the lists in skin activity
 * so when he buy something it's gonna remove it and put it in player one and two
 */
public class skinDatabaseHandler extends SQLiteOpenHelper {

    public skinDatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME,null, Util.DATABASE_VERSION);
    }

    /*
     * create 4 tables
     * 1 for the shapes that the player buy it
     * 2 for the colors that the player buy it
     * 3 for the shapes for selling
     * 3 for the colors for selling
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+Util.SHAPE_TABLE_NAME+"("+
                Util.ID+" INTEGER PRIMARY KEY, "+ Util.SHAPE+" TEXT "+")");

        db.execSQL("CREATE TABLE "+Util.COLOR_TABLE_NAME+"("+
                Util.ID+" INTEGER PRIMARY KEY, "+ Util.COLOR+" INTEGER "+")");

        db.execSQL("CREATE TABLE "+Util.BUY_SHAPE_TABLE_NAME+"("+
                Util.ID+" INTEGER PRIMARY KEY, "+ Util.SHAPE+" TEXT "+")");

        db.execSQL("CREATE TABLE "+Util.BUY_COLOR_TABLE_NAME+"("+
                Util.ID+" INTEGER PRIMARY KEY, "+ Util.COLOR+" INTEGER "+")");

    }

    // make the update in the database (clean everything and add it again)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+Util.SHAPE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+Util.COLOR_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+Util.BUY_SHAPE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+Util.BUY_COLOR_TABLE_NAME);

        onCreate(db);
    }

    // add a shape to the player shapes
    public void addShape(String shape){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Util.SHAPE,shape);
        db.insert(Util.SHAPE_TABLE_NAME,null,values);
        db.close();
        values.clear();
    }
    // add a color to the player shapes
    public void addColor(int color){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Util.COLOR,color);
        db.insert(Util.COLOR_TABLE_NAME,null,values);
        db.close();
        values.clear();
    }

    // work just one time when it's empty to add all shapes one by one
    public void addBuyShape(char shape){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Util.SHAPE, String.valueOf(shape));
        db.insert(Util.BUY_SHAPE_TABLE_NAME,null,values);
        db.close();
        values.clear();
    }
    // work just one time when it's empty to add all colors one by one
    public void addBuyColor(int color){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Util.COLOR,color);
        db.insert(Util.BUY_COLOR_TABLE_NAME,null,values);
        db.close();
        values.clear();
    }

    // works just one time if the shape (X or Y) not found it's wile return false so he can put them just one time
    public boolean isXAndYFound(String shape){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+Util.SHAPE_TABLE_NAME,null);
        if (cursor.moveToFirst())
        {
            do {
                if (cursor.getString(1).equals(shape))
                    return false;
            }while (cursor.moveToNext());
        }
        return true;
    }

    // return all shapes of the player like ArrayList char by char
    public ArrayList<Character> getShapesList(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Character> shapes = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM "+Util.SHAPE_TABLE_NAME,null);
        if (cursor.moveToFirst())
        {
            do {
                String s = cursor.getString(1);


                shapes.add(s.charAt(0));
            }while (cursor.moveToNext());
        }
        return shapes;
    }
    // return all colors of the player like ArrayList int by int
    public ArrayList<Integer> getCOLORSList(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Integer> color = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM "+Util.COLOR_TABLE_NAME,null);
        if (cursor.moveToFirst())
        {
            do {


                color.add(cursor.getInt(1));
            }while (cursor.moveToNext());
        }
        return color;
    }
    // return all shapes that's for sale
    public ArrayList<Character> getBuyShapesList(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Character> shapes = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM "+Util.BUY_SHAPE_TABLE_NAME,null);
        if (cursor.moveToFirst())
        {
            do {
                String s = cursor.getString(1);


                shapes.add(s.charAt(0));
            }while (cursor.moveToNext());
        }
        return shapes;
    }
    // return all colors that's for sale
    public ArrayList<Integer> getBuyCOLORSList(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Integer> color = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM "+Util.BUY_COLOR_TABLE_NAME,null);
        if (cursor.moveToFirst())
        {
            do {


                color.add(cursor.getInt(1));
            }while (cursor.moveToNext());
        }
        return color;
    }
    // delete the shape he buy it from the sale shapes
    public void deleteShape(String shape){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Util.BUY_SHAPE_TABLE_NAME,Util.SHAPE+"=?",new String[]{shape});

        db.close();

    }
    // delete the color he buy it from the sale colors
    public void deleteColor(int color){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Util.BUY_COLOR_TABLE_NAME,Util.COLOR+"=?",new String[]{String.valueOf(color)});

        db.close();

    }
}
