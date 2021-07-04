package com.banana.company.bco.tictactow;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static com.banana.company.bco.tictactow.SharedPreferencesClass.P1;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.KEY_P1_COLOR;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.KEY_P1_SHAPE;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.P2;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.KEY_P2_COLOR;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.KEY_P2_SHAPE;

public class PVPActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView playerOneScore, playerTwoScore, playerStatus;
    private Button []buttons = new Button[9];
    private Button resetGame;

    // if he wont to reset the game it's gonna ask hem if he sure
    Dialog dialog ;

    // count how many time player one win and player two and how many game played
    private int playerOneScoreCount, playerTwoScoreCount, rountCount;
    // if it's true it's gonna be player one turn and if it's not it's gonna be player two turn
    boolean activePlayer;

    // p1 => 0
    // p2 => 1
    // empty => 2
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    // this is how to win
    // and these numbers is the length of the value in gameState
    // if one of these group have the same value == 0 p1 win if it's 1 p2 win
    int[][] winingPosition = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},//rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},//columns
            {0, 4, 8}, {2, 4, 6}//cross
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvp);

        dialog = new Dialog(this);

        playerOneScore = findViewById(R.id.playerOneScore);
        playerTwoScore = findViewById(R.id.playerTwoScore);
        playerStatus = findViewById(R.id.playerStatus);

        resetGame = findViewById(R.id.resetGame);


        for (int i = 0; i < buttons.length; i++) {
            String buttonID = "btn_" + i;
            int resourceID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = findViewById(resourceID);
            // take hem to onClick(View v)
            buttons[i].setOnClickListener(this);
        }
        // give every count 0 and make it player one turn
        rountCount = 0;
        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        activePlayer = true;
    }

    // onClick here gonna work when he click on one of the button to play
    @Override
    public void onClick(View v) {

        // If the button was pressed before it's will return before do anything
        if (!((Button) v).getText().toString().equals(""))
            return;

        /*
         * to change his position in gameState[] it's should take the position
         * so what i did is take the id of the button and make the last char a number of his position in gameState
         */
        String buttonID = v.getResources().getResourceEntryName(v.getId());
        int gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length() - 1));

        // if boolean activePlayer is true so it's player one turn
        // if it's not so th's player two turn
        if (activePlayer) {
            // is will change the shape and the color on the button form SharePreferencesClass that saving these data
            ((Button) v).setText(P1.getString(KEY_P1_SHAPE, "X"));
            ((Button) v).setTextColor(P1.getInt(KEY_P1_COLOR, Color.BLACK));
            //change the value in the position player one play it to 0
            gameState[gameStatePointer] = 0;
        } else {
            // is will change the shape and the color on the button form SharePreferencesClass that saving these data
            ((Button) v).setText(P2.getString(KEY_P2_SHAPE, "O"));
            ((Button) v).setTextColor(P2.getInt(KEY_P2_COLOR, Color.BLACK));
            //change the value in the position player two play it to 1
            gameState[gameStatePointer] = 1;
        }
        // Increase the value by 1 and when it's gonna be 9 there is no button can play it so it's will end no one win
        rountCount++;

        // check if there anyone wining
        if (checkWinner()) {
            // if the winner is player one give hem +1 score
            if (activePlayer) {
                playerOneScoreCount++;
                updataPlayerSore();
                Toast.makeText(this, "player one won", Toast.LENGTH_SHORT).show();
            } else {
                // if the winner is player two give hem +1 score
                playerTwoScoreCount++;
                updataPlayerSore();
                Toast.makeText(this, "player two won", Toast.LENGTH_SHORT).show();

            }
            // to return the game
            playAgain();

        }
        // if there no place to play return the game
        else if (rountCount == 9) {
            playAgain();
            Toast.makeText(this, "no winner", Toast.LENGTH_SHORT).show();
        }
        // change the boolean to make the other player play if it's true (player one turn) if it's false (player two turn)
        else {
            activePlayer = !activePlayer;
        }

        // check who win more to put it in the screen
        if (playerOneScoreCount > playerTwoScoreCount) {
            playerStatus.setText("player one is winning");
        } else if (playerTwoScoreCount > playerOneScoreCount) {
            playerStatus.setText("player two is winning");
        } else {
            playerStatus.setText("");
        }

        //show him the dialog
        resetGame.setOnClickListener(v1 -> areYouSureDialog());
    }

    // check winner method if someone win he will return true and if it's not he will return false
    public boolean checkWinner() {
        boolean winnerResult = false;

        // checking avery way to win
        for (int[] winingPosition : winingPosition) {
            if (gameState[winingPosition[0]] == gameState[winingPosition[1]] &&
                    gameState[winingPosition[1]] == gameState[winingPosition[2]] &&
                    gameState[winingPosition[0]] != 2) {
                winnerResult = true;

                break;
            }
        }
        return winnerResult;

    }

    // update the sore
    public void updataPlayerSore() {
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount));
    }

    // put the count on 0 and remove the text from avery button click it and make all gameState[] have value 2
    public void playAgain() {
        rountCount = 0;
        activePlayer = true;
        for (int i = 0; i < buttons.length; i++) {
            gameState[i] = 2;
            buttons[i].setText("");
        }
    }

    // show the dialog
    public void areYouSureDialog() {
        dialog.setContentView(R.layout.reset_dialog);
        Button yes = dialog.findViewById(R.id.yes);
        Button no = dialog.findViewById(R.id.no);
        // for showing the dialog
        dialog.show();
        // if he click it in yes it's gonna remove avery move and avery score count
        yes.setOnClickListener(v -> {

            playAgain();
            playerOneScoreCount = 0;
            playerTwoScoreCount = 0;
            playerStatus.setText("");
            updataPlayerSore();

            // to close the dialog
            dialog.dismiss();
        });
        // if he click it in no it's gonna close the dialog
        no.setOnClickListener(v -> dialog.dismiss());
    }


}