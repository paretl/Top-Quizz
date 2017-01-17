package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.UserUtils;

/**
 * Created by lparet on 08/01/17.
 */

public class AddFriendTask extends AsyncTask<String, Void, ReturnObject> {
    public AsyncResponse delegate=null;

    public AddFriendTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected ReturnObject doInBackground(String... params) {
        ReturnObject u = UserUtils.addFriend(params[0], params[1]);
        return (u != null) ? u : null;
    }

    @Override
    protected void onPostExecute(ReturnObject result) {
        delegate.processFinish(result);
    }
}
