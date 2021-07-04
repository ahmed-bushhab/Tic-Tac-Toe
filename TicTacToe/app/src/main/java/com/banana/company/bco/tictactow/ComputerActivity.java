package com.banana.company.bco.tictactow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Random;

import static com.banana.company.bco.tictactow.SharedPreferencesClass.KEY_MONEY;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.P1;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.KEY_P1_COLOR;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.KEY_P1_SHAPE;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.P2;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.KEY_P2_COLOR;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.KEY_P2_SHAPE;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.money;

public class ComputerActivity extends AppCompatActivity implements View.OnClickListener {

    // for ads
    private RewardedAd mRewardedAd;
    private final String TAG = "MainActivity";
    // count 10 game to show him ad
    private int count = 0;

    // the text in the screen
    private TextView playerOneScore, playerTwoScore, playerStatus,computerActivityCoin;
    // the 9 button that's he play in
    private Button buttons[] = new Button[9];
    // button reset
    private Button resetGame;

    // count how many time player win and computer and how many game played
    private int playerScoreCount, computerScoreCount, rountCount;
    // if it's true it's gonna be player turn and if it's not it's gonna be computer turn
    private boolean activePlayer;

    // p1 => 0
    // p2 => 1
    // empty => 2
    private int gameState[] = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    // this is how to win if (gameStare[0], [1]and [2] ) == 0 p1 win if it's 1 p2 win all of them in this int array
    private final int winingPosition[][] = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},//rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},//columns
            {0, 4, 8}, {2, 4, 6}//cross
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer);

        // for the ad
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadRewardAds();
            }
        });

        // to show hem the money he have in the screen
        computerActivityCoin = findViewById(R.id.lc_coinsNumber);
        computerActivityCoin.setText(String.valueOf(money.getInt(KEY_MONEY,0)));

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

        // give every count 0 and make it player turn
        rountCount = 0;
        playerScoreCount = 0;
        computerScoreCount = 0;
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

        // if boolean activePlayer is true so it's player turn
        if (activePlayer) {
            // is will change the shape and the color on the button form SharePreferencesClass that saving these data
            ((Button) v).setText(P1.getString(KEY_P1_SHAPE, "X"));
            ((Button) v).setTextColor(P1.getInt(KEY_P1_COLOR, Color.BLACK));
            //change the value in the position player play it to 0
            gameState[gameStatePointer] = 0;
        }
        // Increase the value by 1 and when it's gonna be 9 there is no button can play it so it's will end no one win
        rountCount++;

        // check if there anyone wining
        if (checkWinner()) {
            // if the winner is player give hem +1 score and +10 coin update it in this activity and MainActivity
            if (activePlayer) {
                playerScoreCount++;
                updatePlayerSore();
                Toast.makeText(this, "player is wining", Toast.LENGTH_SHORT).show();
                int newCoins = money.getInt(KEY_MONEY,0)+10;
                money.edit().putInt(KEY_MONEY,newCoins).apply();
                MainActivity.coins.setText(String.valueOf(newCoins));
                computerActivityCoin.setText(String.valueOf(newCoins));
                // to return the game
                playAgain();
            }

        }
        // if there no place to play give hem +5 coin and return the game
        else if (rountCount == 9) {
            int newCoins = money.getInt(KEY_MONEY,0)+5;
            money.edit().putInt(KEY_MONEY,newCoins).apply();
            MainActivity.coins.setText(String.valueOf(newCoins));
            computerActivityCoin.setText(String.valueOf(newCoins));
            playAgain();
            Toast.makeText(this, "no winner", Toast.LENGTH_SHORT).show();

        }
        // change the boolean of activePlayer to make the computer play
        else {
            activePlayer = !activePlayer;
            computerGame();
        }

        // check who win more to put it in the screen
        if (playerScoreCount > computerScoreCount) {
            playerStatus.setText("player is winning");
        } else if (computerScoreCount > playerScoreCount) {
            playerStatus.setText("computer is winning");
        } else {
            playerStatus.setText("");
        }
        // it's here coz he shod play to can reset the game
        resetGame.setOnClickListener(v1 -> {
            playAgain();
            playerScoreCount = 0;
            computerScoreCount = 0;
            playerStatus.setText("");
            updatePlayerSore();
        });
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
                count++;
                if (count >= 10){
                    count = 0;
                    showRewardAds();
                }


            }
        }
        return winnerResult;

    }

    // update the sore
    public void updatePlayerSore() {
        playerOneScore.setText(Integer.toString(playerScoreCount));
        playerTwoScore.setText(Integer.toString(computerScoreCount));
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

    // computer turn
    void computerGame() {

        // the position of the computer move
        int computerPosition = -1;
        /* if he choose hard mode
         * it's will check if computer can win to play in the position who need it to win
         * and if computer can win it's will check if the player can win to stop him
         * and if it's also not computerPosition will stay have -1 value to change in a random position
         */
        if (getIntent().getStringExtra("mode").equals("hard")){

            for (int i = 0; i < gameState.length - 1; i++) {
                if (gameState[winingPosition[i][0]] == 1 && gameState[winingPosition[i][1]] == 1 && gameState[winingPosition[i][2]] == 2) {

                    computerPosition = winingPosition[i][2];
                    break;
                } else if (gameState[winingPosition[i][0]] == 1 && gameState[winingPosition[i][2]] == 1 && gameState[winingPosition[i][1]] == 2) {

                    computerPosition = winingPosition[i][1];
                    break;
                } else if (gameState[winingPosition[i][1]] == 1 && gameState[winingPosition[i][2]] == 1 && gameState[winingPosition[i][0]] == 2) {

                    computerPosition = winingPosition[i][0];
                    break;
                } else if (gameState[winingPosition[i][0]] == 0 && gameState[winingPosition[i][1]] == 0 && gameState[winingPosition[i][2]] == 2) {

                    computerPosition = winingPosition[i][2];
                    break;
                } else if (gameState[winingPosition[i][0]] == 0 && gameState[winingPosition[i][2]] == 0 && gameState[winingPosition[i][1]] == 2) {

                    computerPosition = winingPosition[i][1];
                    break;
                } else if (gameState[winingPosition[i][1]] == 0 && gameState[winingPosition[i][2]] == 0 && gameState[winingPosition[i][0]] == 2) {

                    computerPosition = winingPosition[i][0];
                    break;
                }

            }
        }
        // give computer a random value to play in if it's in easy mode are the computer and the player can't win
        if (computerPosition == -1) {

            Random random = new Random();
            do {
                computerPosition = random.nextInt(gameState.length);
            } while ((gameState[computerPosition] != 2));
        }

        // it's will change the shape and the color on the button form SharePreferencesClass that saving these data
        buttons[computerPosition].setText(P2.getString(KEY_P2_SHAPE, "O"));
        buttons[computerPosition].setTextColor(P2.getInt(KEY_P2_COLOR, Color.BLACK));
        //change the value in the position computer play it to 1
        gameState[computerPosition] = 1;
        // Increase the value by 1 and when it's gonna be 9 there is no button can play it so it's will end no one win
        rountCount++;

        // check if there anyone win
        if (checkWinner()) {
            // if the winner is the computer give hem +1
            if (!activePlayer) {
                computerScoreCount++;
                updatePlayerSore();
                Toast.makeText(this, "computer is wining", Toast.LENGTH_SHORT).show();
                // to return the game
                playAgain();
            }

        }
        // if there no place to play give hem +5 coin and return the game
        else if (rountCount == 9) {
            money.edit().putInt(KEY_MONEY,money.getInt(KEY_MONEY,0)+5).apply();
            MainActivity.coins.setText(String.valueOf(money.getInt(KEY_MONEY,0)));
            playAgain();
            Toast.makeText(this, "no winner", Toast.LENGTH_SHORT).show();

        }
        // change the boolean of activePlayer to make the player play
        else {
            activePlayer = !activePlayer;
        }

        // check who win more to put it in the screen
        if (playerScoreCount > computerScoreCount) {
            playerStatus.setText("player is winning");
        } else if (computerScoreCount > playerScoreCount) {
            playerStatus.setText("computer is winning");
        } else {
            playerStatus.setText("");
        }
    }

    // for ad
    public void loadRewardAds() {

        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-3830667749759531/4520959393",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");
                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad was shown.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.d(TAG, "Ad failed to show.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad was dismissed.");
                                mRewardedAd = null;
                                loadRewardAds();
                            }
                        });
                    }
                });
    }

    // for ad
    public void showRewardAds(){
        if (mRewardedAd != null) {
            mRewardedAd.show(this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();

                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }

}