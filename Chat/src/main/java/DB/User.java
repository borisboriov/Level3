package DB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private int id;
    private String login;
    private String password;
    private String nick;

    public User userBuilder(ResultSet set) throws SQLException {
        this.id = set.getInt(1);
        this.login = set.getString(2);
        this.password = set.getString(3);
        this.nick = set.getString(4);
        return this;
    }

    public int getId() {
        return id;
    }
    public String getLogin() { return login; }
    public String getPassword() {
        return password;
    }
    public String getNick() { return nick; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", nick='" + nick + '\'' +
                '}';
    }
}
