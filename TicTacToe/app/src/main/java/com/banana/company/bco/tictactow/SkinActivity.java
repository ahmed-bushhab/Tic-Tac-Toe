package com.banana.company.bco.tictactow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.banana.company.bco.tictactow.Adapter.SkinAdapter;
import com.banana.company.bco.tictactow.Data.skinDatabaseHandler;

import static com.banana.company.bco.tictactow.SharedPreferencesClass.KEY_MONEY;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.P1_PREFERENCE;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.P2_PREFERENCE;

public class SkinActivity extends AppCompatActivity {


    //make an object for skinDatabaseHandler that saved the color and shape that player buy them and what he did'nt buy them
    public static skinDatabaseHandler db ;
    // make 6 SkinAdapter 1 for shape player one and 2 for the color
    // 3 for the shape of player two and 4 for the color
    // 5 for the shape he did'nt buy and 6 for the color he did'nt buy
    //and make them static so i can change them form SkinAdapter class
    static SkinAdapter p1ShapeAdapter,p1ColorAdapter,p2ShapeAdapter,p2ColorAdapter,buyShapeAdapter,buyColorAdapter;
    // make 6 RecyclerView one for each one of SkinAdapter
    static RecyclerView p1ShapeRecyclerView,p1ColorRecyclerView,p2ShapeRecyclerView,p2ColorRecyclerView,buyShapeRecyclerView,buyColorRecyclerView;
    public static Context con ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);

        con = getApplicationContext();
        db = new skinDatabaseHandler(this);
        // if it's the first time he open the app the app should put the "X"and "O" in the shape of the player and black color in the color
        // and put all the shapes and the colors in RecyclerView for selling and save them in skinDatabaseHandler
        if (db.isXAndYFound("X")){
            char [] chars = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','P','Q','R','S','T','U','V','W','Y','Z'};
            int [] colors = {Color.BLUE,Color.CYAN,Color.DKGRAY,Color.GRAY,Color.GREEN,Color.LTGRAY,
                    Color.MAGENTA,Color.RED,Color.YELLOW};
            db.addShape("X");
            db.addShape("O");
            db.addColor(Color.BLACK);
            for (char aChar : chars)
                db.addBuyShape(aChar);
            for (int color : colors)
                db.addBuyColor(color);

        }

        //to add list of player one shapes
        p1ShapeAdapter = new SkinAdapter(this,db.getShapesList(),null,P1_PREFERENCE);
        p1ShapeRecyclerView = findViewById(R.id.skinPlayerOneRecyclerView);
        p1ShapeRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        p1ShapeRecyclerView.setAdapter(p1ShapeAdapter);

        //to add list of player one colors
        p1ColorAdapter = new SkinAdapter(this,null,db.getCOLORSList(),P1_PREFERENCE);
        p1ColorRecyclerView = findViewById(R.id.colorPlayerOneRecyclerView);
        p1ColorRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        p1ColorRecyclerView.setAdapter(p1ColorAdapter);

        //to add list of player two shapes
        p2ShapeAdapter = new SkinAdapter(this,db.getShapesList(),null,P2_PREFERENCE);
        p2ShapeRecyclerView = findViewById(R.id.skinPlayerTwoRecyclerView);
        p2ShapeRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        p2ShapeRecyclerView.setAdapter(p2ShapeAdapter);

        //to add list of player two colors
        p2ColorAdapter = new SkinAdapter(this,null,db.getCOLORSList(),P2_PREFERENCE);
        p2ColorRecyclerView = findViewById(R.id.colorPlayerTwoRecyclerView);
        p2ColorRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        p2ColorRecyclerView.setAdapter(p2ColorAdapter);

        //to add  list of selling shapes
        buyShapeAdapter = new SkinAdapter(this,db.getBuyShapesList(),null, KEY_MONEY);
        buyShapeRecyclerView = findViewById(R.id.lettersRecyclerView);
        buyShapeRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        buyShapeRecyclerView.setAdapter(buyShapeAdapter);

        //to add  list of selling colors
        buyColorAdapter = new SkinAdapter(this,null,db.getBuyCOLORSList(), KEY_MONEY);
        buyColorRecyclerView = findViewById(R.id.colorsRecyclerView);
        buyColorRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        buyColorRecyclerView.setAdapter(buyColorAdapter);

    }

    // if he buy a color it's will refresh all the recycler that have a color items
    public static void ColorRefresh(){
        buyColorAdapter = new SkinAdapter(con,null,db.getBuyCOLORSList(), KEY_MONEY);
        buyColorRecyclerView.setAdapter(buyColorAdapter);
        p1ColorAdapter = new SkinAdapter(con,null,db.getCOLORSList(),P1_PREFERENCE);
        p1ColorRecyclerView.setAdapter(p1ColorAdapter);
        p2ColorAdapter = new SkinAdapter(con,null,db.getCOLORSList(),P2_PREFERENCE);
        p2ColorRecyclerView.setAdapter(p2ColorAdapter);
    }
    // if he buy a shape it's will refresh all the recycler that have a shape items
    public static void ShapeRefresh(){
        buyShapeAdapter = new SkinAdapter(con,db.getBuyShapesList(),null, KEY_MONEY);
        buyShapeRecyclerView.setAdapter(buyShapeAdapter);
        p1ShapeAdapter = new SkinAdapter(con,db.getShapesList(),null,P1_PREFERENCE);
        p1ShapeRecyclerView.setAdapter(p1ShapeAdapter);
        p2ShapeAdapter = new SkinAdapter(con,db.getShapesList(),null,P2_PREFERENCE);
        p2ShapeRecyclerView.setAdapter(p2ShapeAdapter);

    }
}