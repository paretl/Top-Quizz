package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.EvaluationUtils;

/**
 * Created by Camille on 19/01/2017.
 */

public class EvaluationStatisticTask extends AsyncTask<String, Integer, ReturnObject>{
    public AsyncResponse delegate=null;

    public EvaluationStatisticTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected ReturnObject doInBackground(String... params) {
        ReturnObject u = EvaluationUtils.getEvaluationsStatisticsForEvaluatorPseudo(params[0], params[1]);
        return (u != null) ? u : null;
    }

    @Override
    protected void onPostExecute(ReturnObject result) {
        delegate.processFinish(result);
    }
}

