package cpe.top.quizz.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import cpe.top.quizz.beans.User;

/**
 *
 * @author Donatien
 * @since 07/11/2016
 * @version 0.1
 */

public class UserUtils extends JsonParser {

    public UserUtils(){

    }

    public static User getUser(String pseudo){
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", pseudo);
        JSONObject obj = getJSONFromUrl("user/get/",key);

        try{
            return new User(obj.getString("pseudo"),obj.getString("mail"),obj.getString("password"));
        }catch (JSONException e){
            return null;
        }
    }

    public static User userExist(String pseudo, String password){
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", pseudo);
        key.put("password", password);
        JSONObject obj = getJSONFromUrl("user/get/",key);

        try{
            User u = null;
            if(obj != null){
                u = new User(obj.getString("pseudo"), obj.getString("mail"), obj.getString("password"));
            }
            return u ;
        }catch (JSONException e){
            Log.e("JSON", "",e);
            return null;
        }
    }
}
