package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.UserUtils;

/**
 *
 * @author Louis
 * @since 08/12/2016
 * @version 0.1
 */

<<<<<<< 8ae41cd188d5429d65f1a0b89cfe7455ce61997e
public class GetAllThemesTask extends AsyncTask<Void, Void, ReturnObject> {
    public AsyncUserResponse delegate=null;

=======
public class GetAllThemesTask extends AsyncTask<Void, Void, ReturnObject>

{
    public AsyncUserResponse delegate=null;


>>>>>>> [LPT][sw_Question] Take themes on BD and pass to createQuestion activity (only one theme yet)
    public GetAllThemesTask(AsyncUserResponse asyncResponse) {
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