package com.conelius.trivia;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.conelius.trivia.data.AnswerListAsyncResponse;
import com.conelius.trivia.data.QuestionBank;
import com.conelius.trivia.models.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView questionTextView,questionCounterTextView;
    private Button trueButton;
    private Button falsebutton;
    private ImageButton nextButton,prevButton;
    private int currentQuestionIndex = 0;

    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        trueButton = findViewById(R.id.true_button);
        falsebutton = findViewById(R.id.false_button);
        questionCounterTextView = findViewById(R.id.counter_text);
        questionTextView = findViewById(R.id.question_textview);


        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        falsebutton.setOnClickListener(this);
        trueButton.setOnClickListener(this);


//        new QuestionBank().getQuestions();

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                questionCounterTextView.setText(currentQuestionIndex + " / "+questionList.size());

                Log.d("Main", "onCreate: "+ questionArrayList);

            }
        });


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
                currentQuestionIndex = (currentQuestionIndex + 1 ) % questionList.size();
                updateQuestion();
                break;

            case R.id.true_button:
                checkAnswer(true);

                break;

            case R.id.false_button:
                checkAnswer(false);
                break;

        }
    }

    private void checkAnswer(boolean userChooseCorrect) {
        boolean answerIsTrue = questionList.get(currentQuestionIndex).isAnswerTrue();
        int toastMessageId = 0;
        if (answerIsTrue == userChooseCorrect) {

            toastMessageId = R.string.correct_answer;

        }else{
            toastMessageId = R.string.wrong_answer;

        }

        Toast.makeText(this, toastMessageId, Toast.LENGTH_SHORT).show();
    }

    private void updateQuestion() {

        String question = questionList.get(currentQuestionIndex).getAnswer();
        questionTextView.setText(question);
        questionCounterTextView.setText(currentQuestionIndex + " / "+questionList.size());

    }
}
