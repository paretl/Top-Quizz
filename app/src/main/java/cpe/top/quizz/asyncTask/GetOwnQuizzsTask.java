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

public class GetOwnQuizzsTask extends AsyncTask<String, Integer, List<ReturnObject>> {
    public AsyncResponse delegate=null;

    public GetOwnQuizzsTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected List<ReturnObject> doInBackground(String... params) {
        List<ReturnObject> lR = new ArrayList<ReturnObject>();
        // To distinguish AsyncTask
        ReturnObject infoTask = new ReturnObject();
        infoTask.setCode(ReturnCode.ERROR_000);
        infoTask.setObject(new String("QUIZZ_TASK"));
        lR.add(infoTask);

        ReturnObject u = QuizzUtils.getOwnQuizzByUser(params[0]);
        lR.add(u);



        return (lR != null && lR.size() != 0) ? lR : null;
    }

    @Override
    protected void onPostExecute(List<ReturnObject> result) {
        delegate.processFinish(result);
    }
}