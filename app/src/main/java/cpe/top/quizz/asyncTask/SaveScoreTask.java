package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.QuizzUtils;
import cpe.top.quizz.utils.UserUtils;

/**
 * @author Donatien Gascoin
 * @since 22/12/2016
 * @version 0.1
 */

public class SaveScoreTask extends AsyncTask<String, Integer, ReturnObject>  {
    public AsyncUserResponse delegate=null;

    private String pseudo, password;

    public SaveScoreTask(AsyncUserResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected ReturnObject doInBackground(String... params) {
        //0: pseudo, 1: quizzId, 2: quizz name, 3: right answers, 4: nbQuestion
        ReturnObject u = QuizzUtils.saveScore(params[0], params[1], params[2], params[3], params[4]);
        // Used to return the quizzes of the User from the API
        return (u != null) ? u : null;
    }

    @Override
    protected void onPostExecute(ReturnObject result) {
        delegate.processFinish(result);
    }
}