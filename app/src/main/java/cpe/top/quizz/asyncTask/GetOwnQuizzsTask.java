package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.QuizzUtils;
import cpe.top.quizz.utils.StatisticUtils;

/**
 *
 * @author Louis
 * @since 17/01/2017
 * @version 0.1
 */

public class GetOwnQuizzsTask extends AsyncTask<String, Integer, ReturnObject> {
    public AsyncResponse delegate=null;

    public GetOwnQuizzsTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected ReturnObject doInBackground(String... params) {
        ReturnObject u = QuizzUtils.getOwnQuizzByUser(params[0]);
        return (u != null) ? u : null;
    }

    @Override
    protected void onPostExecute(ReturnObject result) {
        delegate.processFinish(result);
    }
}