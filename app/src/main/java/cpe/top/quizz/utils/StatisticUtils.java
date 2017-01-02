package cpe.top.quizz.utils;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Statistic;

/**
 * @author Maxence Royer
 * @version 0.1
 * @since 22/12/2016
 */
public class StatisticUtils extends JsonParser {

    public StatisticUtils() {

    }

    @Nullable
    public static ReturnObject getTenLastScoreForQuizz(String pseudo, String quizzId) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", pseudo);
        key.put("quizzId", quizzId);
        JSONObject jsonStats = getJSONFromUrl("statistic/getTenLastScoreForQuizz/", key);
        ReturnObject rO = new ReturnObject();
        List<Statistic> listStats = new ArrayList<Statistic>();

        try {
            ReturnCode rC = ReturnCode.valueOf((String) jsonStats.get("code"));

            if (rC.equals(ReturnCode.ERROR_000)) {
                JSONArray jObject = jsonStats.getJSONArray("object");

                for (int i=0; i< jObject.length(); i++) {
                    JSONObject currentElement = jObject.getJSONObject(i);
                    Statistic s = new Statistic();
                    s.setId((int) currentElement.getInt("id"));
                    s.setPseudo((String) currentElement.get("pseudo"));
                    s.setQuizzId((int) currentElement.getInt("quizzId"));
                    s.setNbRightAnswers((int) currentElement.getInt("nbRightAnswers"));
                    s.setNbQuestions((int) currentElement.get("nbQuestions"));
                    s.setQuizzName((String) currentElement.get("quizzName"));
                    listStats.add(s);
                }

                rO.setObject(listStats);
            }

            rO.setCode(rC);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return (rO != null) ? rO : null;
    }
}
