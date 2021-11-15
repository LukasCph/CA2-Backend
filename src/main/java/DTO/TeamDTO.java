package DTO;

import entities.Team;
import entities.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class TeamDTO {

    private String teamName;
    private String teamPass;
    private List<User> users;

    public TeamDTO() {
    }

    public TeamDTO(Team team) {
        this.teamName = team.getTeamName();
        this.users = team.getUsers();
        String salt = BCrypt.gensalt();
        this.teamPass = BCrypt.hashpw(teamPass, salt);
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamPass() {
        return teamPass;
    }

    public void setTeamPass(String teamPass) {
        this.teamPass = teamPass;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "TeamDTO{" +
                "teamName='" + teamName + '\'' +
                ", teamPass='" + teamPass + '\'' +
                ", users=" + users +
                '}';
    }
}

