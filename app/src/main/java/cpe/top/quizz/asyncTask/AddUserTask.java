package cpe.top.quizz.asyncTask;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import junit.framework.Test;

import java.lang.ref.WeakReference;

import cpe.top.quizz.Inscription;
import cpe.top.quizz.InscriptionConfirm;
import cpe.top.quizz.Utils.UserUtils;
import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.User;

/**
 * @author Donatien
 * @version 0.1
 * @since 08/11/2016
 */

public class AddUserTask extends AsyncTask<String, Integer, User> {
    private WeakReference<Inscription> mActivity = null;

    public AsyncUserResponse delegate = null;

    public void link(Inscription pActivity) {
        mActivity = new WeakReference<Inscription>(pActivity);
    }

    public AddUserTask(Inscription pActivity) {
        link(pActivity);
    }

    /**
     * Using with:
     * params[0]: Pseudo
     * params[1]: Mail
     * params[2]: Password
     *
     * @param params
     * @return {@link User}
     */
    @Override
    protected User doInBackground(String... params) {
        User u = new User();

        u.setPseudo(params[0]);
        u.setMail(params[1]);
        u.setPassword(params[2]);

        /*
         *   Test if Pseudo or mail already exist
         *   If one exits, return existing User, else return the new User (make test to know if return is the right user)
         */

        User testPseudo = UserUtils.getUser(params[0]);
        if (u.getPseudo().equals(testPseudo.getPseudo())) {
            Log.w("[AddUser]", "Impossible to add User, Pseudo " + u.getPseudo() + " already exist");
            return testPseudo;
        }

        User testMail = UserUtils.getByMail(params[1]);
        if(u.getMail().equals(testMail.getMail())){
            Log.w("[AddUser]", "Impossible to add User, Mail " + u.getMail() + " already exist");
            return testPseudo;
        }

        // Add user
        User result = UserUtils.addUser(u);
        return (u != null) ? u : null;
    }

    @Override
    protected void onPostExecute(User result) {
        delegate.processFinish(result);
    }
}
