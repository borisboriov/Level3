package ServerSide;

import DB.Singleton;
import ServerSide.Interface.AuthService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseAuthService implements AuthService {

    public static void nickChange() {
//        String[] arr = messageFromClient.split(" ", 1);
//        String nick = clientHandler.getName();

        try {
            Statement statement;
            statement = Singleton.getConnection().createStatement();
            statement.executeUpdate("UPDATE user SET nick = 'xxx' WHERE nick = 'zzzz'");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void start() {
        System.out.println("Authservice started");
    }

    @Override
    public void stop() {
        System.out.println("Authservice stopped");
    }

    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        String sql = String.format("SELECT nick FROM user where u_login = '%s' and u_password = '%s'", login, password);
        ResultSet rs = null;
        try {
            rs = Singleton.getConnection().createStatement().executeQuery(sql);

            if (rs.next()) {
                String str = rs.getString("nick");
                return str;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
