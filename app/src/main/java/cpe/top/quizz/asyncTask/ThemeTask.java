package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.Utils;

/**
 *
 * @author Camille
 * @since 25/11/2016
 * @version 0.1
 */

public class ThemeTask extends AsyncTask<String, Integer, ReturnObject>

{
    public AsyncUserResponse delegate=null;


    public ThemeTask(AsyncUserResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected ReturnObject doInBackground(String... params) {
        ReturnObject u = Utils.getAllThemeByUser(params[0]);
        return (u != null) ? u : null;
    }

    @Override
    protected void onPostExecute(ReturnObject result) {
        delegate.processFinish(result);
    }
}