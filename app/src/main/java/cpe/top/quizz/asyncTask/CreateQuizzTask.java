package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import cpe.top.quizz.asyncTask.responses.AsyncQuestionResponse;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.QuizzUtils;

/**
 * Created by lparet on 21/12/16.
 */

public class CreateQuizzTask extends AsyncTask<Quizz, Integer, ReturnObject> {
    public AsyncQuestionResponse delegate = null;

    public CreateQuizzTask(AsyncQuestionResponse asyncResponse) {
        delegate = asyncResponse;
    }

    protected ReturnObject doInBackground(Quizz... params) {
        ReturnObject u = QuizzUtils.addQuizz(params[0]);
        return (u != null) ? u : null;
    }

    protected void onPostExecute(ReturnObject result) {
        delegate.processFinish(result);
    }
}