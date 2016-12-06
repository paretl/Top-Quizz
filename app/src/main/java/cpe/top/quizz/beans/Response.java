package cpe.top.quizz.beans;

/**
 * @author Donatien
 * @since 10/11/2016
 * @version 0.1
 */

public class Response {

    private static final long serialVersionUID = 1L;

    private int id;

    private String label;

    private Boolean isValide;

    private int idQuestion;

    public Response(String label, Boolean isValide, int idQuestion) {
        this.label = label;
        this.isValide = isValide;
        this.idQuestion = idQuestion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getValide() {
        return isValide;
    }

    public void setValide(Boolean valide) {
        isValide = valide;
    }

    public int getIdQuestion() { return idQuestion; }

    public void setIdQuestion(int idQuestion) { this.idQuestion = idQuestion; }
}
