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
 * @author Donatien
 * @version 0.1
 * @since 07/11/2016
 */

public class UserUtils extends JsonParser {

    public UserUtils() {

    }

    @Nullable
    public static User getUser(String pseudo) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", pseudo);
        JSONObject obj = getJSONFromUrl("user/get/", key);

        try {
            User u = null;
            if (obj != null) {
                u = getUserFromReturnObject(obj);
            }
            return u;
        } catch (JSONException e) {
            return null;
        }
    }

    @Nullable
    public static ReturnObject checkCredentials(String pseudo, String password) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", pseudo);
        key.put("password", password);
        JSONObject obj = getJSONFromUrl("user/checkCredentials/", key);
        ReturnObject object = new ReturnObject();

        try {
            object.setCode(ReturnCode.valueOf(obj.getString("code")));
            User u = null;
            if (obj != null) {
                u = getMinimumUserFromReturnObject(obj);
            } else {
                object.setCode(ReturnCode.ERROR_200);
            }
            object.setObject(u);
        } catch (RuntimeException e) {
            object.setCode(ReturnCode.ERROR_200);
            Log.e("Runtime", "", e);
        } catch (JSONException e) {
            Log.e("JSON", "", e);
            object.setCode(ReturnCode.ERROR_200);
        }
        return object;
    }



    @Nullable
    public static User getByMail(String mail) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("mail", mail);
        JSONObject obj = getJSONFromUrl("user/getByMail/", key);

        try {
            User u = null;
            if (obj != null) {
                u = new User(obj.getString("pseudo"), obj.getString("mail"), obj.getString("password"), null, null);
            }
            return u;
        } catch (JSONException e) {
            Log.e("JSON", "", e);
            return null;
        }
    }

    @Nullable
    public static ReturnObject addUser(User u) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", u.getPseudo());
        key.put("mail", u.getMail());
        key.put("password", u.getPassword());
        JSONObject obj = getJSONFromUrl("user/add/", key);
        ReturnObject object = new ReturnObject();

        try {
            object.setCode(ReturnCode.valueOf(obj.getString("code")));
            User user = null;
            if (obj != null) {
                u = getMinimumUserFromReturnObject(obj);
            }
            object.setObject(u);
        } catch (RuntimeException e) {
            object.setCode(ReturnCode.ERROR_200);
            Log.e("Runtime", "", e);
        } catch (JSONException e) {
            Log.e("JSON", "", e);
            object.setCode(ReturnCode.ERROR_200);
        }
        return object;
    }

    @Nullable
    public static ReturnObject changePassword(String password, String mail) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("password", password);
        key.put("mail", mail);
        JSONObject obj = getJSONFromUrl("user/changePassword/", key);
        ReturnObject object = new ReturnObject();
        try {
            object.setCode(ReturnCode.valueOf(obj.getString("code")));
            User u = null;
            if (obj != null) {
                u = getMinimumUserFromReturnObject(obj);
            }
            object.setObject(u);
        } catch (RuntimeException e) {
            object.setCode(ReturnCode.ERROR_200);
            Log.e("Runtime", "", e);
        } catch (JSONException e) {
            Log.e("JSON", "", e);
            object.setCode(ReturnCode.ERROR_200);
        }
        return object;
    }

    /**
     * Basic user with arguments:
     * - Pseudo
     * - Mail
     * - Other elements was set to null
     *
     * @return {@link User}
     * @throws JSONException
     */
    private static User getMinimumUserFromReturnObject(JSONObject obj) throws JSONException {
        //Get object variable in ReturnObject
        JSONObject jsonUser = obj.getJSONObject("object");

        return new User(jsonUser.getString("pseudo"), jsonUser.getString("mail"), null, null, null);
    }

    /**
     * User with all arguments:
     * Warning:
     * Depending on the JSON sent, the function can make an infinite loop.
     *
     * @return {@link User}
     * @throws JSONException
     */
    protected static User getUserFromReturnObject(JSONObject obj) throws JSONException {
        //Get object variable in ReturnObject
        JSONObject jsonUser = obj.getJSONObject("object");

        JSONArray friendsArray = jsonUser.getJSONArray("friends");
        Collection<User> friends = null;
        if (friendsArray.length() != 0) {
            friends = ParseUtils.getFriendsFromJsonArray(friendsArray);
        }

        JSONArray questionArray = jsonUser.getJSONArray("questions");
        Collection<Question> questions = null;
        if (questionArray.length() != 0) {
            questions = ParseUtils.getQuestionsFromJsonArray(questionArray);
        }

        return new User(jsonUser.getString("pseudo"), jsonUser.getString("mail"), null, friends, questions);
    }

    private static Collection<Question> getLabelQuestionsFromJsonArray(JSONArray questionsArray) throws JSONException {
        Collection<Question> questions = new ArrayList<>();
        if (questionsArray.length() != 0) {
            for (int i = 0; i < questionsArray.length(); i++) {

                JSONObject tmpObj = questionsArray.getJSONObject(i);

                Question questionTmp = new Question(tmpObj.getInt("id"),tmpObj.getString("label"), tmpObj.getString("explanation"), tmpObj.getString("pseudo"), null, null, null);
                questions.add(questionTmp);
            }
        }

        return questions;
    }

    public static ReturnObject getAllThemeByUser(String pseudo) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", pseudo);
        JSONObject obj = getJSONFromUrl("theme/getAllByUser/", key);
        ReturnObject object = new ReturnObject();
        try {
            object.setCode(ReturnCode.valueOf(obj.getString("code")));
            Collection<Theme> t = null;
            if (!obj.isNull("object")) {
                t = ParseUtils.getThemesFromJsonArray(obj.getJSONArray("object"));
            }
            object.setObject(t);
        } catch (RuntimeException e) {
            object.setCode(ReturnCode.ERROR_200);
            Log.e("Runtime", "", e);
        } catch (JSONException e) {
            Log.e("JSON", "", e);
            object.setCode(ReturnCode.ERROR_200);
        }
        return object;
    }
    
    /**
     * Add question
     * <p>
     * A question has a pseudo, a label, an explanation and a collection of themes
     *
     * @return @return {@link ReturnObject}
     * @throws JSONException
     */
    @Nullable
    public static ReturnObject addQuestion(Question q) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", q.getPseudo());
        key.put("label", q.getLabel());
        List<Theme> myThemes = (List<Theme>) q.getThemes();
        String themes = "";
        // List of themes (with names)
        for(Theme t : myThemes) {
            if("".equals(themes)) {
                themes = t.getName();
            } else {
                themes = themes + "|" + t.getName();
            }
        }
        key.put("themes", themes);
        key.put("explanation", q.getExplanation());

        JSONObject obj = getJSONFromUrl("question/add/", key);

        ReturnObject object = new ReturnObject();
        try {
            object.setCode(ReturnCode.valueOf(obj.getString("code")));
            object.setObject(q);
        } catch (RuntimeException e) {
            object.setCode(ReturnCode.ERROR_200);
            Log.e("Runtime", "", e);
        } catch (JSONException e) {
            Log.e("JSON", "", e);
            object.setCode(ReturnCode.ERROR_200);
        } catch (Exception e) {
            Log.e("JSON", "", e);
            object.setCode(ReturnCode.ERROR_050);
        }
        return object;
    }

    /**
     * Add question
     * <p>
     * A response has a pseudo, a number, a label and boolean to know if it's the good response
     *
     * @return @return {@link ReturnObject}
     * @throws JSONException
     */
    @Nullable
    public static ReturnObject addResponse(int number, Response r, String pseudo) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("number", Integer.toString(number));
        key.put("pseudo", pseudo);
        key.put("label", r.getLabel());
        key.put("isValide", r.getValide().toString());

        System.out.println(key);
        JSONObject obj = getJSONFromUrl("response/addTmpResponse/", key);

        ReturnObject object = new ReturnObject();
        try {
            object.setCode(ReturnCode.valueOf(obj.getString("code")));
            object.setObject(r);
        } catch (RuntimeException e) {
            object.setCode(ReturnCode.ERROR_200);
            Log.e("Runtime", "", e);
        } catch (JSONException e) {
            Log.e("JSON", "", e);
            object.setCode(ReturnCode.ERROR_200);
        } catch (Exception e) {
            Log.e("JSON", "", e);
            object.setCode(ReturnCode.ERROR_050);
        }
        return object;
    }

    /**
     * Get ALL themes from BD - no duplicate themes
     *
     * @return @return {@link ReturnObject}
     * @throws JSONException
     */
    public static ReturnObject getAllThemes() {
        Map<String, String> key = new LinkedHashMap<>();
        JSONObject obj = getJSONFromUrl("theme/getAllThemes/", key);
        ReturnObject object = new ReturnObject();
        try {
            object.setCode(ReturnCode.valueOf(obj.getString("code")));
            Collection<Theme> t = null;
            if (obj != null && obj.has("object")) {
                t = ParseUtils.getThemesFromJsonArray(obj.getJSONArray("object"));
            }
            object.setObject(t);
        } catch (RuntimeException e) {
            object.setCode(ReturnCode.ERROR_200);
            Log.e("Runtime", "", e);
        } catch (JSONException e) {
            Log.e("JSON", "", e);
            object.setCode(ReturnCode.ERROR_200);
        }
        return object;
    }

    public static ReturnObject getQuestionsByThemes(String theme, String pseudo) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("theme", theme);
        key.put("pseudo", pseudo);
        JSONObject obj = getJSONFromUrl("theme/getQuestionsByThemes/", key);
        ReturnObject object = new ReturnObject();

        try {
            object.setCode(ReturnCode.valueOf(obj.getString("code")));
            Collection<Question> q = null;
            if (obj != null && obj.has("object")) {
                q = getLabelQuestionsFromJsonArray(obj.getJSONArray("object"));
            }
            object.setObject(q);
        } catch (RuntimeException e) {
            object.setCode(ReturnCode.ERROR_200);
            Log.e("Runtime", "", e);
        } catch (JSONException e) {
            Log.e("JSON", "", e);
            object.setCode(ReturnCode.ERROR_200);
        }
        return object;
    }

    public static ReturnObject getQuestionsByThemesAndUser(User user, ArrayList<Theme> themesList ) {
        Map<String, String> key = new LinkedHashMap<>();

        String themes = "";
        // List of themes (with names)
        for(Theme t : themesList) {
            if("".equals(themes)) {
                themes = t.getName();
            } else {
                themes = themes + "|" + t.getName();
            }
        }
        key.put("theme", themes);
        key.put("pseudo", user.getPseudo());
        JSONObject obj = getJSONFromUrl("theme/getQuestionsByThemes", key);
        ReturnObject object = new ReturnObject();
        try {
            object.setCode(ReturnCode.valueOf(obj.getString("code")));
            Collection<Question> t = null;
            if (obj != null && obj.has("object") && obj.getString("object") != "null") {
                t = getQuestionIdAndLabelFromJsonArray(obj.getJSONArray("object"));
            }
            object.setObject(t);
        } catch (RuntimeException e) {
            object.setCode(ReturnCode.ERROR_200);
            Log.e("Runtime", "", e);
        } catch (JSONException e) {
            Log.e("JSON", "", e);
            object.setCode(ReturnCode.ERROR_200);
        }
        return object;
    }


    /**
     * Get all Questions from JSON
     * <p>
     * WARNING: Possible infinite loop !
     * A quizz contain a Question collection, and a question a collection of quizz
     *
     * @return {@link Collection<Question>}
     * @throws JSONException
     */
    private static Collection<Question> getQuestionIdAndLabelFromJsonArray(JSONArray questionsArray) throws JSONException {
        Collection<Question> questions = new ArrayList<>();
        if (questionsArray.length() != 0) {
            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject tmpObj = questionsArray.getJSONObject(i);
                Question questionTmp = new Question(tmpObj.getInt("id"), tmpObj.getString("label"), tmpObj.getString("explanation"), tmpObj.getString("pseudo"), null, null, null);
                questions.add(questionTmp);
            }
        }
        return questions;
    }


}
