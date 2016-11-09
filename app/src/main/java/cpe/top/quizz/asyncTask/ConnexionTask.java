package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import cpe.top.quizz.MainActivity;
import cpe.top.quizz.Utils.UserUtils;
import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.User;

/**
 *
 * @author Donatien
 * @since 08/11/2016
 * @version 0.1
 */

public class ConnexionTask extends AsyncTask<String, Integer, User>

{
    public AsyncUserResponse delegate=null;

    private String pseudo, password;

    public ConnexionTask() {
    }

    @Override
    protected User doInBackground(String... params) {
        User u = UserUtils.userExist(params[0], params[1]);
        return (u != null) ? u : null;
    }

    @Override
    protected void onPostExecute(User result) {
        delegate.processFinish(result);
    }
}