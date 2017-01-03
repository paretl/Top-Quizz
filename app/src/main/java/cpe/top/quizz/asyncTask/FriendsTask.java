package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncFriendsResponse;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.FriendsUtils;
import cpe.top.quizz.utils.QuizzUtils;

/**
 * @author Maxence Royer
 * @since 21/11/2016
 * @version 0.1
 */
public class FriendsTask extends AsyncTask<String, Integer, List<ReturnObject>> {
    public AsyncFriendsResponse delegate = null;

    private String name;

    public FriendsTask(AsyncFriendsResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected List<ReturnObject> doInBackground(String... params) {
        List<ReturnObject> lR = new ArrayList<ReturnObject>();
        // To distinguish AsyncTask
        ReturnObject infoTask = new ReturnObject();
        infoTask.setCode(ReturnCode.ERROR_000);
        infoTask.setObject(new String("FRIENDS_TASK"));
        lR.add(infoTask);

        // The list of friends
        //ReturnObject u = FriendsUtils.getAllFriendsByUser(params[0]);
        lR.add(u);

        return (lR != null && lR.size() != 0) ? lR : null;
    }

    @Override
    protected void onPostExecute(List<ReturnObject> result) {
        delegate.processFinish(result);
    }
}