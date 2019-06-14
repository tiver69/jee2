import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionConfiguration {
    public static Connection getConnection(String URL, String user, String password) throws SQLException {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL,user,password);
        } catch (ClassNotFoundException e){
            throw new RuntimeException("Check mysql Driver");
        }
        return connection;
    }
}
