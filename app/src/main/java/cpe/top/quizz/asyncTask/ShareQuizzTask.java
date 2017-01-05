package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import cpe.top.quizz.asyncTask.responses.AsyncQuestionResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.QuizzUtils;

/**
 * Created by lparet on 21/12/16.
 */

public class ShareQuizzTask extends AsyncTask<String, Integer, ReturnObject> {
    public AsyncQuestionResponse delegate = null;

    public ShareQuizzTask(AsyncQuestionResponse asyncResponse) {
        delegate = asyncResponse;
    }

    protected ReturnObject doInBackground(String... params) {
        // params[0] = pseudo, params[1] = id quizz
        ReturnObject u = QuizzUtils.shareQuizz(params[0], params[1]);
        return (u != null) ? u : null;
    }

    protected void onPostExecute(ReturnObject result) {
        delegate.processFinish(result);
    }
}