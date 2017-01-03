package cpe.top.quizz.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.Response;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;

/**
 * @author Maxence Royer
 * @version 1.0
 * @since 03/01/2016
 */
public class FriendsUtils extends JsonParser {

    public FriendsUtils() {

    }

    @Nullable
    public static List<User> getAllFriendsByUser(String pseudo) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", pseudo);
        JSONObject obj = getJSONFromUrl("friends/getAllFriendsByPseudo/", key);

        try {
            /**List<User> listFriends = new ArrayList<User>();
            if (obj != null) {

            }*/
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
