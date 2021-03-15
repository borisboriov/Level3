package DB;


import java.sql.*;

public class Main {

    public static void main(String[] args) {

        Statement statement = null;

        try {
            statement = Singleton.getConnection().createStatement();
            statement.executeUpdate("DELETE FROM user");
            PreparedStatement preparedStatement = null;
            Singleton.getConnection().setAutoCommit(false);
            for (int i = 1; i < 4; i++) {
                String str = "INSERT INTO user (u_login, u_password, nick)  VALUES ('log" + i + "', '1', 'one" + i + "')";
                statement.addBatch(str);
            }
            statement.executeBatch();
            Singleton.getConnection().setAutoCommit(true);

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }



        try {
            ResultSet set = statement.executeQuery("SELECT * FROM user");
            while (set.next()) {
                User user = new User().userBuilder(set);
                System.out.println(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

//} finally {
//        try {
//        if (statement != null) {
//        statement.close();
//
//        }
//        } catch (SQLException throwables) {
//        throwables.printStackTrace();
//        }
//        }
}