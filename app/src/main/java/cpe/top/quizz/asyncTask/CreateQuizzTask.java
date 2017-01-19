package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.QuizzUtils;

/**
 * Created by lparet on 21/12/16.
 */

public class CreateQuizzTask extends AsyncTask<Quizz, Integer, List<ReturnObject>> {
    public AsyncResponse delegate = null;

    public CreateQuizzTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    protected List<ReturnObject> doInBackground(Quizz... params) {
        List<ReturnObject> lR = new ArrayList<ReturnObject>();
        // To distinguish AsyncTask
        ReturnObject infoTask = new ReturnObject();
        infoTask.setCode(ReturnCode.ERROR_000);
        infoTask.setObject(new String("QUIZZ_TASK"));
        lR.add(infoTask);

        ReturnObject u = QuizzUtils.addQuizz(params[0]);
        lR.add(u);



        return (lR != null && lR.size() != 0) ? lR : null;
    }

    protected void onPostExecute(List<ReturnObject> result) {
        delegate.processFinish(result);
    }
}
