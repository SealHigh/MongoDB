package model;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observer;

public interface DBQueries {


    void updateDB(String statement);
    /**
     * Retrivies all records in the database
     * @param <T>
     * @return a list of all the records
     */
    <T> ArrayList<T> getAllRecords();

    void insertRecord(Object o);

    void deleteRecord(Object o);

    ArrayList<Artist> getArtists(int albumID);

    ArrayList<String> getGenres(int albumID);

    /**
     * Searches through a table records with given value
     * @param option the column to search in
     * @param query the value to search for
     * @return a list of the records with the value
     */
    <T> ArrayList<T> searchRecord(SearchOptions option, String query);

    
}