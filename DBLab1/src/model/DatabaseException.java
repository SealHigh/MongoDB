package model;

/**
 * Created by Martin on 2016-12-13.
 */
public class DatabaseException  extends  Exception{

    DatabaseException(String  msg){
        super(msg);
    }

    DatabaseException(){
        super();
    }
}
