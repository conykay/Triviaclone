package com.conelius.trivia;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.conelius.trivia.data.AnswerListAsyncResponse;
import com.conelius.trivia.data.QuestionBank;
import com.conelius.trivia.models.Question;
import com.conelius.trivia.models.Score;
import com.conelius.trivia.util.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String HIGHSCORE_COUNT ="highscore_count" ;
    private TextView questionTextView,questionCounterTextView,highScoreTextView,playerScoreTextView;
    private Button trueButton;
    private Button falsebutton;
    private ImageButton nextButton,prevButton;
    private int currentQuestionIndex = 0;

    private int scoreCounter = 0;
    private Score score;
    private Prefs prefs;

//    private int highscore = 0;
//    private int playerScore = 0;



    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score = new Score();

        prefs = new Prefs(MainActivity.this);



        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        trueButton = findViewById(R.id.true_button);
        falsebutton = findViewById(R.id.false_button);
        questionCounterTextView = findViewById(R.id.counter_text);
        questionTextView = findViewById(R.id.question_textview);
        playerScoreTextView = findViewById(R.id.player_score);
        highScoreTextView = findViewById(R.id.highScore_textview);

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        falsebutton.setOnClickListener(this);
        trueButton.setOnClickListener(this);

        playerScoreTextView.setText(MessageFormat.format("Current score: {0}", String.valueOf(score.getScore())));

        highScoreTextView.setText(MessageFormat.format("Highest Score:{0}", prefs.getHighScore()));

//        new QuestionBank().getQuestions();

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                questionCounterTextView.setText(currentQuestionIndex + " / "+questionList.size());

                Log.d("Main", "onCreate: "+ questionArrayList);

            }
        });

//        playerScoreTextView.setText("Score: "+ playerScore);
//
//        SharedPreferences getHighScore = getSharedPreferences(HIGHSCORE_COUNT,MODE_PRIVATE);
//
//        int count = getHighScore.getInt("highscore", 0);
//
//        highScoreTextView.setText("High score: "+ count);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prev_button:
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                    updateQuestion();
                }
                break;

            case R.id.next_button:
                goNext();
                break;

            case R.id.true_button:
                checkAnswer(true);
//                updateScore(true);
                updateQuestion();

                break;

            case R.id.false_button:
                checkAnswer(false);
//                updateScore(false);
                updateQuestion();
                break;

        }
    }

    private void checkAnswer(boolean userChooseCorrect) {
        boolean answerIsTrue = questionList.get(currentQuestionIndex).isAnswerTrue();
        int toastMessageId = 0;
        if (answerIsTrue == userChooseCorrect) {
            fadeView();
            addPoints();
            toastMessageId = R.string.correct_answer;

        }else{

            shakeAnimation();
            deductPoints();
            toastMessageId = R.string.wrong_answer;

        }

        Toast.makeText(this, toastMessageId, Toast.LENGTH_SHORT).show();
    }

    private void addPoints() {
        scoreCounter +=100;
        score.setScore(scoreCounter);
        playerScoreTextView.setText(MessageFormat.format("Current score: {0}", String.valueOf(score.getScore())));
    }

    private void deductPoints() {
        scoreCounter -=100;
        if (scoreCounter>0){
            score.setScore(scoreCounter);
            playerScoreTextView.setText(MessageFormat.format("Current score: {0}", String.valueOf(score.getScore())));
        }else{
            scoreCounter = 0;
            score.setScore(scoreCounter);
            playerScoreTextView.setText(MessageFormat.format("Current score: {0}", String.valueOf(score.getScore())));
        }
    }

//    private void updateScore(boolean state) {
//        SharedPreferences getHighScore = getSharedPreferences(HIGHSCORE_COUNT,MODE_PRIVATE);
//        highscore = getHighScore.getInt("highscore", 0);
//
//        if (state == questionList.get(currentQuestionIndex).isAnswerTrue()){
//
//            if (playerScore > currentQuestionIndex){
//                Toast.makeText(this, "Move to next question.", Toast.LENGTH_SHORT).show();
//            }else{
//
//                playerScore +=1;
//            }
//
//        }else{
//            if (playerScore > 0){
//
//                playerScore -=1;
//            }
//        }
//
//        playerScoreTextView.setText("Score: "+playerScore);
//
//        SharedPreferences sharedPreferences = getSharedPreferences(HIGHSCORE_COUNT,MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        if (playerScore > highscore){
//
//            highscore = playerScore;
//
//            highScoreTextView.setText("High score: "+ highscore);
//
//        }
//        editor.putInt("highscore",highscore);
//
//        editor.apply();
//
//    }

    private void updateQuestion() {

        String question = questionList.get(currentQuestionIndex).getAnswer();
        questionTextView.setText(question);
        questionCounterTextView.setText(currentQuestionIndex + " / "+questionList.size());

    }

    private void fadeView() {
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
               goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                cardView.setCardBackgroundColor(Color.WHITE);
                goNext();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void goNext() {
        currentQuestionIndex = (currentQuestionIndex + 1 ) % questionList.size();
        updateQuestion();
    }

    @Override
    protected void onPause() {
        prefs.saveHighScore(score.getScore());
        super.onPause();
    }
}
