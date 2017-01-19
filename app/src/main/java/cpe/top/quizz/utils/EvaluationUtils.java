package cpe.top.quizz.utils;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cpe.top.quizz.beans.Evaluation;
import cpe.top.quizz.beans.ReturnCode;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Statistic;

/**
 * Created by Camille on 19/01/2017.
 */

public class EvaluationUtils extends JsonParser{

    public EvaluationUtils() {

    }

    @Nullable
    public static ReturnObject getEvaluationsStatisticsForEvaluatorPseudo(String pseudo, String quizzId) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", pseudo);
        key.put("quizzId", quizzId);
        JSONObject jsonStats = getJSONFromUrl("evaluation/getEvaluationsStatisticsForEvaluatorPseudo/", key);
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

    @Nullable
    public static ReturnObject getEvaluationsForEvaluatorPseudo(String pseudo) {
        Map<String, String> key = new LinkedHashMap<>();
        key.put("pseudo", pseudo);
        JSONObject jsonStats = getJSONFromUrl("evaluation/getEvaluationsForEvaluatorPseudo/", key);
        ReturnObject rO = new ReturnObject();
        List<Evaluation> listEval = new ArrayList<>();
        try {
            ReturnCode rC = ReturnCode.valueOf((String) jsonStats.get("code"));
            if (rC.equals(ReturnCode.ERROR_000)) {
                JSONArray jObject = jsonStats.getJSONArray("object");
                for (int i=0; i< jObject.length(); i++) {
                    JSONObject currentElement = jObject.getJSONObject(i);
                    Evaluation e = new Evaluation();
                    e.setId((int) currentElement.getInt("id"));
                    e.setTargetPseudo((String) currentElement.get("targetPseudo"));
                    e.setEvaluatorPseudo((String) currentElement.get("evaluatorPseudo"));
                    e.setQuizzId((int) currentElement.getInt("quizzId"));
                    e.setQuizzName((String) currentElement.get("quizzName"));
                    e.setTimer((int) currentElement.getInt("timer"));
                    e.setTimer((int) currentElement.getInt("timer"));
                    Date date = new Date((long) currentElement.get("deadLine"));
                    e.setDeadLine(date);
                    e.setDone((Boolean) currentElement.get("done"));
                    listEval.add(e);
                }
                rO.setObject(listEval);
            }
            rO.setCode(rC);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (rO != null) ? rO : null;
    }
}
