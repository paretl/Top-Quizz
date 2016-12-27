package cpe.top.quizz.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Maxence Royer
 * @since 22/12/2016
 * @version 0.1
 */
public class Statistic implements Serializable {

    /**
     * Using for serialise object
     */
    private static final long serialVersionUID = 1L;

    private int id;

    private String pseudo;

    private Integer quizzId;

    private Integer nbRightAnswers;

    private Integer nbQuestions;

    private Date date;

    private String quizzzName;

    public Statistic() {
        super();
    }

    public Statistic(int id, String pseudo, Integer quizzId, String quizzzName, Integer rightAnswers, Integer nbAnswers, Date date) {
        super();
        this.id = id;
        this.pseudo = pseudo;
        this.quizzId = quizzId;
        this.quizzzName = quizzzName;
        this.nbRightAnswers = rightAnswers;
        this.nbQuestions = nbAnswers;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getNbQuestions() {
        return nbQuestions;
    }

    public void setNbQuestions(Integer nbQuestions) {
        this.nbQuestions = nbQuestions;
    }

    public Integer getNbRightAnswers() {
        return nbRightAnswers;
    }

    public void setNbRightAnswers(Integer nbRightAnswers) {
        this.nbRightAnswers = nbRightAnswers;
    }

    public Integer getQuizzId() {
        return quizzId;
    }

    public void setQuizzId(Integer quizzId) {
        this.quizzId = quizzId;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuizzzName() { return quizzzName; }

    public void setQuizzzName(String quizzzName) { this.quizzzName = quizzzName; }

    public String toString() { return this.getQuizzId().toString(); }
}
