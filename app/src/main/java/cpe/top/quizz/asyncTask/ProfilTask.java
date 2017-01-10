package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncProfilResponse;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.utils.QuizzUtils;
import cpe.top.quizz.utils.UserUtils;

/**
 * @author Maxence Royer
 * @since 04/01/2017
 * @version 1.0
 */
public class ProfilTask extends AsyncTask<String, Integer, List<ReturnObject>> {

    private static final String PROFIL_TASK = "PROFIL_TASK";

    public AsyncProfilResponse delegate = null;

    private String name;

    public ProfilTask(AsyncProfilResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected List<ReturnObject> doInBackground(String... params) {
        List<ReturnObject> listReturnObject = new ArrayList<ReturnObject>();
        // To distinguish AsyncTask
        ReturnObject infoTask = new ReturnObject();
        infoTask.setCode(ReturnCode.ERROR_000);
        infoTask.setObject(PROFIL_TASK);
        listReturnObject.add(infoTask);

        // The friend
        ReturnObject userTask = new ReturnObject();
        ReturnObject user = UserUtils.getUser(params[0]);
        userTask.setObject(((ReturnObject) user).getObject());
        userTask.setCode(((ReturnObject) user).getCode());
        listReturnObject.add(userTask);

        // All quizz of the friend
        ReturnObject allQuizz = QuizzUtils.getAllQuizzByUser(params[0]);
        listReturnObject.add(allQuizz);

        return (listReturnObject != null && listReturnObject.size() != 0) ? listReturnObject : null;
    }

    @Override
    protected void onPostExecute(List<ReturnObject> result) {
        delegate.processFinish(result);
    }
}