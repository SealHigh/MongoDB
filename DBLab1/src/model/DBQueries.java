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

    void rateAlbum(Object o);

    int getAlbumRating(int albumId);

    ArrayList<Artist> getArtists(int albumID);

    ArrayList<Review> getReviews(int albumID);


    ArrayList<String> getGenres(int albumID);

    <T> ArrayList<T> searchTitle(String title);

    <T> ArrayList<T> searchArtist(String title);

    <T> ArrayList<T> searchGenre(String title);

    <T> ArrayList<T> searchRating(String title);

    
}