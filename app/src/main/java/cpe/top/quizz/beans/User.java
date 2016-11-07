package cpe.top.quizz.beans;


import java.io.Serializable;

/**
 *
 * @author Donatien
 * @since 06/11/2016
 * @version 1.0
 */

public class User implements Serializable{

    private String pseudo;
    private String mail;
    private String password;

    public User(String pseudo, String mail, String password) {
        super();
        this.pseudo = pseudo;
        this.mail = mail;
        this.password = password;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        return "Pseudo:" + pseudo + " password: " + password + " mail: " + mail;
    }

}
