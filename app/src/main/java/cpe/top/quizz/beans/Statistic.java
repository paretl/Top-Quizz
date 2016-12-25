package cpe.top.quizz.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Donatien
 * @since 10/11/2016
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

    private String quizzName;

    private Integer nbRightAnswers;

    private Integer nbQuestions;

    private Date date;

    public Statistic(){

    }

    public Statistic(String pseudo, Integer quizzId, String quizzName, Integer rightAnswers, Integer nbQuestions, Date date) {
        super();
        this.pseudo = pseudo;
        this.quizzId = quizzId;
        this.nbRightAnswers = rightAnswers;
        this.nbQuestions = nbQuestions;
        this.date = date;
    }

    public Statistic(int id, String pseudo, Integer quizzId, String quizzName, Integer rightAnswers, Integer nbQuestions, Date date) {
        super();
        this.id = id;
        this.pseudo = pseudo;
        this.quizzId = quizzId;
        this.nbRightAnswers = rightAnswers;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public Integer getQuizzId() {
        return quizzId;
    }

    public void setQuizzId(Integer quizzId) {
        this.quizzId = quizzId;
    }

    public Integer getNbRightAnswers() {
        return nbRightAnswers;
    }

    public void setNbRightAnswers(Integer nbRightAnswers) {
        this.nbRightAnswers = nbRightAnswers;
    }

    public Integer getNbQuestions() {
        return nbQuestions;
    }

    public void setNbQuestions(Integer nbQuestions) {
        this.nbQuestions = nbQuestions;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getQuizzName() {
        return quizzName;
    }

    public void setQuizzName(String quizzName) {
        this.quizzName = quizzName;
    }
}
