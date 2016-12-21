package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.Utils;

/**
 * Created by Romain on 19/12/2016.
 */

public class ShowQuestionTask extends AsyncTask<String, Integer, ReturnObject> {
    public AsyncUserResponse delegate=null;

    public ShowQuestionTask(AsyncUserResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected ReturnObject doInBackground(String... params) {
        ReturnObject u = Utils.getQuestionsByThemes(params[0], params[1]);
        return (u != null) ? u : null;
    }

    @Override
    protected void onPostExecute(ReturnObject result) {
        delegate.processFinish(result);
    }
}
