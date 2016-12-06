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

public class CreateResponseTask extends AsyncTask<ArrayList<Response>, Integer, ReturnObject> {
    public AsyncQuestionResponse delegate=null;

    public CreateResponseTask(AsyncQuestionResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected ReturnObject doInBackground(ArrayList<Response>... params) {
        int i;
        ReturnObject u = null;
        for(i =0; i<5; i++) {
            u = UserUtils.addResponse(params[0].get(i));
        }
        return (u != null) ? u : null;
    }

    @Override
    protected void onPostExecute(ReturnObject result) {
        delegate.processFinish(result);
    }
}
