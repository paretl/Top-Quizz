package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.UserUtils;

/**
 * Created by lparet on 08/01/17.
 */

public class AddFriendTask extends AsyncTask<String, Void, ReturnObject> {
    public AsyncUserResponse delegate=null;

    public AddFriendTask(AsyncUserResponse asyncResponse) {
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
