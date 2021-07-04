package com.banana.company.bco.tictactow.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.banana.company.bco.tictactow.MainActivity;
import com.banana.company.bco.tictactow.R;
import com.banana.company.bco.tictactow.SkinActivity;

import java.util.ArrayList;

import static com.banana.company.bco.tictactow.SharedPreferencesClass.KEY_MONEY;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.P1;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.KEY_P1_COLOR;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.P1_PREFERENCE;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.KEY_P1_SHAPE;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.P2;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.KEY_P2_COLOR;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.P2_PREFERENCE;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.KEY_P2_SHAPE;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.money;

public class SkinAdapter extends RecyclerView.Adapter<SkinAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Character> skinShape;
    private final ArrayList<Integer> backgroundColor;
    private final String player;

    public SkinAdapter(Context context, ArrayList<Character> skinShape, ArrayList<Integer> backgroundColor, String player) {
        this.context = context;
        this.skinShape = skinShape;
        this.backgroundColor = backgroundColor;
        this.player = player;
    }

    @NonNull
    @Override
    public SkinAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_items_for_skin_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkinAdapter.ViewHolder holder, int position) {

        // if shape have items it should have a null for colors so he can put just the shape in the viewHolder
        if (skinShape != null) {
            // it will change the background to know what shape the player have this for player one
            if (player.equals(P1_PREFERENCE) && P1.getString(KEY_P1_SHAPE,"X").equals(String.valueOf(skinShape.get(position)))){
                holder.cardView.setBackgroundColor(R.drawable.background);
            }
            // it will change the background to know what shape the player have this for player two
            if (player.equals(P2_PREFERENCE) && P2.getString(KEY_P2_SHAPE,"O").equals(String.valueOf(skinShape.get(position)))){
                holder.cardView.setBackgroundColor(R.drawable.background);
            }
            // set the shape in viewHolder
            holder.skin.setText(String.valueOf(skinShape.get(position)));
            // to put text of the price if it's for sale
            if (player.equals(KEY_MONEY))
                holder.price.setText("499");
            // if he clack in item
            holder.itemView.setOnClickListener(v -> {
                // if it's player one change the shape of it and save it in SharedPreference
                if (player.equals(P1_PREFERENCE)){
                    P1.edit().putString(KEY_P1_SHAPE, String.valueOf(skinShape.get(position))).apply();
                    SkinActivity.ShapeRefresh();

                    // if it's player two change the shape of it and save it in SharedPreference
                }else if (player.equals(P2_PREFERENCE)){
                    P2.edit().putString(KEY_P2_SHAPE, String.valueOf(skinShape.get(position))).apply();
                    SkinActivity.ShapeRefresh();

                    //if it's for sale he gonna check first if he have enough money to buy
                    // if he is so delete hem from skinDatabaseHandler the table for sale and put it in table of the player
                    // and refresh all the shapes
                }else if (player.equals(KEY_MONEY)){

                    if (money.getInt(KEY_MONEY, 0) >= 499) {
                        money.edit().putInt(KEY_MONEY, money.getInt(KEY_MONEY, 0) - 499).apply();
                        SkinActivity.db.deleteShape(String.valueOf(skinShape.get(position)));
                        SkinActivity.db.addShape(String.valueOf(skinShape.get(position)));
                        SkinActivity.ShapeRefresh();
                        MainActivity.coins.setText(String.valueOf(money.getInt(KEY_MONEY,0)));
                    }
                }

            });
        } else {
            // set the shape in viewHolder
            holder.color.setBackgroundColor(backgroundColor.get(position));

            // it will change the background to know what color the player have this for player one
            if (player.equals(P1_PREFERENCE) && P1.getInt(KEY_P1_COLOR,Color.BLACK) == backgroundColor.get(position)){
                holder.cardView.setBackgroundColor(R.drawable.background);
            }
            // it will change the background to know what color the player have this for player two
            if (player.equals(P2_PREFERENCE) && P2.getInt(KEY_P2_COLOR,Color.BLACK) == backgroundColor.get(position)){
                holder.cardView.setBackgroundColor(R.drawable.background);
            }

            // to put text of the price if it's for sale
            if (player.equals(KEY_MONEY))
                holder.price.setText("499");

            // if he clack in item
            holder.itemView.setOnClickListener(v -> {
                // if it's player one change the color of it and save it in SharedPreference
                if (player.equals(P1_PREFERENCE)){
                    P1.edit().putInt(KEY_P1_COLOR, backgroundColor.get(position)).apply();
                    SkinActivity.ColorRefresh();
                }
                // if it's player two change the color of it and save it in SharedPreference
                else if (player.equals(P2_PREFERENCE)){

                    P2.edit().putInt(KEY_P2_COLOR, backgroundColor.get(position)).apply();
                    SkinActivity.ColorRefresh();
                }

                //if it's for sale he gonna check first if he have enough money to buy
                // if he is so delete hem from skinDatabaseHandler the table for sale and put it in table of the player
                // and refresh all the colors
                else if (player.equals(KEY_MONEY)) {
                    if (money.getInt(KEY_MONEY, 0) >= 499) {
                        money.edit().putInt(KEY_MONEY, money.getInt(KEY_MONEY, 0) - 499).apply();
                        SkinActivity.db.deleteColor(backgroundColor.get(position));
                        SkinActivity.db.addColor(backgroundColor.get(position));
                        SkinActivity.ColorRefresh();
                        MainActivity.coins.setText(String.valueOf(money.getInt(KEY_MONEY,0)));
                    }

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        if (skinShape != null)
            return skinShape.size();
        else
            return backgroundColor.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView skin;
        private final TextView price;
        private final ImageView color;
        private final CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            color = itemView.findViewById(R.id.backgroundColor);
            price = itemView.findViewById(R.id.price);
            skin = itemView.findViewById(R.id.playerOneSkin);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
