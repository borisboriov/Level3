package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Singleton {

    static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB = "jdbc:mysql://127.0.0.1:3306/users?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASSWORD = "koptevo123";
    static private   Connection connection;

    private Singleton(){

    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection == null){
            connection = initConnection();
        }
        return connection;
    }
    public static Connection initConnection() throws SQLException, ClassNotFoundException {
        Class.forName(DRIVER);
        return DriverManager.getConnection(DB, USER, PASSWORD);
    }

}
