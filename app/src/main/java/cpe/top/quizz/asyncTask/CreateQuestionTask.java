package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import cpe.top.quizz.asyncTask.responses.AsyncQuestionResponse;
import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.UserUtils;

/**
 * Created by lparet on 06/12/16.
 */

public class CreateQuestionTask extends AsyncTask<Question, Integer, ReturnObject> {
    public AsyncQuestionResponse delegate = null;

    public CreateQuestionTask(AsyncQuestionResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected ReturnObject doInBackground(Question... params) {
        ReturnObject u = UserUtils.addQuestion(params[0]);
        return (u != null) ? u : null;
    }

    @Override
    protected void onPostExecute(ReturnObject result) {
        delegate.processFinish(result);
    }
}
