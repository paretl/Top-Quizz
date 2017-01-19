package cpe.top.quizz.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Camille
 * @since 19/01/2017
 * @version 1.0
 */

public class Evaluation implements Serializable {

    private Integer id;

    private String evaluatorPseudo;

    private String targetPseudo;

    private Integer quizzId;

    private String quizzName;

    private Integer timer;

    private Date deadLine;

    private Boolean done;

    public Evaluation(Integer id, String evaluatorPseudo, String targetPseudo, Integer quizzId, String quizzName, Integer timer, Date deadLine, Boolean done) {
        this.id = id;
        this.evaluatorPseudo = evaluatorPseudo;
        this.targetPseudo = targetPseudo;
        this.quizzId = quizzId;
        this.quizzName = quizzName;
        this.timer = timer;
        this.deadLine = deadLine;
        this.done = done;
    }

    public Evaluation(String evaluatorPseudo, String targetPseudo, Integer quizzId, String quizzName, Integer timer, Date deadLine, Boolean done) {
        this.evaluatorPseudo = evaluatorPseudo;
        this.targetPseudo = targetPseudo;
        this.quizzId = quizzId;
        this.quizzName = quizzName;
        this.timer = timer;
        this.deadLine = deadLine;
        this.done = done;
    }

    public Evaluation() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEvaluatorPseudo() {
        return evaluatorPseudo;
    }

    public void setEvaluatorPseudo(String evaluatorPseudo) {
        this.evaluatorPseudo = evaluatorPseudo;
    }

    public String getTargetPseudo() {
        return targetPseudo;
    }

    public void setTargetPseudo(String targetPseudo) {
        this.targetPseudo = targetPseudo;
    }

    public Integer getQuizzId() {
        return quizzId;
    }

    public void setQuizzId(Integer quizzId) {
        this.quizzId = quizzId;
    }

    public String getQuizzName() {
        return quizzName;
    }

    public void setQuizzName(String quizzName) {
        this.quizzName = quizzName;
    }

    public Integer getTimer() {
        return timer;
    }

    public void setTimer(Integer timer) {
        this.timer = timer;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}
