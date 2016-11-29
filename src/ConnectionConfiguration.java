import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Martin on 2016-11-25.
 */
public class ConnectionConfiguration {

    public static Connection getConnection(){
        Connection connection = null;
        String databaseName = "library";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName +"?autoReconnect=true&useSSL=false", "root", "root");
        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }
}
