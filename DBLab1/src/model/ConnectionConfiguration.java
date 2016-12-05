package model;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Martin on 2016-11-25.
 */
public class ConnectionConfiguration {

    public static Connection getConnection(){
        Connection connection = null;
        String databaseName = "labb1";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/" + databaseName +"?autoReconnect=true&useSSL=false", "root", "root");
        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }
}
