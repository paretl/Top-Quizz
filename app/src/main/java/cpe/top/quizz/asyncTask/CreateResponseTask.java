package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.Response;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.UserUtils;

/**
 * Created by lparet on 06/12/16.
 */

public class CreateResponseTask extends AsyncTask<Object, Integer, List<ReturnObject>> {
    public AsyncResponse delegate = null;

    public CreateResponseTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected List<ReturnObject> doInBackground(Object... params) {
        List<ReturnObject> lR = new ArrayList<ReturnObject>();
        // To distinguish AsyncTask
        ReturnObject infoTask = new ReturnObject();
        infoTask.setCode(ReturnCode.ERROR_000);
        infoTask.setObject(new String("QUESTION_TASK"));
        lR.add(infoTask);

        int i;
        String pseudo = (String) params[1];
        ReturnObject u = null;
        for (i = 0; i < 4; i++) {
            Response r = ((ArrayList<Response>) params[0]).get(i);
            u = UserUtils.addResponse((i + 1), r, pseudo);
        }
        lR.add(u);



        return (lR != null && lR.size() != 0) ? lR : null;
    }

    @Override
    protected void onPostExecute(List<ReturnObject> result) {
        delegate.processFinish(result);
    }
}