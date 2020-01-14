package com.conelius.trivia.data;

import com.conelius.trivia.models.Question;

import java.util.ArrayList;

/**
 * created by Conelius on 1/9/2020 at 12:39 PM
 */
public interface AnswerListAsyncResponse {

    void processFinished(ArrayList<Question> questionArrayList);

}
