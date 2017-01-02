package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;

import cpe.top.quizz.asyncTask.responses.AsyncQuestionResponse;
import cpe.top.quizz.beans.Response;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.UserUtils;

/**
 * Created by lparet on 06/12/16.
 */

public class CreateResponseTask extends AsyncTask<Object, Integer, ReturnObject> {
    public AsyncQuestionResponse delegate = null;

    public CreateResponseTask(AsyncQuestionResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected ReturnObject doInBackground(Object... params) {
        int i;
        String pseudo = (String) params[1];
        ReturnObject u = null;
        for (i = 0; i < 4; i++) {
            Response r = ((ArrayList<Response>) params[0]).get(i);
            u = UserUtils.addResponse((i + 1), r, pseudo);
        }
        return (u != null) ? u : null;
    }

    @Override
    protected void onPostExecute(ReturnObject result) {
        delegate.processFinish(result);
    }
}