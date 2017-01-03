package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncStatisticResponse;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.QuizzUtils;
import cpe.top.quizz.utils.StatisticUtils;

/**
 *
 * @author Donatien
 * @since 27/12/2016
 * @version 0.1
 */

public class GetAllQuizzsTask extends AsyncTask<String, Integer, List<ReturnObject>> {
    public AsyncStatisticResponse delegate=null;

    public GetAllQuizzsTask(AsyncStatisticResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected List<ReturnObject> doInBackground(String... params) {
        List<ReturnObject> lR = new ArrayList<ReturnObject>();

        // Ten lasts scores
        /*if(params[0]!=null) {
            // To distinguish AsyncTask
            ReturnObject infoTask = new ReturnObject();
            infoTask.setCode(ReturnCode.ERROR_000);
            infoTask.setObject(new String("QUIZZS_TASKS"));
            lR.add(infoTask);
            ReturnObject u = QuizzUtils.getAllQuizzByUser(params[0]);
            lR.add(u);
        } else {
            ReturnObject u = QuizzUtils.getAllQuizzByUser(params[0]);
            lR.add(u);
        }*/

        // Ten lasts scores
        if("Maxence".equals(params[0])) {
            System.out.println("OK");
            ReturnObject u = QuizzUtils.getAllQuizzByUser(params[0]);
            lR.add(u);
        } else {
            // To distinguish AsyncTask
            ReturnObject infoTask = new ReturnObject();
            infoTask.setCode(ReturnCode.ERROR_000);
            infoTask.setObject(new String("QUIZZS_TASKS"));
            lR.add(infoTask);
            ReturnObject u = QuizzUtils.getAllQuizzByUser(params[0]);
            lR.add(u);
        }


        return (lR != null && lR.size() != 0) ? lR : null;
    }

    @Override
    protected void onPostExecute(List<ReturnObject> result) {
        delegate.processFinish(result);
    }
}