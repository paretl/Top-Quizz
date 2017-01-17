package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.StatisticUtils;

/**
 * @author Maxence Royer
 * @since 25/11/2016
 * @version 0.1
 */
public class StatisticTask extends AsyncTask<String, Integer, List<ReturnObject>> {
    public AsyncResponse delegate=null;


    public StatisticTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected List<ReturnObject> doInBackground(String... params) {
        List<ReturnObject> lR = new ArrayList<ReturnObject>();
        // To distinguish AsyncTask
        ReturnObject infoTask = new ReturnObject();
        infoTask.setCode(ReturnCode.ERROR_000);
        infoTask.setObject(new String("STATISTICS_TASKS"));
        lR.add(infoTask);

        // Ten lasts scores
        ReturnObject u = StatisticUtils.getTenLastScoreForQuizz(params[0], params[1]);
        lR.add(u);

        return (lR != null && lR.size() != 0) ? lR : null;
    }

    @Override
    protected void onPostExecute(List<ReturnObject> result) {
        delegate.processFinish(result);
    }
}