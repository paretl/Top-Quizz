package cpe.top.quizz.asyncTask;

import android.os.AsyncTask;

import java.util.Random;

import javax.mail.MessagingException;

import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.utils.Mail;
import cpe.top.quizz.utils.UserUtils;
import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.ReturnObject;

/**
 *
 * @author Louis
 * @since 21/11/2016
 * @version 0.1
 */

public class ChangePasswordTask extends AsyncTask<String, Integer, ReturnObject>

{
    public AsyncUserResponse delegate=null;

    final String newPassword = generatePassword();

    final String subject = "Top Quizz - Nouveau mot de passe";
    final String body = "Bonjour,\n\nVotre nouveau mot de passe est : " + newPassword + "\n\nA bientôt sur Top Quizz";

    public ChangePasswordTask(AsyncUserResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected ReturnObject doInBackground(String... params) {

        String mail = params[0];
        ReturnObject u = new ReturnObject();
        try {
            u = UserUtils.changePassword(newPassword, mail);
            // Send email
            Mail.sendEmail(mail, subject, body);
            UserUtils.changePassword(newPassword,mail);
        } catch (MessagingException em) {
            u.setCode(ReturnCode.ERROR_050);
        }
        return (u != null) ? u : null;
    }

    // Method that create a random password of 10 str
    private String generatePassword() {
        String str = "ABCDEFGHJKLMNOPQRSTUVWXYZ123456789";
        String ar="";
        Random r = new Random();
        int te=0;
        for(int i=1;i<=6;i++){
            te=r.nextInt(34);
            ar=ar+str.charAt(te);
        }
        System.out.println(ar);
        return ar;
    }

    @Override
    protected void onPostExecute(ReturnObject result) {
        delegate.processFinish(result);
    }
}