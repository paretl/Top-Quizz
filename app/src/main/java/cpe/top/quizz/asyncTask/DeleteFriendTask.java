package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncFriendsResponse;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.UserUtils;

/**
 * @author Maxence Royer
 * @since 09/01/2017
 * @version 1.0
 */
public class DeleteFriendTask extends AsyncTask<String, Integer, List<ReturnObject>> {

    private static final String FRIENDS_DEL = "FRIENDS_DEL";

    public AsyncFriendsResponse delegate = null;

    public DeleteFriendTask(AsyncFriendsResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected List<ReturnObject> doInBackground(String... params) {
        List<ReturnObject> lR = new ArrayList<ReturnObject>();

        // To distinguish AsyncTask
        ReturnObject infoTask = new ReturnObject();
        infoTask.setCode(ReturnCode.ERROR_000);
        infoTask.setObject(FRIENDS_DEL);
        lR.add(infoTask);

        ReturnObject userDeleteTask = new ReturnObject();
        ReturnObject returnDel = UserUtils.deleteUserFriend(params[0], params[1]);
        userDeleteTask.setCode(((ReturnObject) returnDel).getCode());
        lR.add(userDeleteTask);

        return (lR != null && lR.size() != 0) ? lR : null;
    }

    @Override
    protected void onPostExecute(List<ReturnObject> result) {
        delegate.processFinish(result);
    }
}