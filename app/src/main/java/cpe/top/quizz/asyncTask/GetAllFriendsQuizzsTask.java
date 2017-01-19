package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.QuizzUtils;

/**
 *
 * @author Louis
 * @since 04/01/2016
 * @version 0.1
 */

public class GetAllFriendsQuizzsTask extends AsyncTask<String, Integer, List<ReturnObject>> {
    public AsyncResponse delegate=null;

    public GetAllFriendsQuizzsTask(AsyncResponse asyncResponse) {
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

        ReturnObject u = QuizzUtils.getAllFriendsQuizzs(params[0]);
        lR.add(u);

        return (lR != null && lR.size() != 0) ? lR : null;
    }

    protected void onPostExecute(List<ReturnObject> result) {
        delegate.processFinish(result);
    }
}