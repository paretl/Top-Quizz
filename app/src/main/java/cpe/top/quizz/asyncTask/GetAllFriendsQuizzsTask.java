package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.QuizzUtils;

/**
 *
 * @author Louis
 * @since 04/01/2016
 * @version 0.1
 */

public class GetAllFriendsQuizzsTask extends AsyncTask<String, Integer, ReturnObject> {
    public AsyncResponse delegate=null;

    public GetAllFriendsQuizzsTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected ReturnObject doInBackground(String... params) {
        ReturnObject u = QuizzUtils.getAllFriendsQuizzs(params[0]);
        return (u != null) ? u : null;
    }

    protected void onPostExecute(ReturnObject result) {
        delegate.processFinish(result);
    }
}