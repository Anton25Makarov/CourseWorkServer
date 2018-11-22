package by.bsuir.course.entities;

import java.io.Serializable;
import java.util.Objects;

public class Referee extends Human implements Serializable {
    private String login;
    private String password;
    private String sport;

    public Referee() {
    }

    public Referee(String login, String password, String sport) {
        this.login = login;
        this.password = password;
        this.sport = sport;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Referee referee = (Referee) o;
        return Objects.equals(login, referee.login) &&
                Objects.equals(password, referee.password) &&
                Objects.equals(sport, referee.sport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, sport);
    }


}
