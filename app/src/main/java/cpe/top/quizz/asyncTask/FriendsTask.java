package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.FriendsUtils;

/**
 * @author Maxence Royer
 * @since 21/11/2016
 * @version 0.1
 */
public class FriendsTask extends AsyncTask<String, Integer, List<ReturnObject>> {

    private static final String FRIENDS_TASK = "FRIENDS_TASK";

    public AsyncResponse delegate = null;

    private String name;

    public FriendsTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected List<ReturnObject> doInBackground(String... params) {
        List<ReturnObject> listReturnObject = new ArrayList<ReturnObject>();
        // To distinguish AsyncTask
        ReturnObject infoTask = new ReturnObject();
        infoTask.setCode(ReturnCode.ERROR_000);
        infoTask.setObject(FRIENDS_TASK);
        listReturnObject.add(infoTask);

        // The list of friends
        ReturnObject returnObject = new ReturnObject();
        ReturnObject returned = (ReturnObject) FriendsUtils.getAllFriendsByUser(params[0]);
        returnObject.setCode((ReturnCode) returned.getCode());
        returnObject.setObject((List<User>) returned.getObject());
        listReturnObject.add(returnObject);

        // Add to return object
        listReturnObject.add(returnObject);

        return (listReturnObject != null && listReturnObject.size() != 0) ? listReturnObject : null;
    }

    @Override
    protected void onPostExecute(List<ReturnObject> result) {
        delegate.processFinish(result);
    }
}