package cpe.top.quizz.asyncTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.StartQuizz;
import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.Evaluation;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.QuizzUtils;

/**
 * Created by Romain on 18/01/2017.
 */

public class GetEvalTask extends AsyncTask<Object, Integer, ReturnObject> {

    private static final String QUIZZ = "QUIZZ";

    private static final String TIMER = "TIMER";

    private static final String EVALUATIONID = "EVALUATIONID";

    private final static String USER = "USER";


    private Context mContext;

    private Quizz quizz;
    private User connectedUser;

    public GetEvalTask(Context context, User connectedUser, Quizz quizz){
        mContext = context;
        this.connectedUser = connectedUser;
        this.quizz = quizz;

    }

    @Override
    protected ReturnObject doInBackground(Object... params) {
        // To distinguish AsyncTask
        ReturnObject obj = QuizzUtils.getEvalForPseudoAndQuizzId(connectedUser.getPseudo(), quizz.getId());

        return obj;
    }

    @Override
    protected void onPostExecute(ReturnObject result) {
        Evaluation evaluation = (Evaluation) result.getObject();

        Intent myIntent = new Intent(mContext, StartQuizz.class);
        myIntent.putExtra(QUIZZ, quizz);
        myIntent.putExtra(USER, connectedUser);
        myIntent.putExtra(TIMER, evaluation.getTimer());
        myIntent.putExtra(EVALUATIONID, evaluation.getId());
        mContext.startActivity(myIntent);

    }
}
