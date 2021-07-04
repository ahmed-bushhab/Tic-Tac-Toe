package com.banana.company.bco.tictactow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;

import static com.banana.company.bco.tictactow.SharedPreferencesClass.KEY_MONEY;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.MONEY_PREFERENCE;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.P1;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.P1_PREFERENCE;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.P2;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.P2_PREFERENCE;
import static com.banana.company.bco.tictactow.SharedPreferencesClass.money;

public class MainActivity extends AppCompatActivity {

    // these for ads
    private RewardedInterstitialAd rewardedInterstitialAd;
    private RewardedAd mRewardedAd;
    private final String TAG = "MainActivity";

    /* the linearLayout is for the prize of a 100 coin
     * so i can put text and image horizontally
     */
    private LinearLayout linearLayout;

    // button and the textView in the activity_main.xml
    private Button playButton, skinButton, supportButton, playWithComputer;
    public static TextView coins;

    // for the dialog when client click  "playWithComputer" button
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // for the ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadRewardAds();
                loadAd();
            }
        });


        linearLayout = findViewById(R.id.linearLayoutReward);
        // when linearLayout click it it's gonna show him the ad if it's ready
        linearLayout.setOnClickListener(v -> showRewardAds());

        // save the information's of the color and the shape of player one and player two and to save the money that he have
        P1 = getSharedPreferences(P1_PREFERENCE, MODE_PRIVATE);
        P2 = getSharedPreferences(P2_PREFERENCE, MODE_PRIVATE);
        money = getSharedPreferences(MONEY_PREFERENCE, MODE_PRIVATE);

        dialog = new Dialog(this);

        // to show his money
        coins = findViewById(R.id.coinsNumber);
        coins.setText(String.valueOf(money.getInt(KEY_MONEY, 0)));

        playButton = findViewById(R.id.playWithsameOne);
        skinButton = findViewById(R.id.skinButton);
        supportButton = findViewById(R.id.supportUsButton);
        playWithComputer = findViewById(R.id.playWithComputer);

        // if it's click it take hem to the player one Vs player two activity
        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PVPActivity.class);
            startActivity(intent);
        });


        // if it's click it take hem to the skin activity so he can change the color or the shape of the player or buy one
        skinButton.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), SkinActivity.class);
            startActivity(intent);
        });

        // if it's click it take show hem the dialog so he can chose the mode (easy,hard)
        playWithComputer.setOnClickListener(v -> computerDialog());
        // to show him ad for support
        supportButton.setOnClickListener(v -> {
            if (rewardedInterstitialAd != null)
                rewardedInterstitialAd.show(MainActivity.this, rewardItem -> {

                });
        });

    }

    // the method to chose the mode (easy,hard)
    public void computerDialog() {
        // contact the dialog with layout file
        dialog.setContentView(R.layout.playing_dialog);
        /* make buttons to contact with buttons in layout file
         * one for easy to play it in easy mode
         * and anther one for hard to play it in hard mode
         */
        Button easy = dialog.findViewById(R.id.Easy);
        Button hard = dialog.findViewById(R.id.Hard);
        // to show the dialog
        dialog.show();

        // when easy button click it it's gonna sand easy to the other activity
        easy.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), ComputerActivity.class);
            intent.putExtra("mode", "easy");
            startActivity(intent);
            // to dismiss the dialog
            dialog.dismiss();
        });

        // when easy button click it it's gonna sand hard to the other activity
        hard.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), ComputerActivity.class);
            intent.putExtra("mode", "hard");
            startActivity(intent);
            // to dismiss the dialog
            dialog.dismiss();
        });
    }

    // for the ad
    public void loadRewardAds() {

        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-xxxxxxxxxxxxxxxx/xxxxxxxxxx",
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

    // for the ad
    public void showRewardAds(){
        if (mRewardedAd != null) {
            mRewardedAd.show(this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();

                    int put100 = money.getInt(KEY_MONEY,0)+100;
                    money.edit().putInt(KEY_MONEY,put100).apply();
                    coins.setText(String.valueOf(put100));
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }

    // for the ad
    public void loadAd() {
        // Use the test ad unit ID to load an ad.
        RewardedInterstitialAd.load(MainActivity.this, "ca-app-pub-xxxxxxxxxxxxxxxx/xxxxxxxxxx",
                new AdRequest.Builder().build(),  new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        rewardedInterstitialAd = ad;
                        Log.e(TAG, "onAdLoaded");

                        rewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            /** Called when the ad failed to show full screen content. */
                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                Log.i(TAG, "onAdFailedToShowFullScreenContent");
                            }

                            /** Called when ad showed the full screen content. */
                            @Override
                            public void onAdShowedFullScreenContent() {
                                Log.i(TAG, "onAdShowedFullScreenContent");
                            }

                            /** Called when full screen content is dismissed. */
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Log.i(TAG, "onAdDismissedFullScreenContent");
                                loadAd();
                            }
                        });
                    }
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.e(TAG, "onAdFailedToLoad");
                    }
                });
    }

}