package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.UserUtils;

/**
 *
 * @author Louis
 * @since 08/12/2016
 * @version 0.1
 */

public class GetAllThemesTask extends AsyncTask<Void, Void, ReturnObject> {
    public AsyncResponse delegate=null;

    public GetAllThemesTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected ReturnObject doInBackground(Void... params) {
        ReturnObject u = UserUtils.getAllThemes();
        return (u != null) ? u : null;
    }

    @Override
    protected void onPostExecute(ReturnObject result) {
        delegate.processFinish(result);
    }
}