package com.conelius.trivia.util;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * created by Conelius on 1/14/2020 at 11:30 PM
 */
public class Prefs {

    private SharedPreferences sharedPreferences;

    public Prefs(Activity activity) {
        this.sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void saveHighScore(int score) {

        int currentScore = score;

        int lastScore = sharedPreferences.getInt("high_score",0);

        if (currentScore > lastScore) {

            sharedPreferences.edit().putInt("high_score",currentScore).apply();

        }

    }

    public int getHighScore() {
        return sharedPreferences.getInt("high_score",0);
    }
}
