package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.UserUtils;

/**
 * Created by lparet on 19/12/16.
 */

public class GetQuestionsByThemesAndUserTask extends AsyncTask<Object, Integer, List<ReturnObject>> {
    public AsyncResponse delegate=null;

    public GetQuestionsByThemesAndUserTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
    }

    @Override
    protected List<ReturnObject> doInBackground(Object... params) {
        List<ReturnObject> lR = new ArrayList<ReturnObject>();
        // To distinguish AsyncTask
        ReturnObject infoTask = new ReturnObject();
        infoTask.setCode(ReturnCode.ERROR_000);
        infoTask.setObject(new String("QUIZZ_TASK"));
        lR.add(infoTask);

        User user = (User) params[0];
        ArrayList<Theme> themes = (ArrayList<Theme>) params[1];
        ReturnObject u = UserUtils.getQuestionsByThemesAndUser(user, themes);
        lR.add(u);



        return (lR != null && lR.size() != 0) ? lR : null;
    }

    @Override
    protected void onPostExecute(List<ReturnObject> result) {
        delegate.processFinish(result);
    }
}
