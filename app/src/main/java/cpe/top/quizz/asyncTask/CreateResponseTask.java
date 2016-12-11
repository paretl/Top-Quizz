package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;

import cpe.top.quizz.asyncTask.responses.AsyncQuestionResponse;
import cpe.top.quizz.beans.Response;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.UserUtils;

/**
 * Created by lparet on 06/12/16.
 */

public class CreateResponseTask extends AsyncTask<Object, Integer, ReturnObject> {
<<<<<<< dc60fb17cb3303b864edcc246bf76ed2524cf34d
<<<<<<< 8ae41cd188d5429d65f1a0b89cfe7455ce61997e
    public AsyncQuestionResponse delegate = null;
=======
    public AsyncQuestionResponse delegate=null;
>>>>>>> [LPT][sw_Question] Take themes on BD and pass to createQuestion activity (only one theme yet)
=======
    public AsyncQuestionResponse delegate = null;
>>>>>>> [LPT][sw_Question] Adapt Android to API and fix details

    public CreateResponseTask(AsyncQuestionResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected ReturnObject doInBackground(Object... params) {
        int i;
        String pseudo = (String) params[1];
        ReturnObject u = null;
<<<<<<< dc60fb17cb3303b864edcc246bf76ed2524cf34d
<<<<<<< 8ae41cd188d5429d65f1a0b89cfe7455ce61997e
        for (i = 0; i < 4; i++) {
            Response r = ((ArrayList<Response>) params[0]).get(i);
            u = UserUtils.addResponse((i + 1), r, pseudo);
=======
        for(i=0; i<5; i++) {
            Response r = ((ArrayList<Response>) params[0]).get(i);
            u = UserUtils.addResponse((i+1), r, pseudo);
>>>>>>> [LPT][sw_Question] Take themes on BD and pass to createQuestion activity (only one theme yet)
=======
        for (i = 0; i < 4; i++) {
            Response r = ((ArrayList<Response>) params[0]).get(i);
            u = UserUtils.addResponse((i + 1), r, pseudo);
>>>>>>> [LPT][sw_Question] Adapt Android to API and fix details
        }
        return (u != null) ? u : null;
    }

    @Override
    protected void onPostExecute(ReturnObject result) {
        delegate.processFinish(result);
    }
}
