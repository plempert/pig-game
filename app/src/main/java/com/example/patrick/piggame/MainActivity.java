package com.example.patrick.piggame;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";

    private int mUserTotalScore;
    private int mUserTurnScore;
    private int mAiTotalScore;
    private int mAiTurnScore;

    private Button mRollButton;
    private Button mHoldButton;
    private Button mResetButton;

    private ImageView mDiceFaceImage;

    private TextView mScoreAndStatusLabel;

    private int mRollDie() {
        Random random = new Random();
        int min = 1;
        int max = 6;
        return random.nextInt((max - min) + 1) + min;
    }

    private void mDisplayDieImage(int face_num) {
        switch (face_num) {
            case 1:
                mDiceFaceImage.setImageResource(R.drawable.dice1);
                break;
            case 2:
                mDiceFaceImage.setImageResource(R.drawable.dice2);
                break;
            case 3:
                mDiceFaceImage.setImageResource(R.drawable.dice3);
                break;
            case 4:
                mDiceFaceImage.setImageResource(R.drawable.dice4);
                break;
            case 5:
                mDiceFaceImage.setImageResource(R.drawable.dice5);
                break;
            case 6:
                mDiceFaceImage.setImageResource(R.drawable.dice6);
                break;
        }
    }

    private String mGenerateOverallScoresText() {
        return "Your score: " + mUserTotalScore + "\nComputer score: " + mAiTotalScore;
    }

    private void mComputerTurn() {
        int roll_value = mRollDie();
        mDisplayDieImage(roll_value);
        mAiTurnScore += roll_value;
        if (roll_value == 1) {
            mAiTurnScore = 0;
            mScoreAndStatusLabel.setText(mGenerateOverallScoresText() + "\nYour turn score: " + mUserTurnScore + "\nComputer rolled a 1");
            mHandler.removeCallbacks(mRunnable);
            mRollButton.setEnabled(true);
            mHoldButton.setEnabled(true);
        } else if (mAiTurnScore >= 20) {
            mAiTotalScore += mAiTurnScore;
            mAiTurnScore = 0;
            mScoreAndStatusLabel.setText(mGenerateOverallScoresText() + "\nYour turn score: " + mUserTurnScore + "\nComputer holds");
            mHandler.removeCallbacks(mRunnable);
            mRollButton.setEnabled(true);
            mHoldButton.setEnabled(true);
            mCheckWinCondition();
        } else {
            mScoreAndStatusLabel.setText(mGenerateOverallScoresText() + "\nYour turn score: " + mUserTurnScore + "\nComputer turn score: " + mAiTurnScore);
            mHandler.postDelayed(mRunnable,1000);
        }
//        mRollButton.setEnabled(true);
//        mHoldButton.setEnabled(true);

    }

    private boolean mCheckWinCondition() {
        if(mUserTotalScore >= 100){
            mScoreAndStatusLabel.setText(mGenerateOverallScoresText() + "\nYour turn score: " + mUserTurnScore + " <WINNER\nComputer turn score: " + mAiTurnScore);
            mRollButton.setEnabled(false);
            mHoldButton.setEnabled(false);
            return true;
        } else if (mAiTotalScore >= 100) {
            mScoreAndStatusLabel.setText(mGenerateOverallScoresText() + "\nYour turn score: " + mUserTurnScore + "\nComputer turn score: " + mAiTurnScore + " <WINNER");
            mRollButton.setEnabled(false);
            mHoldButton.setEnabled(false);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUserTotalScore = 0;
        mUserTurnScore = 0;
        mAiTotalScore = 0;
        mAiTurnScore = 0;

        mRollButton = (Button) findViewById(R.id.roll_button);
        mRollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int roll_value = mRollDie();
                Log.d(TAG, "User rolled a " + Integer.toString(roll_value));
                mDisplayDieImage(roll_value);
                if (roll_value != 1) {
                    mUserTurnScore += roll_value;
                } else {
                    mRollButton.setEnabled(false);
                    mHoldButton.setEnabled(false);
                    mUserTurnScore = 0;
                    mHandler.postDelayed(mRunnable, 500);
                }
                mScoreAndStatusLabel.setText(mGenerateOverallScoresText() + "\nYour turn score: " + Integer.toString(mUserTurnScore) + "\nComputer turn score: " + mAiTurnScore);
            }
        });

        mHoldButton = (Button) findViewById(R.id.hold_button);
        mHoldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRollButton.setEnabled(false);
                mHoldButton.setEnabled(false);
                mUserTotalScore += mUserTurnScore;
                mUserTurnScore = 0;
                mScoreAndStatusLabel.setText(mGenerateOverallScoresText() + "\nYour turn score: 0" + "\nComputer turn score: " + mAiTurnScore);
                if(!mCheckWinCondition()) {
                    mHandler.postDelayed(mRunnable, 500);
                }
            }
        });

        mResetButton = (Button) findViewById(R.id.reset_button);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserTotalScore = 0;
                mUserTurnScore = 0;
                mAiTotalScore = 0;
                mAiTurnScore = 0;
                mScoreAndStatusLabel.setText(mGenerateOverallScoresText() + "\nYour turn score: 0");
                mRollButton.setEnabled(true);
                mHoldButton.setEnabled(true);
            }
        });

        mDiceFaceImage = (ImageView) findViewById(R.id.dice_face_image);
        mDiceFaceImage.setImageResource(R.drawable.dice1);

        mScoreAndStatusLabel = (TextView) findViewById(R.id.score_and_status_label);
        mScoreAndStatusLabel.setText(mGenerateOverallScoresText() + "\nYour turn score: " + mUserTurnScore + "\nComputer turn score: " + mAiTurnScore);


//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mComputerTurn();
        }
    };



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
