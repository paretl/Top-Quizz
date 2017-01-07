package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncQuizzResponse;
import cpe.top.quizz.asyncTask.responses.AsyncStatisticResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.QuizzUtils;

/**
 *
 * @author Louis
 * @since 04/01/2016
 * @version 0.1
 */

public class GetAllFriendsQuizzsTask extends AsyncTask<String, Integer, ReturnObject> {
    public AsyncQuizzResponse delegate=null;

    public GetAllFriendsQuizzsTask(AsyncQuizzResponse asyncResponse) {
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