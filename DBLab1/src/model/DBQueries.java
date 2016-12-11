package model;


import org.bson.Document;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observer;

public interface DBQueries {


    boolean userLogIn(String userName, String password);

    /**
     * Retrivies all records in the database
     * @param <T>
     * @return a list of all the records
     */
    <T> ArrayList<T> getAllRecords();


    void insertRecord(Object o) throws ParseException;

    void deleteRecord(Object o);

    void rateAlbum(Object o);

    int getAlbumRating(String albumId);

    ArrayList<Artist> getArtists(String albumID);

    ArrayList<Review> getReviews(String albumID);

    User getUser(String albumID);

    double avgRatingFromDoc(Document cur, int rating);

    ArrayList<String> getGenres(String albumID);

    <T> ArrayList<T> searchTitle(String title);

    <T> ArrayList<T> searchArtist(String title);
    
    Director getDirector(String albumID);

    <T> ArrayList<T> searchGenre(String title);

    <T> ArrayList<T> searchRating(int title);

    
}
