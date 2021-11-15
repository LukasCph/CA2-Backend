package DTO;

import entities.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UserDTO {

    public UserDTO() {
    }

    private String userName;
    private String userPass;

    public UserDTO(User user) {
        this.userName = user.getUserName();
        String salt = BCrypt.gensalt();
        this.userPass = BCrypt.hashpw(userPass, salt);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
}
