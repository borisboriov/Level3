package ServerSide;

import DB.Main;
import DB.Singleton;
import ServerSide.Interface.AuthService;
import javax.swing.border.EmptyBorder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService {

//    private List<Entry> entryList;
//
//    public BaseAuthService() {
//        entryList = new ArrayList<>();
//        entryList.add(new Entry("log1", "pass1", "One"));
//        entryList.add(new Entry("log2", "pass2", "Two"));
//        entryList.add(new Entry("log3", "pass3", "Three"));
//    }

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

//    private class Entry {
//        private String login;
//        private String password;
//        private String nick;
//
//        public Entry(String login, String password, String nick) {
//            this.login = login;
//            this.password = password;
//            this.nick = nick;
//        }
//    }
}
