package entities;

import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

    @Entity
    @Table(name = "teams")
    public class Team implements Serializable {

        private static final long serialVersionUID = 1L;

        @Id
        @Basic(optional = false)
        @NotNull
        @Column(name = "team_name", length = 25)
        private String teamName;
        @Basic(optional = false)
        @NotNull
        @Size(min = 1, max = 255)
        @Column(name = "team_pass")
        private String teamPass;
        @JoinTable(name = "team_link", joinColumns = {
                @JoinColumn(name = "team_name", referencedColumnName = "team_name")}, inverseJoinColumns = {
                @JoinColumn(name = "user_name", referencedColumnName = "user_name")})
        @OneToMany
        private List<User> users;

        public Team() {
        }

        //TODO Change when password is hashed
        public boolean verifyPassword(String pw, String hashedPw) {
            return BCrypt.checkpw(pw, hashedPw);
        }

        public Team(String teamName, String teamPass, List<User> users) {
            this.teamName = teamName;
            this.users = users;
            String salt =BCrypt.gensalt();
            this.teamPass = BCrypt.hashpw(teamPass, salt);
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getTeamPass() {
            return teamPass;
        }

        public void setTeamPass(String teamPass) {
            this.teamPass = teamPass;
        }

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }
    }

