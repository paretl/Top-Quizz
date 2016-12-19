package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;

import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.UserUtils;

/**
 * Created by lparet on 19/12/16.
 */

public class GetQuestionsByThemesAndUserTask extends AsyncTask<Object, Integer, ReturnObject> {
    public AsyncUserResponse delegate=null;

    public GetQuestionsByThemesAndUserTask(AsyncUserResponse asyncResponse) {
            delegate = asyncResponse;
    }

    @Override
    protected ReturnObject doInBackground(Object... params) {
        User user = (User) params[0];
        ArrayList<Theme> themes = (ArrayList<Theme>) params[1];
        ReturnObject u = UserUtils.getQuestionsByThemesAndUser(user, themes);
        return (u != null) ? u : null;
    }

    @Override
    protected void onPostExecute(ReturnObject result) {
        delegate.processFinish(result);
    }
}
