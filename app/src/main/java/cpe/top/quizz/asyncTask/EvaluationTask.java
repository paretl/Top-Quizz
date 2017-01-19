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

public class EvaluationTask extends AsyncTask<String, Integer, List<ReturnObject>> {
    public AsyncResponse delegate=null;


    public EvaluationTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected List<ReturnObject> doInBackground(String... params) {
        List<ReturnObject> lR = new ArrayList<ReturnObject>();
        // To distinguish AsyncTask
        ReturnObject infoTask = new ReturnObject();
        infoTask.setCode(ReturnCode.ERROR_000);
        infoTask.setObject(new String("EVALUATION_TASKS"));
        lR.add(infoTask);

        // Ten lasts scores
        ReturnObject u = EvaluationUtils.getEvaluationsForEvaluatorPseudo(params[0]);
        lR.add(u);

        return (lR != null && lR.size() != 0) ? lR : null;
    }

    @Override
    protected void onPostExecute(List<ReturnObject> result) {
        delegate.processFinish(result);
    }
}
