package cpe.top.quizz.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;

/**
 * @author Maxence Royer
 * @version 0.1
 * @since 21/11/2016
 */
public class QuizzUtils extends JsonParser {

    public QuizzUtils() {

    }

    @Nullable
    public static Quizz getQuizz(String name) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("name", name);
        JSONObject obj = getJSONFromUrl("quizz/getQuizzByName/", key);

        try {
            Quizz q = null;
            if (obj != null) {
                q = ParseUtils.getQuizzFromReturnObject(obj);
            }
            return q;
        } catch (JSONException e) {
            return null;
        }
    }

    @Nullable
    public static ReturnObject getAllQuizzByUser(String pseudo) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", pseudo);
        JSONObject jsonQuizz = getJSONFromUrl("quizz/getAllQuizzesByPseudo/", key);
        ReturnObject rO = new ReturnObject();
        List<Quizz> listQuizzes = new ArrayList<Quizz>();

        try {
            ReturnCode rC = ReturnCode.valueOf((String) jsonQuizz.get("code"));

            if (rC.equals(ReturnCode.ERROR_000)) {
                JSONArray jObject = jsonQuizz.getJSONArray("object");

                for (int i=0; i< jObject.length(); i++) {
                    JSONObject currentElement = jObject.getJSONObject(i);
                    Quizz q = new Quizz();
                    q.setId((int) currentElement.getInt("id"));
                    q.setName((String) currentElement.get("name"));
                    q.setIsVisible((String) currentElement.get("isVisible"));
                    q.setQuestions(ParseUtils.getQuestionsFromJsonArray(currentElement.getJSONArray("questions")));
                    listQuizzes.add(q);
                }

                rO.setObject(listQuizzes);
            }

            rO.setCode(rC);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return (rO != null) ? rO : null;
    }

    @Nullable
    public static ReturnObject deleteQuizz(Integer id) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("id", String.valueOf(id));
        JSONObject obj = getJSONFromUrl("quizz/deleteQuizzById/", key);

        ReturnObject rO = new ReturnObject();
        ReturnCode rC = null;
        try {
            rC = ReturnCode.valueOf((String) obj.get("code"));
            rO.setCode(rC);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rO;
    }

    public static ReturnObject saveScore(String pseudo, String quizzId, String quizzName, String nbQuestions, String nbRightAnswers) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", pseudo);
        key.put("quizzId", quizzId);
        key.put("quizzName", quizzName);
        key.put("nbRightAnswers", nbRightAnswers);
        key.put("nbQuestion", nbQuestions);


        ReturnObject rO = new ReturnObject();
        try {
            JSONObject jsonQuizz = getJSONFromUrl("statistic/addScoreForQuizz/", key);

            if(jsonQuizz != null){
                if(jsonQuizz.has("code")){
                    rO.setCode(ReturnCode.valueOf((String) jsonQuizz.get("code")));

                    if (ReturnCode.ERROR_000.equals(rO.getCode())) {
                        JSONObject jObject = jsonQuizz.getJSONObject("object");
                        rO.setObject(ParseUtils.getStatisticsFromJsonObject(jObject));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            rO.setCode(ReturnCode.ERROR_050);
        }catch (Exception e){
            e.printStackTrace();
            rO.setCode(ReturnCode.ERROR_050);
        }

        return rO;
    }

    @Nullable
    public static ReturnObject addQuizz(Quizz q) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("name", q.getName());
        key.put("visibility", "PROTECTED");
        List<Question> myQuestions = (List<Question>) q.getQuestions();
        String questions = "";
        // List of themes (with names)
        for(Question question : myQuestions) {
            if("".equals(questions)) {
                questions = Integer.toString(question.getId());
            } else {
                questions = questions + "," + Integer.toString(question.getId());
            }
        }
        key.put("questions", questions);
        JSONObject obj = getJSONFromUrl("quizz/add/", key);
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
        }
        return object;
    }

    @Nullable
    public static ReturnObject getAllFriendsByPseudo(String pseudo) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", pseudo);
        JSONObject jsonQuizz = getJSONFromUrl("friends/getAllFriendsByPseudo/", key);
        ReturnObject rO = new ReturnObject();
        List<Quizz> listQuizzes = new ArrayList<Quizz>();

        try {
            ReturnCode rC = ReturnCode.valueOf((String) jsonQuizz.get("code"));

            if (rC.equals(ReturnCode.ERROR_000)) {
                JSONArray jObject = jsonQuizz.getJSONArray("object");

                for (int i=0; i< jObject.length(); i++) {
                    JSONObject currentElement = jObject.getJSONObject(i);
                    Quizz q = new Quizz();
                    q.setId((int) currentElement.getInt("id"));
                    q.setName((String) currentElement.get("name"));
                    q.setIsVisible((String) currentElement.get("isVisible"));
                    q.setQuestions(ParseUtils.getQuestionsFromJsonArray(currentElement.getJSONArray("questions")));
                    listQuizzes.add(q);
                }

                rO.setObject(listQuizzes);
            }

            rO.setCode(rC);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return (rO != null) ? rO : null;
    }


    @Nullable
    public static ReturnObject addQuizzInMyList(String pseudo, String id) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", pseudo);
        key.put("id", id);
        JSONObject obj = getJSONFromUrl("quizz/addInList/", key);
        ReturnObject object = new ReturnObject();
        try {
            object.setCode(ReturnCode.valueOf(obj.getString("code")));
        } catch (RuntimeException e) {
            object.setCode(ReturnCode.ERROR_200);
            Log.e("Runtime", "", e);
        } catch (JSONException e) {
            Log.e("JSON", "", e);
            object.setCode(ReturnCode.ERROR_200);
        }
        return object;
    }
}
