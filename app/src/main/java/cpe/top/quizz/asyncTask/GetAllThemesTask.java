package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.UserUtils;

/**
 *
 * @author Louis
 * @since 08/12/2016
 * @version 0.1
 */

public class GetAllThemesTask extends AsyncTask<Void, Void, List<ReturnObject>> {
    public AsyncResponse delegate=null;

    public GetAllThemesTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected List<ReturnObject> doInBackground(Void... params) {
        List<ReturnObject> lR = new ArrayList<ReturnObject>();
        // To distinguish AsyncTask
        ReturnObject infoTask = new ReturnObject();
        infoTask.setCode(ReturnCode.ERROR_000);
        infoTask.setObject(new String("THEME_TASK"));
        lR.add(infoTask);


        ReturnObject u = UserUtils.getAllThemes();
        lR.add(u);



        return (lR != null && lR.size() != 0) ? lR : null;
    }

    @Override
    protected void onPostExecute(List<ReturnObject> result) {
        delegate.processFinish(result);
    }
}