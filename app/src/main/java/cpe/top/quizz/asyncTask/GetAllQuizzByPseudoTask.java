package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncStatisticResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.QuizzUtils;

/**
 *
 * @author Louis
 * @since 04/01/2016
 * @version 0.1
 */

public class GetAllQuizzByPseudoTask extends AsyncTask<String, Integer, ReturnObject> {
    public AsyncStatisticResponse delegate=null;

    public GetAllQuizzByPseudoTask(AsyncStatisticResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected ReturnObject doInBackground(String... params) {
        ReturnObject u = QuizzUtils.getAllFriendsByPseudo(params[0]);
        return (u != null) ? u : null;
    }

    protected void onPostExecute(List<ReturnObject> result) {
        delegate.processFinish(result);
    }
}