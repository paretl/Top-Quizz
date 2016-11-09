package cpe.top.quizz.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import cpe.top.quizz.beans.User;

/**
 * @author Donatien
 * @version 0.1
 * @since 07/11/2016
 */

public class UserUtils extends JsonParser {

    public UserUtils() {

    }

    public static User getUser(String pseudo) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", pseudo);
        JSONObject obj = getJSONFromUrl("user/get/", key);

        try {
            User u = null;
            if (obj != null) {
                return new User(obj.getString("pseudo"), obj.getString("mail"), obj.getString("password"));
            }
            return u;
        } catch (JSONException e) {
            return null;
        }
    }

    public static User userExist(String pseudo, String password) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", pseudo);
        key.put("password", password);
        JSONObject obj = getJSONFromUrl("user/get/", key);

        try {
            User u = null;
            if (obj != null) {
                u = new User(obj.getString("pseudo"), obj.getString("mail"), obj.getString("password"));
            }
            return u;
        } catch (JSONException e) {
            Log.e("JSON", "", e);
            return null;
        }
    }

    public static User getByMail(String mail) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("mail", mail);
        JSONObject obj = getJSONFromUrl("user/getByMail/", key);

        try {
            User u = null;
                if (obj != null) {
                    u = new User(obj.getString("pseudo"), obj.getString("mail"), obj.getString("password"));
                }
                return u;
        } catch (JSONException e) {
            Log.e("JSON", "", e);
            return null;
        }
    }

    public static User addUser(User u) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", u.getPseudo());
        key.put("mail", u.getMail());
        key.put("password", u.getPassword());
        JSONObject obj = getJSONFromUrl("user/add/", key);

        try {
            User result = null;
            if (obj != null) {
                result = new User(obj.getString("pseudo"), obj.getString("mail"), obj.getString("password"));
            }
            return result;
        } catch (JSONException e) {
            Log.e("JSON", "", e);
            return null;
        }
    }

    public static boolean checkNewUser(String pseudo, String mail) {

        if (getUser(pseudo) != null) {
            return false;
        }
        if (getByMail(mail) != null) {
            return false;
        }
        return true;
    }
}
