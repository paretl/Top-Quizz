package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.QuizzUtils;

/**
 * Created by lparet on 06/12/16.
 */

public class CreateEvalTask extends AsyncTask<String, Integer, ReturnObject> {
    public AsyncResponse delegate=null;

    public CreateEvalTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected ReturnObject doInBackground(String... params) {
        ReturnObject u = QuizzUtils.createEval(params[0], params[1], params[2], params[3], params[4], params[5]);
        return (u != null) ? u : null;
    }

    @Override
    protected void onPostExecute(ReturnObject result) {
        delegate.processFinish(result);
    }
}
