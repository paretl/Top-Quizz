package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.QuizzUtils;

/**
 * @author Donatien Gascoin
 * @since 22/12/2016
 * @version 0.1
 */

public class SaveScoreTask extends AsyncTask<String, Integer, List<ReturnObject>>  {
    public AsyncResponse delegate=null;

    private String pseudo, password;

    public SaveScoreTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected List<ReturnObject> doInBackground(String... params) {
        List<ReturnObject> lR = new ArrayList<ReturnObject>();
        // To distinguish AsyncTask
        ReturnObject infoTask = new ReturnObject();
        infoTask.setCode(ReturnCode.ERROR_000);
        infoTask.setObject(new String("SCORE_TASK"));
        lR.add(infoTask);
        //0: pseudo, 1: quizzId, 2: quizz name, 3: right answers, 4: nbQuestion
        ReturnObject u = QuizzUtils.saveScore(params[0], params[1], params[2], params[3], params[4]);

        //params[5] = evaluationId != -1 -> evaluation mode
        if(Integer.parseInt(params[5]) != -1){
            QuizzUtils.makeEvaluationDone(params[0], params[5]);
        }

        // Used to return the quizzes of the User from the API
        lR.add(u);



        return (lR != null && lR.size() != 0) ? lR : null;
    }

    @Override
    protected void onPostExecute(List<ReturnObject> result) {
        delegate.processFinish(result);
    }


}