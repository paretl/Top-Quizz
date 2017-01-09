package cpe.top.quizz.utils;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
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
    public static ReturnObject getAllFriendsByUser(String pseudo) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", pseudo);
        JSONObject jsonFriends = getJSONFromUrl("user/getAllFriendsByPseudo/", key);
        ReturnObject rO = new ReturnObject();

        try {
            ReturnCode rC = ReturnCode.valueOf((String) jsonFriends.get("code"));
            Collection<User> userFriends = new ArrayList<User>();

            if (rC.equals(ReturnCode.ERROR_000)) {
                if (jsonFriends.get("object") instanceof JSONArray) {
                    JSONArray jObject = jsonFriends.getJSONArray("object");

                    for (int i=0; i< jObject.length(); i++) {
                        JSONObject currentElement = jObject.getJSONObject(i);

                        if (currentElement.get("quizz") != null && currentElement.get("quizz") instanceof JSONArray && currentElement.getJSONArray("quizz") != null) {
                            JSONArray quizzArray = currentElement.getJSONArray("quizz");
                            Collection<Quizz> quizz = null;
                            if (quizzArray.length() != 0) {
                                quizz = ParseUtils.getQuizzsFromJsonArray(quizzArray);
                            }
                            userFriends.add(new User(currentElement.getString("pseudo"), currentElement.getString("mail"), quizz));
                        } else {
                            userFriends.add(new User(currentElement.getString("pseudo"), currentElement.getString("mail"), null));
                        }
                    }
                }

                rO.setObject(userFriends);
            }

            rO.setCode(rC);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return (rO != null) ? rO : null;
    }
}
