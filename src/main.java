import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Martin on 2016-11-25.
 */
public class main {
    public static void main(String[] args){
        Connection connection = null;
        try {
            connection =  ConnectionConfiguration.getConnection();
            Statement stmt = connection.createStatement();

            String isbnString = "test";
            stmt.executeUpdate("INSERT INTO t_book (isbn, title, publisher) VALUES ('"+isbnString+"', 'testing', 'testare')");
            ResultSet rs = stmt.executeQuery("SELECT isbn, title FROM t_book");
            while(rs.next()){
                System.out.println(rs.getString("isbn") +" "+ rs.getString("title"));

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        

    }
}
